package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.exception.ApiException;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.ordemservico.domain.OSAdditionalRequest;
import com.neritech.saas.ordemservico.domain.OSAdditionalRequestItem;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.OSAdditionalModels;
import com.neritech.saas.ordemservico.repository.OSAdditionalRequestItemRepository;
import com.neritech.saas.ordemservico.repository.OSAdditionalRequestRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class OSAdditionalRequestService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final OSAdditionalRequestRepository requestRepository;
    private final OSAdditionalRequestItemRepository itemRepository;
    private final OrdemServicoRepository ordemServicoRepository;

    public OSAdditionalRequestService(
            OSAdditionalRequestRepository requestRepository,
            OSAdditionalRequestItemRepository itemRepository,
            OrdemServicoRepository ordemServicoRepository) {
        this.requestRepository = requestRepository;
        this.itemRepository = itemRepository;
        this.ordemServicoRepository = ordemServicoRepository;
    }

    @Transactional(readOnly = true)
    public List<OSAdditionalModels.Response> list(Long ordemServicoId) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        requireOwnedOrder(ordemServicoId, tenantId);
        return requestRepository.findByOrdemServicoIdAndEmpresaIdOrderByDataCadastroDesc(ordemServicoId, tenantId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OSAdditionalModels.Response find(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return toResponse(requireOwnedRequest(id, tenantId));
    }

    @Transactional
    public OSAdditionalModels.Response create(Long ordemServicoId, OSAdditionalModels.CreateRequest input) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OrdemServico order = requireOwnedOrder(ordemServicoId, tenantId);

        OSAdditionalRequest request = new OSAdditionalRequest();
        request.setEmpresaId(tenantId);
        request.setOrdemServicoId(order.getId());
        request.setBaseOsVersion(order.getVersao());
        request.setTitle(input.title().trim());
        request.setReason(input.reason().trim());
        request.setStatus(OSAdditionalRequest.Status.RASCUNHO);
        request = requestRepository.saveAndFlush(request);

        replaceItems(request, input.items());
        recalculate(request);
        return toResponse(requestRepository.saveAndFlush(request));
    }

    @Transactional
    public OSAdditionalModels.Response update(Long id, OSAdditionalModels.UpdateRequest input) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OSAdditionalRequest request = requireOwnedRequest(id, tenantId);
        requireDraft(request);

        request.setTitle(input.title().trim());
        request.setReason(input.reason().trim());
        replaceItems(request, input.items());
        recalculate(request);
        return toResponse(requestRepository.saveAndFlush(request));
    }

    @Transactional
    public OSAdditionalModels.SubmitResponse submit(Long id, OSAdditionalModels.SubmitRequest input) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OSAdditionalRequest request = requireOwnedRequest(id, tenantId);
        requireDraft(request);
        List<OSAdditionalRequestItem> items = itemRepository.findByAdditionalRequestIdOrderById(request.getId());
        if (items.isEmpty()) {
            throw error("Inclua ao menos um item antes de enviar a solicitação.", HttpStatus.UNPROCESSABLE_ENTITY,
                    "OS_ADDITIONAL_EMPTY");
        }

        String token = generateToken();
        LocalDateTime now = LocalDateTime.now();
        request.setRecipientName(input.recipientName().trim());
        request.setRecipientChannel(input.channel().trim().toUpperCase());
        request.setRecipientMasked(input.recipientMasked().trim());
        request.setTokenHash(hashToken(token));
        request.setTokenExpiresAt(input.expiresAt());
        request.setSubmittedAt(now);
        request.setViewedAt(null);
        request.setDecidedAt(null);
        request.setRevokedAt(null);
        request.setStatus(OSAdditionalRequest.Status.PENDENTE);
        items.forEach(item -> {
            item.setDecision(OSAdditionalRequestItem.Decision.PENDING);
            item.setDecisionComment(null);
        });
        itemRepository.saveAll(items);
        request = requestRepository.saveAndFlush(request);
        return new OSAdditionalModels.SubmitResponse(toResponse(request), token);
    }

    @Transactional
    public OSAdditionalModels.Response revoke(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OSAdditionalRequest request = requireOwnedRequest(id, tenantId);
        if (request.getStatus() != OSAdditionalRequest.Status.PENDENTE
                && request.getStatus() != OSAdditionalRequest.Status.VISUALIZADA) {
            throw error("Somente uma solicitação pendente ou visualizada pode ser revogada.",
                    HttpStatus.CONFLICT, "OS_ADDITIONAL_REVOKE_NOT_ALLOWED");
        }
        request.setStatus(OSAdditionalRequest.Status.REVOGADA);
        request.setRevokedAt(LocalDateTime.now());
        request.setTokenHash(null);
        return toResponse(requestRepository.saveAndFlush(request));
    }

    @Transactional
    public OSAdditionalModels.PublicResponse publicFind(String token) {
        OSAdditionalRequest request = requireValidToken(token);
        if (request.getStatus() == OSAdditionalRequest.Status.PENDENTE) {
            request.setStatus(OSAdditionalRequest.Status.VISUALIZADA);
            request.setViewedAt(LocalDateTime.now());
            requestRepository.save(request);
        }
        OrdemServico order = ordemServicoRepository.findByIdAndEmpresaId(request.getOrdemServicoId(), request.getEmpresaId())
                .orElseThrow(() -> error("Ordem de serviço não encontrada.", HttpStatus.NOT_FOUND, "OS_NOT_FOUND"));
        return toPublicResponse(request, order.getNumeroOS());
    }

    @Transactional
    public OSAdditionalModels.PublicResponse publicDecide(String token, OSAdditionalModels.PublicDecisionRequest input) {
        OSAdditionalRequest request = requireValidToken(token);
        if (request.getStatus() != OSAdditionalRequest.Status.PENDENTE
                && request.getStatus() != OSAdditionalRequest.Status.VISUALIZADA) {
            throw error("Esta solicitação não aceita mais decisão.", HttpStatus.CONFLICT,
                    "OS_ADDITIONAL_DECISION_NOT_ALLOWED");
        }

        List<OSAdditionalRequestItem> items = itemRepository.findByAdditionalRequestIdOrderById(request.getId());
        Map<Long, OSAdditionalModels.ItemDecision> decisions = new HashMap<>();
        for (OSAdditionalModels.ItemDecision decision : input.items()) {
            if (decisions.put(decision.itemId(), decision) != null) {
                throw error("Um item foi informado mais de uma vez.", HttpStatus.UNPROCESSABLE_ENTITY,
                        "OS_ADDITIONAL_DUPLICATE_DECISION");
            }
        }
        Set<Long> expectedIds = new HashSet<>(items.stream().map(OSAdditionalRequestItem::getId).toList());
        if (!expectedIds.equals(decisions.keySet())) {
            throw error("Decida todos os itens desta versão da solicitação.", HttpStatus.UNPROCESSABLE_ENTITY,
                    "OS_ADDITIONAL_INCOMPLETE_DECISION");
        }

        int approved = 0;
        int rejected = 0;
        for (OSAdditionalRequestItem item : items) {
            OSAdditionalModels.ItemDecision decision = decisions.get(item.getId());
            OSAdditionalRequestItem.Decision value = parseDecision(decision.decision());
            item.setDecision(value);
            item.setDecisionComment(clean(decision.comment()));
            if (value == OSAdditionalRequestItem.Decision.APPROVED) approved++;
            if (value == OSAdditionalRequestItem.Decision.REJECTED) rejected++;
        }
        itemRepository.saveAll(items);

        request.setDecidedAt(LocalDateTime.now());
        if (approved == items.size()) request.setStatus(OSAdditionalRequest.Status.APROVADA);
        else if (rejected == items.size()) request.setStatus(OSAdditionalRequest.Status.RECUSADA);
        else request.setStatus(OSAdditionalRequest.Status.PARCIAL);
        request = requestRepository.saveAndFlush(request);

        OrdemServico order = ordemServicoRepository.findByIdAndEmpresaId(request.getOrdemServicoId(), request.getEmpresaId())
                .orElseThrow(() -> error("Ordem de serviço não encontrada.", HttpStatus.NOT_FOUND, "OS_NOT_FOUND"));
        return toPublicResponse(request, order.getNumeroOS());
    }

    private void replaceItems(OSAdditionalRequest request, List<OSAdditionalModels.ItemDraft> drafts) {
        itemRepository.deleteByAdditionalRequestId(request.getId());
        List<OSAdditionalRequestItem> items = new ArrayList<>();
        for (OSAdditionalModels.ItemDraft draft : drafts) {
            OSAdditionalRequestItem item = new OSAdditionalRequestItem();
            item.setAdditionalRequestId(request.getId());
            try {
                item.setOperation(OSAdditionalRequestItem.Operation.valueOf(draft.operation().trim().toUpperCase()));
                item.setItemType(OSAdditionalRequestItem.ItemType.valueOf(draft.itemType().trim().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw error("Operação ou tipo de item inválido.", HttpStatus.UNPROCESSABLE_ENTITY,
                        "OS_ADDITIONAL_ITEM_INVALID");
            }
            item.setSourceItemId(draft.sourceItemId());
            item.setCatalogItemId(draft.catalogItemId());
            item.setDescription(draft.description().trim());
            item.setQuantity(draft.quantity());
            item.setUnit(clean(draft.unit()));
            item.setAmountDelta(nonNull(draft.amountDelta()));
            item.setTimeDeltaMinutes(nonNegative(draft.timeDeltaMinutes()));
            item.setDecision(OSAdditionalRequestItem.Decision.PENDING);
            items.add(item);
        }
        itemRepository.saveAll(items);
    }

    private void recalculate(OSAdditionalRequest request) {
        List<OSAdditionalRequestItem> items = itemRepository.findByAdditionalRequestIdOrderById(request.getId());
        request.setAmountDelta(items.stream().map(OSAdditionalRequestItem::getAmountDelta)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        request.setTimeDeltaMinutes(items.stream().mapToInt(OSAdditionalRequestItem::getTimeDeltaMinutes).sum());
    }

    private OSAdditionalRequest requireValidToken(String token) {
        if (token == null || token.isBlank()) {
            throw error("Token inválido.", HttpStatus.NOT_FOUND, "OS_ADDITIONAL_TOKEN_INVALID");
        }
        OSAdditionalRequest request = requestRepository.findByTokenHash(hashToken(token.trim()))
                .orElseThrow(() -> error("Solicitação não encontrada ou link inválido.", HttpStatus.NOT_FOUND,
                        "OS_ADDITIONAL_TOKEN_INVALID"));
        if (request.getTokenExpiresAt() == null || !request.getTokenExpiresAt().isAfter(LocalDateTime.now())) {
            if (request.getStatus() == OSAdditionalRequest.Status.PENDENTE
                    || request.getStatus() == OSAdditionalRequest.Status.VISUALIZADA) {
                request.setStatus(OSAdditionalRequest.Status.EXPIRADA);
                request.setTokenHash(null);
                requestRepository.save(request);
            }
            throw error("Este link de aprovação expirou.", HttpStatus.GONE, "OS_ADDITIONAL_TOKEN_EXPIRED");
        }
        return request;
    }

    private OSAdditionalRequest requireOwnedRequest(Long id, Long tenantId) {
        return requestRepository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> error("Solicitação de adicional não encontrada.", HttpStatus.NOT_FOUND,
                        "OS_ADDITIONAL_NOT_FOUND"));
    }

    private OrdemServico requireOwnedOrder(Long id, Long tenantId) {
        return ordemServicoRepository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> error("Ordem de serviço não encontrada.", HttpStatus.NOT_FOUND, "OS_NOT_FOUND"));
    }

    private void requireDraft(OSAdditionalRequest request) {
        if (request.getStatus() != OSAdditionalRequest.Status.RASCUNHO
                && request.getStatus() != OSAdditionalRequest.Status.PRONTA_PARA_ENVIO) {
            throw error("Uma solicitação já enviada não pode ser editada silenciosamente.", HttpStatus.CONFLICT,
                    "OS_ADDITIONAL_IMMUTABLE_VERSION");
        }
    }

    private OSAdditionalModels.Response toResponse(OSAdditionalRequest request) {
        List<OSAdditionalModels.ItemResponse> items = itemRepository.findByAdditionalRequestIdOrderById(request.getId())
                .stream().map(this::toItemResponse).toList();
        return new OSAdditionalModels.Response(
                request.getId(), request.getOrdemServicoId(), request.getBaseOsVersion(), request.getTitle(),
                request.getReason(), request.getStatus().name(), request.getAmountDelta(), request.getTimeDeltaMinutes(),
                request.getRecipientName(), request.getRecipientChannel(), request.getRecipientMasked(),
                request.getTokenExpiresAt(), request.getSubmittedAt(), request.getViewedAt(), request.getDecidedAt(),
                request.getRevokedAt(), request.getVersao(), request.getDataCadastro(), items, allowedActions(request));
    }

    private OSAdditionalModels.PublicResponse toPublicResponse(OSAdditionalRequest request, String orderNumber) {
        return new OSAdditionalModels.PublicResponse(
                request.getId(), orderNumber, request.getTitle(), request.getReason(), request.getStatus().name(),
                request.getAmountDelta(), request.getTimeDeltaMinutes(), request.getTokenExpiresAt(),
                itemRepository.findByAdditionalRequestIdOrderById(request.getId()).stream()
                        .map(this::toItemResponse).toList());
    }

    private OSAdditionalModels.ItemResponse toItemResponse(OSAdditionalRequestItem item) {
        return new OSAdditionalModels.ItemResponse(
                item.getId(), item.getOperation().name(), item.getItemType().name(), item.getSourceItemId(),
                item.getCatalogItemId(), item.getDescription(), item.getQuantity(), item.getUnit(), item.getAmountDelta(),
                item.getTimeDeltaMinutes(), item.getDecision().name(), item.getDecisionComment());
    }

    private List<String> allowedActions(OSAdditionalRequest request) {
        if (!hasAuthority("OS_EDITAR")) return List.of();
        return switch (request.getStatus()) {
            case RASCUNHO, PRONTA_PARA_ENVIO -> List.of("EDIT", "SUBMIT");
            case PENDENTE, VISUALIZADA -> List.of("REVOKE");
            default -> List.of();
        };
    }

    private boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && authentication.getAuthorities().stream().anyMatch(granted ->
                        authority.equals(granted.getAuthority()) || "ROLE_ADMIN".equals(granted.getAuthority()));
    }

    private OSAdditionalRequestItem.Decision parseDecision(String value) {
        try {
            OSAdditionalRequestItem.Decision parsed = OSAdditionalRequestItem.Decision.valueOf(value.trim().toUpperCase());
            if (parsed == OSAdditionalRequestItem.Decision.PENDING) throw new IllegalArgumentException();
            return parsed;
        } catch (Exception ex) {
            throw error("Decisão inválida. Use APPROVED ou REJECTED.", HttpStatus.UNPROCESSABLE_ENTITY,
                    "OS_ADDITIONAL_DECISION_INVALID");
        }
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 indisponível", ex);
        }
    }

    private BigDecimal nonNull(BigDecimal value) { return value == null ? BigDecimal.ZERO : value; }
    private int nonNegative(Integer value) { return value == null ? 0 : value; }
    private String clean(String value) { return value == null || value.trim().isEmpty() ? null : value.trim(); }
    private ApiException error(String message, HttpStatus status, String code) { return new ApiException(message, status, code); }
}
