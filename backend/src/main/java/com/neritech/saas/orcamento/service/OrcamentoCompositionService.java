package com.neritech.saas.orcamento.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.orcamento.domain.OrcamentoLineItem;
import com.neritech.saas.orcamento.domain.OrcamentoServiceGroup;
import com.neritech.saas.orcamento.domain.CatalogKit;
import com.neritech.saas.orcamento.domain.CatalogKitVersion;
import com.neritech.saas.orcamento.domain.CatalogKitVersionItem;
import com.neritech.saas.orcamento.domain.OrcamentoKitInstantiation;
import com.neritech.saas.orcamento.dto.OrcamentoAddCatalogItemRequest;
import com.neritech.saas.orcamento.dto.OrcamentoCompositionGroupResponse;
import com.neritech.saas.orcamento.dto.OrcamentoCompositionLineResponse;
import com.neritech.saas.orcamento.dto.OrcamentoCompositionResponse;
import com.neritech.saas.orcamento.dto.OrcamentoCreateGroupRequest;
import com.neritech.saas.orcamento.dto.OrcamentoCatalogItemResponse;
import com.neritech.saas.orcamento.dto.OrcamentoCatalogSearchResponse;
import com.neritech.saas.orcamento.dto.OrcamentoReorderRequest;
import com.neritech.saas.orcamento.dto.OrcamentoRevisionRequest;
import com.neritech.saas.orcamento.dto.OrcamentoUpdateGroupRequest;
import com.neritech.saas.orcamento.dto.OrcamentoUpdateLineRequest;
import com.neritech.saas.orcamento.dto.OrcamentoInstantiateKitRequest;
import com.neritech.saas.orcamento.repository.CatalogKitRepository;
import com.neritech.saas.orcamento.repository.CatalogKitVersionItemRepository;
import com.neritech.saas.orcamento.repository.CatalogKitVersionRepository;
import com.neritech.saas.orcamento.repository.OrcamentoKitInstantiationRepository;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class OrcamentoCompositionService {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final OrdemServicoRepository budgetRepository;
    private final OrcamentoServiceGroupRepository groupRepository;
    private final OrcamentoLineItemRepository lineRepository;
    private final ProdutoRepository productRepository;
    private final ServicoRepository serviceRepository;
    private final CatalogKitRepository kitRepository;
    private final CatalogKitVersionRepository kitVersionRepository;
    private final CatalogKitVersionItemRepository kitItemRepository;
    private final OrcamentoKitInstantiationRepository kitInstantiationRepository;

    public OrcamentoCompositionService(
            OrdemServicoRepository budgetRepository,
            OrcamentoServiceGroupRepository groupRepository,
            OrcamentoLineItemRepository lineRepository,
            ProdutoRepository productRepository,
            ServicoRepository serviceRepository,
            CatalogKitRepository kitRepository,
            CatalogKitVersionRepository kitVersionRepository,
            CatalogKitVersionItemRepository kitItemRepository,
            OrcamentoKitInstantiationRepository kitInstantiationRepository) {
        this.budgetRepository = budgetRepository;
        this.groupRepository = groupRepository;
        this.lineRepository = lineRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
        this.kitRepository = kitRepository;
        this.kitVersionRepository = kitVersionRepository;
        this.kitItemRepository = kitItemRepository;
        this.kitInstantiationRepository = kitInstantiationRepository;
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
        return searchCatalog(rawQuery, null);
    }

    @Transactional(readOnly = true)
    public OrcamentoCatalogSearchResponse searchCatalog(String rawQuery, String rawType) {
        Long tenantId = requireTenant();
        String query = rawQuery == null ? "" : rawQuery.trim();
        if (query.length() < 2 || query.length() > 80) {
            throw new BusinessException("Busca de catalogo deve conter entre 2 e 80 caracteres.");
        }
        String type = rawType == null || rawType.isBlank() ? "ALL" : rawType.trim().toUpperCase();
        if (!Set.of("ALL", "PART", "LABOR", "KIT").contains(type)) {
            throw new BusinessException("Filtro de catalogo nao suportado.");
        }
        var page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nome"));
        var products = Set.of("ALL", "PART").contains(type)
                ? productRepository.searchActive(tenantId, query, page)
                : org.springframework.data.domain.Page.<Produto>empty();
        var services = Set.of("ALL", "LABOR").contains(type)
                ? serviceRepository.searchActive(tenantId, query, page)
                : org.springframework.data.domain.Page.<Servico>empty();
        var kits = Set.of("ALL", "KIT").contains(type)
                ? kitRepository.searchActive(tenantId, query, page)
                : org.springframework.data.domain.Page.<CatalogKit>empty();
        List<OrcamentoCatalogItemResponse> items = new ArrayList<>();
        products.forEach(product -> items.add(new OrcamentoCatalogItemResponse(
                product.getId(), "PART", product.getNome(), product.getCodigoInterno(),
                money(product.getPrecoVenda()),
                resolveAvailability(product.getQuantidadeEstoque(), BigDecimal.ONE).name(),
                1,
                product.getVersao())));
        services.forEach(catalogService -> items.add(new OrcamentoCatalogItemResponse(
                catalogService.getId(), "LABOR", catalogService.getNome(), null,
                money(catalogService.getPrecoBase()), "NOT_APPLICABLE", 1, catalogService.getVersao())));
        kits.forEach(kit -> mapCatalogKit(tenantId, kit).ifPresent(items::add));
        return new OrcamentoCatalogSearchResponse(
                query,
                List.copyOf(items),
                products.hasNext() || services.hasNext() || kits.hasNext());
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

        return finishMutation(tenantId, budget);
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

        return finishMutation(tenantId, budget);
    }

    @Transactional
    public OrcamentoCompositionResponse instantiateKit(
            Long budgetId,
            Long kitId,
            String rawIdempotencyKey,
            OrcamentoInstantiateKitRequest request) {
        Long tenantId = requireTenant();
        OrdemServico budget = lockBudget(tenantId, budgetId);
        String idempotencyKey = normalizeIdempotencyKey(rawIdempotencyKey);
        String fingerprint = fingerprintKitRequest(kitId, request);

        Optional<OrcamentoKitInstantiation> previous = kitInstantiationRepository
                .findByEmpresaIdAndOrcamentoIdAndIdempotencyKey(tenantId, budgetId, idempotencyKey);
        if (previous.isPresent()) {
            if (!previous.get().getRequestFingerprint().equals(fingerprint)) {
                throw new BusinessException("Idempotency-Key ja utilizada com outro pedido de kit.");
            }
            return buildResponse(tenantId, budget);
        }

        assertRevision(budget, request.expectedRevision());
        CatalogKit kit = kitRepository.findByIdAndEmpresaIdAndActiveTrue(kitId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Kit ativo nao encontrado no catalogo autenticado."));
        CatalogKitVersion version = kitVersionRepository
                .findByEmpresaIdAndKitIdAndVersionNumberAndPublishedTrue(
                        tenantId, kitId, kit.getCurrentVersion())
                .orElseThrow(() -> new BusinessException("Kit sem versao publicada disponivel."));
        List<CatalogKitVersionItem> kitItems =
                kitItemRepository.findByEmpresaIdAndKitVersionIdOrderByPositionAsc(tenantId, version.getId());
        if (kitItems.isEmpty()) throw new BusinessException("Kit publicado nao possui itens instanciaveis.");

        List<OrcamentoServiceGroup> currentGroups =
                groupRepository.findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(tenantId, budgetId);
        int targetPosition = request.targetPosition() == null ? currentGroups.size() : request.targetPosition();
        if (targetPosition > currentGroups.size()) {
            throw new BusinessException("Posicao de destino do kit nao existe na composicao atual.");
        }
        for (int index = 0; index < currentGroups.size(); index++) {
            currentGroups.get(index).setPosition(1_000_000 + index);
        }
        if (!currentGroups.isEmpty()) groupRepository.saveAllAndFlush(currentGroups);

        OrcamentoServiceGroup group = new OrcamentoServiceGroup();
        group.setEmpresaId(tenantId);
        group.setOrcamento(budget);
        group.setTitle(version.getTitleSnapshot());
        group.setCustomerDescription(version.getDescriptionSnapshot());
        group.setRecommended(version.isRecommendedDefault());
        group.setVisibility(OrcamentoServiceGroup.Visibility.CUSTOMER_VISIBLE);
        group.setPosition(targetPosition);
        group.setKitOriginId(kitId);
        group.setKitOriginVersion(version.getVersionNumber());
        groupRepository.saveAndFlush(group);

        List<OrcamentoServiceGroup> finalOrder = new ArrayList<>(currentGroups);
        finalOrder.add(targetPosition, group);
        for (int index = 0; index < finalOrder.size(); index++) finalOrder.get(index).setPosition(index);
        groupRepository.saveAll(finalOrder);

        BigDecimal kitQuantity = request.quantity().setScale(3, RoundingMode.HALF_UP);
        List<OrcamentoLineItem> lines = new ArrayList<>();
        for (int index = 0; index < kitItems.size(); index++) {
            lines.add(instantiateKitLine(tenantId, group, kitId, version, kitItems.get(index), kitQuantity, index));
        }
        lineRepository.saveAll(lines);

        OrcamentoKitInstantiation instantiation = new OrcamentoKitInstantiation();
        instantiation.setEmpresaId(tenantId);
        instantiation.setOrcamento(budget);
        instantiation.setGroup(group);
        instantiation.setKitOriginId(kitId);
        instantiation.setKitOriginVersion(version.getVersionNumber());
        instantiation.setIdempotencyKey(idempotencyKey);
        instantiation.setRequestFingerprint(fingerprint);
        kitInstantiationRepository.save(instantiation);

        return finishMutation(tenantId, budget);
    }

    @Transactional
    public OrcamentoCompositionResponse updateGroup(
            Long budgetId,
            Long groupId,
            OrcamentoUpdateGroupRequest request) {
        Long tenantId = requireTenant();
        OrdemServico budget = lockBudget(tenantId, budgetId);
        assertRevision(budget, request.expectedRevision());
        OrcamentoServiceGroup group = requireGroup(tenantId, budgetId, groupId);

        group.setTitle(normalizeTitle(request.title()));
        group.setCustomerDescription(trimToNull(request.customerDescription()));
        group.setInternalNote(trimToNull(request.internalNote()));
        group.setRecommended(request.recommended());
        group.setVisibility(parseVisibility(request.visibility()));
        groupRepository.save(group);

        return finishMutation(tenantId, budget);
    }

    @Transactional
    public OrcamentoCompositionResponse duplicateGroup(
            Long budgetId,
            Long groupId,
            OrcamentoRevisionRequest request) {
        Long tenantId = requireTenant();
        OrdemServico budget = lockBudget(tenantId, budgetId);
        assertRevision(budget, request.expectedRevision());
        OrcamentoServiceGroup source = requireGroup(tenantId, budgetId, groupId);

        OrcamentoServiceGroup duplicate = new OrcamentoServiceGroup();
        duplicate.setEmpresaId(tenantId);
        duplicate.setOrcamento(budget);
        duplicate.setTitle(copyTitle(source.getTitle()));
        duplicate.setCustomerDescription(source.getCustomerDescription());
        duplicate.setInternalNote(source.getInternalNote());
        duplicate.setRecommended(source.isRecommended());
        duplicate.setVisibility(source.getVisibility());
        duplicate.setKitOriginId(source.getKitOriginId());
        duplicate.setKitOriginVersion(source.getKitOriginVersion());
        duplicate.setPosition(Math.toIntExact(groupRepository.countByEmpresaIdAndOrcamentoId(tenantId, budgetId)));
        groupRepository.saveAndFlush(duplicate);

        List<OrcamentoLineItem> sourceLines =
                lineRepository.findByEmpresaIdAndGroupIdOrderByPositionAsc(tenantId, groupId);
        List<OrcamentoLineItem> duplicateLines = sourceLines.stream()
                .map(line -> copyLine(tenantId, duplicate, line, line.getPosition()))
                .toList();
        lineRepository.saveAll(duplicateLines);

        return finishMutation(tenantId, budget);
    }

    @Transactional
    public OrcamentoCompositionResponse deleteGroup(Long budgetId, Long groupId, Long expectedRevision) {
        Long tenantId = requireTenant();
        OrdemServico budget = lockBudget(tenantId, budgetId);
        assertRevision(budget, expectedRevision);
        OrcamentoServiceGroup group = requireGroup(tenantId, budgetId, groupId);

        groupRepository.delete(group);
        groupRepository.flush();
        normalizeGroupPositions(tenantId, budgetId);

        return finishMutation(tenantId, budget);
    }

    @Transactional
    public OrcamentoCompositionResponse reorderGroups(Long budgetId, OrcamentoReorderRequest request) {
        Long tenantId = requireTenant();
        OrdemServico budget = lockBudget(tenantId, budgetId);
        assertRevision(budget, request.expectedRevision());
        List<OrcamentoServiceGroup> groups =
                groupRepository.findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(tenantId, budgetId);
        validateExactOrder(request.orderedIds(), groups.stream().map(OrcamentoServiceGroup::getId).toList(), "grupos");

        Map<Long, OrcamentoServiceGroup> byId = new LinkedHashMap<>();
        groups.forEach(group -> byId.put(group.getId(), group));
        persistGroupOrder(request.orderedIds().stream().map(byId::get).toList());

        return finishMutation(tenantId, budget);
    }

    @Transactional
    public OrcamentoCompositionResponse updateLine(
            Long budgetId,
            Long groupId,
            Long itemId,
            OrcamentoUpdateLineRequest request) {
        Long tenantId = requireTenant();
        OrdemServico budget = lockBudget(tenantId, budgetId);
        assertRevision(budget, request.expectedRevision());
        OrcamentoLineItem item = requireLine(tenantId, budgetId, groupId, itemId);

        BigDecimal quantity = request.quantity().setScale(3, RoundingMode.HALF_UP);
        item.setQuantity(quantity);
        item.setAvailabilityStatus(refreshAvailability(tenantId, item, quantity));
        item.setTotalAmount(calculateLineTotal(quantity, item.getUnitPrice(), item.getDiscountAmount()));
        lineRepository.save(item);

        return finishMutation(tenantId, budget);
    }

    @Transactional
    public OrcamentoCompositionResponse duplicateLine(
            Long budgetId,
            Long groupId,
            Long itemId,
            OrcamentoRevisionRequest request) {
        Long tenantId = requireTenant();
        OrdemServico budget = lockBudget(tenantId, budgetId);
        assertRevision(budget, request.expectedRevision());
        OrcamentoServiceGroup group = requireGroup(tenantId, budgetId, groupId);
        OrcamentoLineItem source = requireLine(tenantId, budgetId, groupId, itemId);

        int position = Math.toIntExact(lineRepository.countByEmpresaIdAndGroupId(tenantId, groupId));
        lineRepository.save(copyLine(tenantId, group, source, position));

        return finishMutation(tenantId, budget);
    }

    @Transactional
    public OrcamentoCompositionResponse deleteLine(
            Long budgetId,
            Long groupId,
            Long itemId,
            Long expectedRevision) {
        Long tenantId = requireTenant();
        OrdemServico budget = lockBudget(tenantId, budgetId);
        assertRevision(budget, expectedRevision);
        OrcamentoLineItem item = requireLine(tenantId, budgetId, groupId, itemId);

        lineRepository.delete(item);
        lineRepository.flush();
        normalizeLinePositions(tenantId, groupId);

        return finishMutation(tenantId, budget);
    }

    @Transactional
    public OrcamentoCompositionResponse reorderLines(
            Long budgetId,
            Long groupId,
            OrcamentoReorderRequest request) {
        Long tenantId = requireTenant();
        OrdemServico budget = lockBudget(tenantId, budgetId);
        assertRevision(budget, request.expectedRevision());
        requireGroup(tenantId, budgetId, groupId);
        List<OrcamentoLineItem> lines =
                lineRepository.findByEmpresaIdAndGroupIdOrderByPositionAsc(tenantId, groupId);
        validateExactOrder(request.orderedIds(), lines.stream().map(OrcamentoLineItem::getId).toList(), "itens");

        Map<Long, OrcamentoLineItem> byId = new LinkedHashMap<>();
        lines.forEach(line -> byId.put(line.getId(), line));
        persistLineOrder(request.orderedIds().stream().map(byId::get).toList());

        return finishMutation(tenantId, budget);
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

    private Optional<OrcamentoCatalogItemResponse> mapCatalogKit(Long tenantId, CatalogKit kit) {
        Optional<CatalogKitVersion> versionOptional = kitVersionRepository
                .findByEmpresaIdAndKitIdAndVersionNumberAndPublishedTrue(
                        tenantId, kit.getId(), kit.getCurrentVersion());
        if (versionOptional.isEmpty()) return Optional.empty();
        CatalogKitVersion version = versionOptional.get();
        List<CatalogKitVersionItem> items =
                kitItemRepository.findByEmpresaIdAndKitVersionIdOrderByPositionAsc(tenantId, version.getId());
        if (items.isEmpty()) return Optional.empty();

        BigDecimal total = items.stream()
                .map(item -> item.getQuantity().multiply(item.getUnitPriceSnapshot()))
                .reduce(ZERO, BigDecimal::add);
        return Optional.of(new OrcamentoCatalogItemResponse(
                kit.getId(),
                "KIT",
                version.getTitleSnapshot(),
                kit.getReference(),
                money(total),
                resolveKitAvailability(tenantId, items).name(),
                items.size(),
                version.getVersionNumber()));
    }

    private OrcamentoLineItem instantiateKitLine(
            Long tenantId,
            OrcamentoServiceGroup group,
            Long kitId,
            CatalogKitVersion version,
            CatalogKitVersionItem source,
            BigDecimal kitQuantity,
            int position) {
        OrcamentoLineItem line = new OrcamentoLineItem();
        line.setEmpresaId(tenantId);
        line.setGroup(group);
        line.setLineType(source.getLineType() == CatalogKitVersionItem.LineType.PART
                ? OrcamentoLineItem.LineType.PART
                : OrcamentoLineItem.LineType.LABOR);
        line.setCatalogItemId(source.getCatalogItemId());
        line.setCatalogVersion(source.getCatalogVersion());
        line.setSource(OrcamentoLineItem.Source.KIT);
        line.setKitOriginId(kitId);
        line.setKitOriginVersion(version.getVersionNumber());
        line.setDescriptionSnapshot(source.getDescriptionSnapshot());
        line.setReferenceSnapshot(source.getReferenceSnapshot());
        BigDecimal quantity = source.getQuantity().multiply(kitQuantity).setScale(3, RoundingMode.HALF_UP);
        if (quantity.signum() <= 0) throw new BusinessException("Quantidade resultante do item do kit deve ser positiva.");
        line.setQuantity(quantity);
        line.setUnitPrice(requireCanonicalPrice(source.getUnitPriceSnapshot()));
        line.setDiscountAmount(ZERO);
        line.setAvailabilityStatus(refreshAvailability(tenantId, line, quantity));
        line.setTotalAmount(calculateLineTotal(quantity, line.getUnitPrice(), line.getDiscountAmount()));
        line.setPosition(position);
        return line;
    }

    private OrcamentoLineItem.AvailabilityStatus resolveKitAvailability(
            Long tenantId,
            List<CatalogKitVersionItem> items) {
        boolean hasPart = false;
        OrcamentoLineItem.AvailabilityStatus aggregate = OrcamentoLineItem.AvailabilityStatus.AVAILABLE;
        for (CatalogKitVersionItem item : items) {
            if (item.getLineType() != CatalogKitVersionItem.LineType.PART) continue;
            hasPart = true;
            OrcamentoLineItem.AvailabilityStatus current = productRepository
                    .findByIdAndEmpresaId(item.getCatalogItemId(), tenantId)
                    .map(product -> resolveAvailability(product.getQuantidadeEstoque(), item.getQuantity()))
                    .orElse(OrcamentoLineItem.AvailabilityStatus.NEEDED);
            if (current == OrcamentoLineItem.AvailabilityStatus.NEEDED) return current;
            if (current == OrcamentoLineItem.AvailabilityStatus.PARTIAL) aggregate = current;
        }
        return hasPart ? aggregate : OrcamentoLineItem.AvailabilityStatus.NOT_APPLICABLE;
    }

    private OrcamentoLineItem.AvailabilityStatus resolveAvailability(BigDecimal available, BigDecimal requested) {
        BigDecimal safeAvailable = available == null ? BigDecimal.ZERO : available.max(BigDecimal.ZERO);
        if (safeAvailable.compareTo(requested) >= 0) return OrcamentoLineItem.AvailabilityStatus.AVAILABLE;
        if (safeAvailable.signum() > 0) return OrcamentoLineItem.AvailabilityStatus.PARTIAL;
        return OrcamentoLineItem.AvailabilityStatus.NEEDED;
    }

    private OrcamentoLineItem.AvailabilityStatus refreshAvailability(
            Long tenantId,
            OrcamentoLineItem item,
            BigDecimal requested) {
        if (item.getLineType() != OrcamentoLineItem.LineType.PART || item.getCatalogItemId() == null) {
            return OrcamentoLineItem.AvailabilityStatus.NOT_APPLICABLE;
        }
        return productRepository.findByIdAndEmpresaId(item.getCatalogItemId(), tenantId)
                .map(product -> resolveAvailability(product.getQuantidadeEstoque(), requested))
                .orElse(OrcamentoLineItem.AvailabilityStatus.NEEDED);
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
                group.getInternalNote(),
                group.getKitOriginId(),
                group.getKitOriginVersion(),
                group.isRecommended(),
                group.getVisibility().name(),
                group.getPosition(),
                money(subtotal),
                lines.stream().map(this::mapLine).toList());
    }

    private OrcamentoCompositionLineResponse mapLine(OrcamentoLineItem item) {
        return new OrcamentoCompositionLineResponse(
                item.getId(), item.getLineType().name(), item.getCatalogItemId(), item.getCatalogVersion(),
                item.getSource().name(), item.getKitOriginId(), item.getKitOriginVersion(),
                item.getDescriptionSnapshot(), item.getReferenceSnapshot(),
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

    private OrcamentoServiceGroup requireGroup(Long tenantId, Long budgetId, Long groupId) {
        return groupRepository.findByIdAndEmpresaIdAndOrcamentoId(groupId, tenantId, budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo nao encontrado no orcamento autenticado."));
    }

    private OrcamentoLineItem requireLine(Long tenantId, Long budgetId, Long groupId, Long itemId) {
        return lineRepository
                .findByIdAndEmpresaIdAndGroupIdAndGroupOrcamentoId(itemId, tenantId, groupId, budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Item nao encontrado no grupo autenticado."));
    }

    private OrcamentoCompositionResponse finishMutation(Long tenantId, OrdemServico budget) {
        advanceRevision(budget);
        recalculateBudget(tenantId, budget);
        budgetRepository.saveAndFlush(budget);
        return buildResponse(tenantId, budget);
    }

    private void normalizeGroupPositions(Long tenantId, Long budgetId) {
        persistGroupOrder(groupRepository.findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(tenantId, budgetId));
    }

    private void normalizeLinePositions(Long tenantId, Long groupId) {
        persistLineOrder(lineRepository.findByEmpresaIdAndGroupIdOrderByPositionAsc(tenantId, groupId));
    }

    private void persistGroupOrder(List<OrcamentoServiceGroup> groups) {
        for (int index = 0; index < groups.size(); index++) groups.get(index).setPosition(1_000_000 + index);
        groupRepository.saveAllAndFlush(groups);
        for (int index = 0; index < groups.size(); index++) groups.get(index).setPosition(index);
        groupRepository.saveAll(groups);
    }

    private void persistLineOrder(List<OrcamentoLineItem> lines) {
        for (int index = 0; index < lines.size(); index++) lines.get(index).setPosition(1_000_000 + index);
        lineRepository.saveAllAndFlush(lines);
        for (int index = 0; index < lines.size(); index++) lines.get(index).setPosition(index);
        lineRepository.saveAll(lines);
    }

    private void validateExactOrder(List<Long> requested, List<Long> current, String resourceLabel) {
        Set<Long> unique = new HashSet<>(requested);
        if (requested.size() != current.size() || unique.size() != requested.size() || !unique.equals(new HashSet<>(current))) {
            throw new BusinessException("A ordem de " + resourceLabel + " nao corresponde a composicao atual.");
        }
    }

    private OrcamentoLineItem copyLine(
            Long tenantId,
            OrcamentoServiceGroup group,
            OrcamentoLineItem source,
            int position) {
        OrcamentoLineItem copy = new OrcamentoLineItem();
        copy.setEmpresaId(tenantId);
        copy.setGroup(group);
        copy.setLineType(source.getLineType());
        copy.setCatalogItemId(source.getCatalogItemId());
        copy.setCatalogVersion(source.getCatalogVersion());
        copy.setSource(source.getSource());
        copy.setKitOriginId(source.getKitOriginId());
        copy.setKitOriginVersion(source.getKitOriginVersion());
        copy.setDescriptionSnapshot(source.getDescriptionSnapshot());
        copy.setReferenceSnapshot(source.getReferenceSnapshot());
        copy.setQuantity(source.getQuantity());
        copy.setUnitPrice(source.getUnitPrice());
        copy.setDiscountAmount(source.getDiscountAmount());
        copy.setTotalAmount(source.getTotalAmount());
        copy.setAvailabilityStatus(source.getAvailabilityStatus());
        copy.setPosition(position);
        return copy;
    }

    private String copyTitle(String source) {
        String suffix = " (copia)";
        String base = source.length() + suffix.length() <= 120
                ? source
                : source.substring(0, 120 - suffix.length()).trim();
        return base + suffix;
    }

    private String normalizeIdempotencyKey(String value) {
        String key = value == null ? "" : value.trim();
        if (key.isEmpty() || key.length() > 200) {
            throw new BusinessException("Idempotency-Key obrigatoria e limitada a 200 caracteres.");
        }
        return key;
    }

    private String fingerprintKitRequest(Long kitId, OrcamentoInstantiateKitRequest request) {
        String payload = kitId + "|" + request.expectedRevision() + "|"
                + request.quantity().stripTrailingZeros().toPlainString() + "|" + request.targetPosition();
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 indisponivel para idempotencia.", exception);
        }
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
