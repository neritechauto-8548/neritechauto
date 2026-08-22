package com.neritech.saas.orcamento.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.orcamento.domain.OrcamentoLineItem;
import com.neritech.saas.orcamento.domain.OrcamentoServiceGroup;
import com.neritech.saas.orcamento.dto.OrcamentoAddCatalogItemRequest;
import com.neritech.saas.orcamento.dto.OrcamentoCompositionGroupResponse;
import com.neritech.saas.orcamento.dto.OrcamentoCompositionLineResponse;
import com.neritech.saas.orcamento.dto.OrcamentoCompositionResponse;
import com.neritech.saas.orcamento.dto.OrcamentoCreateGroupRequest;
import com.neritech.saas.orcamento.dto.OrcamentoCatalogItemResponse;
import com.neritech.saas.orcamento.dto.OrcamentoCatalogSearchResponse;
import com.neritech.saas.orcamento.repository.OrcamentoLineItemRepository;
import com.neritech.saas.orcamento.repository.OrcamentoServiceGroupRepository;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.domain.Servico;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;
import com.neritech.saas.produtoServico.repository.ServicoRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrcamentoCompositionService {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final OrdemServicoRepository budgetRepository;
    private final OrcamentoServiceGroupRepository groupRepository;
    private final OrcamentoLineItemRepository lineRepository;
    private final ProdutoRepository productRepository;
    private final ServicoRepository serviceRepository;

    public OrcamentoCompositionService(
            OrdemServicoRepository budgetRepository,
            OrcamentoServiceGroupRepository groupRepository,
            OrcamentoLineItemRepository lineRepository,
            ProdutoRepository productRepository,
            ServicoRepository serviceRepository) {
        this.budgetRepository = budgetRepository;
        this.groupRepository = groupRepository;
        this.lineRepository = lineRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
    }

    @Transactional(readOnly = true)
    public OrcamentoCompositionResponse get(Long budgetId) {
        Long tenantId = requireTenant();
        OrdemServico budget = budgetRepository.findByIdAndEmpresaId(budgetId, tenantId)
                .filter(item -> item.getTipoOS() == TipoOS.ORCAMENTO)
                .orElseThrow(() -> new ResourceNotFoundException("Orcamento nao encontrado no contexto autenticado."));
        return buildResponse(tenantId, budget);
    }

    @Transactional(readOnly = true)
    public OrcamentoCatalogSearchResponse searchCatalog(String rawQuery) {
        Long tenantId = requireTenant();
        String query = rawQuery == null ? "" : rawQuery.trim();
        if (query.length() < 2 || query.length() > 80) {
            throw new BusinessException("Busca de catalogo deve conter entre 2 e 80 caracteres.");
        }

        var page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nome"));
        var products = productRepository.searchActive(tenantId, query, page);
        var services = serviceRepository.searchActive(tenantId, query, page);
        List<OrcamentoCatalogItemResponse> items = new ArrayList<>();
        products.forEach(product -> items.add(new OrcamentoCatalogItemResponse(
                product.getId(), "PART", product.getNome(), product.getCodigoInterno(),
                money(product.getPrecoVenda()),
                resolveAvailability(product.getQuantidadeEstoque(), BigDecimal.ONE).name())));
        services.forEach(catalogService -> items.add(new OrcamentoCatalogItemResponse(
                catalogService.getId(), "LABOR", catalogService.getNome(), null,
                money(catalogService.getPrecoBase()), "NOT_APPLICABLE")));
        return new OrcamentoCatalogSearchResponse(
                query,
                List.copyOf(items),
                products.hasNext() || services.hasNext());
    }

    @Transactional
    public OrcamentoCompositionResponse createGroup(Long budgetId, OrcamentoCreateGroupRequest request) {
        Long tenantId = requireTenant();
        OrdemServico budget = lockBudget(tenantId, budgetId);
        assertRevision(budget, request.expectedRevision());

        OrcamentoServiceGroup group = new OrcamentoServiceGroup();
        group.setEmpresaId(tenantId);
        group.setOrcamento(budget);
        group.setTitle(normalizeTitle(request.title()));
        group.setCustomerDescription(trimToNull(request.customerDescription()));
        group.setRecommended(request.recommended());
        group.setVisibility(parseVisibility(request.visibility()));
        group.setPosition(Math.toIntExact(groupRepository.countByEmpresaIdAndOrcamentoId(tenantId, budgetId)));
        groupRepository.save(group);

        advanceRevision(budget);
        budgetRepository.saveAndFlush(budget);
        return buildResponse(tenantId, budget);
    }

    @Transactional
    public OrcamentoCompositionResponse addCatalogItem(
            Long budgetId,
            Long groupId,
            OrcamentoAddCatalogItemRequest request) {
        Long tenantId = requireTenant();
        OrdemServico budget = lockBudget(tenantId, budgetId);
        assertRevision(budget, request.expectedRevision());
        OrcamentoServiceGroup group = groupRepository
                .findByIdAndEmpresaIdAndOrcamentoId(groupId, tenantId, budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo nao encontrado no orcamento autenticado."));

        OrcamentoLineItem item = resolveCatalogSnapshot(tenantId, group, request);
        item.setEmpresaId(tenantId);
        item.setPosition(Math.toIntExact(lineRepository.countByEmpresaIdAndGroupId(tenantId, groupId)));
        lineRepository.save(item);

        advanceRevision(budget);
        recalculateBudget(tenantId, budget);
        budgetRepository.saveAndFlush(budget);
        return buildResponse(tenantId, budget);
    }

    private OrcamentoLineItem resolveCatalogSnapshot(
            Long tenantId,
            OrcamentoServiceGroup group,
            OrcamentoAddCatalogItemRequest request) {
        OrcamentoLineItem item = new OrcamentoLineItem();
        item.setGroup(group);
        item.setCatalogItemId(request.catalogItemId());
        item.setQuantity(request.quantity().setScale(3, RoundingMode.HALF_UP));
        item.setDiscountAmount(ZERO);

        if ("PART".equals(request.lineType())) {
            Produto product = productRepository.findByIdAndEmpresaId(request.catalogItemId(), tenantId)
                    .filter(value -> Boolean.TRUE.equals(value.getAtivo()))
                    .orElseThrow(() -> new BusinessException("Peca inexistente ou inativa no catalogo autenticado."));
            item.setLineType(OrcamentoLineItem.LineType.PART);
            item.setSource(OrcamentoLineItem.Source.PRODUCT_CATALOG);
            item.setDescriptionSnapshot(product.getNome());
            item.setReferenceSnapshot(product.getCodigoInterno());
            item.setCatalogVersion(product.getVersao());
            item.setUnitPrice(requireCanonicalPrice(product.getPrecoVenda()));
            item.setAvailabilityStatus(resolveAvailability(product.getQuantidadeEstoque(), request.quantity()));
        } else if ("LABOR".equals(request.lineType())) {
            Servico catalogService = serviceRepository.findByIdAndEmpresaId(request.catalogItemId(), tenantId)
                    .filter(value -> Boolean.TRUE.equals(value.getAtivo()))
                    .orElseThrow(() -> new BusinessException("Servico inexistente ou inativo no catalogo autenticado."));
            item.setLineType(OrcamentoLineItem.LineType.LABOR);
            item.setSource(OrcamentoLineItem.Source.SERVICE_CATALOG);
            item.setDescriptionSnapshot(catalogService.getNome());
            item.setReferenceSnapshot(null);
            item.setCatalogVersion(catalogService.getVersao());
            item.setUnitPrice(requireCanonicalPrice(catalogService.getPrecoBase()));
            item.setAvailabilityStatus(OrcamentoLineItem.AvailabilityStatus.NOT_APPLICABLE);
        } else {
            throw new BusinessException("Tipo de linha nao suportado para inclusao pelo catalogo.");
        }

        item.setTotalAmount(calculateLineTotal(item.getQuantity(), item.getUnitPrice(), item.getDiscountAmount()));
        return item;
    }

    private OrcamentoLineItem.AvailabilityStatus resolveAvailability(BigDecimal available, BigDecimal requested) {
        BigDecimal safeAvailable = available == null ? BigDecimal.ZERO : available.max(BigDecimal.ZERO);
        if (safeAvailable.compareTo(requested) >= 0) return OrcamentoLineItem.AvailabilityStatus.AVAILABLE;
        if (safeAvailable.signum() > 0) return OrcamentoLineItem.AvailabilityStatus.PARTIAL;
        return OrcamentoLineItem.AvailabilityStatus.NEEDED;
    }

    private void recalculateBudget(Long tenantId, OrdemServico budget) {
        List<OrcamentoServiceGroup> groups = groupRepository
                .findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(tenantId, budget.getId());
        Map<Long, OrcamentoServiceGroup> groupById = new LinkedHashMap<>();
        groups.forEach(group -> groupById.put(group.getId(), group));
        List<OrcamentoLineItem> lines = lineRepository.findCompositionLines(tenantId, budget.getId());

        BigDecimal parts = ZERO;
        BigDecimal labor = ZERO;
        for (OrcamentoLineItem line : lines) {
            OrcamentoServiceGroup group = groupById.get(line.getGroup().getId());
            if (group == null || group.getVisibility() == OrcamentoServiceGroup.Visibility.INTERNAL_ONLY || group.isRecommended()) {
                continue;
            }
            if (line.getLineType() == OrcamentoLineItem.LineType.PART) parts = parts.add(line.getTotalAmount());
            if (line.getLineType() == OrcamentoLineItem.LineType.LABOR) labor = labor.add(line.getTotalAmount());
        }
        budget.setValorProdutos(money(parts));
        budget.setValorServicos(money(labor));
        budget.setValorTotal(money(parts.add(labor)));
    }

    private OrcamentoCompositionResponse buildResponse(Long tenantId, OrdemServico budget) {
        List<OrcamentoServiceGroup> groups = groupRepository
                .findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(tenantId, budget.getId());
        List<OrcamentoLineItem> lines = lineRepository.findCompositionLines(tenantId, budget.getId());
        Map<Long, List<OrcamentoLineItem>> linesByGroup = new LinkedHashMap<>();
        lines.forEach(line -> linesByGroup.computeIfAbsent(line.getGroup().getId(), ignored -> new ArrayList<>()).add(line));

        BigDecimal required = ZERO;
        BigDecimal recommended = ZERO;
        BigDecimal parts = ZERO;
        BigDecimal labor = ZERO;
        List<OrcamentoCompositionGroupResponse> groupResponses = new ArrayList<>();

        for (OrcamentoServiceGroup group : groups) {
            List<OrcamentoLineItem> groupLines = linesByGroup.getOrDefault(group.getId(), List.of());
            BigDecimal subtotal = groupLines.stream()
                    .map(OrcamentoLineItem::getTotalAmount)
                    .reduce(ZERO, BigDecimal::add);
            if (group.getVisibility() == OrcamentoServiceGroup.Visibility.CUSTOMER_VISIBLE) {
                if (group.isRecommended()) recommended = recommended.add(subtotal);
                else required = required.add(subtotal);
            }
            if (!group.isRecommended() && group.getVisibility() == OrcamentoServiceGroup.Visibility.CUSTOMER_VISIBLE) {
                parts = parts.add(sumType(groupLines, OrcamentoLineItem.LineType.PART));
                labor = labor.add(sumType(groupLines, OrcamentoLineItem.LineType.LABOR));
            }
            groupResponses.add(mapGroup(group, groupLines, subtotal));
        }

        List<String> blockers = new ArrayList<>();
        if (groups.isEmpty()) blockers.add("Adicione ao menos um grupo de servico.");
        if (!groups.isEmpty() && lines.isEmpty()) blockers.add("Adicione ao menos uma peca ou mao de obra.");
        boolean canReview = blockers.isEmpty();

        return new OrcamentoCompositionResponse(
                budget.getId(),
                budget.getCompositionRevision() == null ? 0 : budget.getCompositionRevision(),
                lines.isEmpty() ? "EMPTY" : "CURRENT",
                "BRL",
                money(required),
                money(recommended),
                money(parts),
                money(labor),
                groups.size(),
                lines.size(),
                canReview,
                List.copyOf(blockers),
                List.copyOf(groupResponses));
    }

    private OrcamentoCompositionGroupResponse mapGroup(
            OrcamentoServiceGroup group,
            List<OrcamentoLineItem> lines,
            BigDecimal subtotal) {
        return new OrcamentoCompositionGroupResponse(
                group.getId(),
                group.getTitle(),
                group.getCustomerDescription(),
                group.isRecommended(),
                group.getVisibility().name(),
                group.getPosition(),
                money(subtotal),
                lines.stream().map(this::mapLine).toList());
    }

    private OrcamentoCompositionLineResponse mapLine(OrcamentoLineItem item) {
        return new OrcamentoCompositionLineResponse(
                item.getId(), item.getLineType().name(), item.getCatalogItemId(), item.getCatalogVersion(),
                item.getSource().name(), item.getDescriptionSnapshot(), item.getReferenceSnapshot(),
                item.getQuantity(), item.getUnitPrice(), item.getDiscountAmount(), item.getTotalAmount(),
                item.getAvailabilityStatus().name(), item.getPosition());
    }

    private BigDecimal sumType(List<OrcamentoLineItem> lines, OrcamentoLineItem.LineType type) {
        return lines.stream()
                .filter(line -> line.getLineType() == type)
                .map(OrcamentoLineItem::getTotalAmount)
                .reduce(ZERO, BigDecimal::add);
    }

    private OrdemServico lockBudget(Long tenantId, Long budgetId) {
        return budgetRepository.findBudgetForCompositionUpdate(budgetId, tenantId, TipoOS.ORCAMENTO)
                .orElseThrow(() -> new ResourceNotFoundException("Orcamento nao encontrado no contexto autenticado."));
    }

    private void assertRevision(OrdemServico budget, Long expectedRevision) {
        long current = budget.getCompositionRevision() == null ? 0 : budget.getCompositionRevision();
        if (expectedRevision == null || expectedRevision != current) {
            throw new OptimisticLockingFailureException(
                    "Conflito de composicao: recarregue o orcamento antes de continuar.");
        }
    }

    private void advanceRevision(OrdemServico budget) {
        long current = budget.getCompositionRevision() == null ? 0 : budget.getCompositionRevision();
        budget.setCompositionRevision(current + 1);
    }

    private BigDecimal requireCanonicalPrice(BigDecimal price) {
        if (price == null || price.signum() < 0) {
            throw new BusinessException("Catalogo sem preco canonico valido para este item.");
        }
        return price.setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateLineTotal(BigDecimal quantity, BigDecimal unitPrice, BigDecimal discount) {
        BigDecimal total = quantity.multiply(unitPrice).subtract(discount);
        if (total.signum() < 0) throw new BusinessException("Desconto nao pode superar o valor da linha.");
        return money(total);
    }

    private BigDecimal money(BigDecimal value) {
        return (value == null ? BigDecimal.ZERO : value).setScale(2, RoundingMode.HALF_UP);
    }

    private OrcamentoServiceGroup.Visibility parseVisibility(String value) {
        if (value == null || value.isBlank()) return OrcamentoServiceGroup.Visibility.CUSTOMER_VISIBLE;
        return OrcamentoServiceGroup.Visibility.valueOf(value);
    }

    private String normalizeTitle(String value) {
        String title = value == null ? "" : value.trim().replaceAll("\\s+", " ");
        if (!title.matches(".*[\\p{L}\\p{N}].*")) {
            throw new BusinessException("Titulo do grupo precisa conter letras ou numeros.");
        }
        return title;
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Long requireTenant() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) throw new IllegalStateException("Tenant autenticado nao disponivel para composicao.");
        return tenantId;
    }
}
