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
import com.neritech.saas.orcamento.dto.OrcamentoCompositionResponse;
import com.neritech.saas.orcamento.dto.OrcamentoReorderRequest;
import com.neritech.saas.orcamento.dto.OrcamentoRevisionRequest;
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
import com.neritech.saas.produtoServico.repository.ProdutoRepository;
import com.neritech.saas.produtoServico.repository.ServicoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrcamentoCompositionServiceTest {

    private static final Long TENANT_ID = 41L;

    @Mock private OrdemServicoRepository budgetRepository;
    @Mock private OrcamentoServiceGroupRepository groupRepository;
    @Mock private OrcamentoLineItemRepository lineRepository;
    @Mock private ProdutoRepository productRepository;
    @Mock private ServicoRepository serviceRepository;
    @Mock private CatalogKitRepository kitRepository;
    @Mock private CatalogKitVersionRepository kitVersionRepository;
    @Mock private CatalogKitVersionItemRepository kitItemRepository;
    @Mock private OrcamentoKitInstantiationRepository kitInstantiationRepository;

    private OrcamentoCompositionService service;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TENANT_ID);
        service = new OrcamentoCompositionService(
                budgetRepository, groupRepository, lineRepository, productRepository, serviceRepository,
                kitRepository, kitVersionRepository, kitItemRepository, kitInstantiationRepository);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void rejectsBudgetOutsideAuthenticatedTenantWithoutReadingComposition() {
        when(budgetRepository.findByIdAndEmpresaId(19L, TENANT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(19L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("contexto autenticado");

        verify(groupRepository, never()).findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(any(), any());
        verify(lineRepository, never()).findCompositionLines(any(), any());
    }

    @Test
    void rejectsStaleRevisionBeforeCatalogOrPersistenceAccess() {
        OrdemServico budget = budget(10L, 4L);
        when(budgetRepository.findBudgetForCompositionUpdate(10L, TENANT_ID, TipoOS.ORCAMENTO))
                .thenReturn(Optional.of(budget));

        OrcamentoAddCatalogItemRequest request =
                new OrcamentoAddCatalogItemRequest(3L, "PART", 88L, BigDecimal.ONE);

        assertThatThrownBy(() -> service.addCatalogItem(10L, 20L, request))
                .isInstanceOf(OptimisticLockingFailureException.class)
                .hasMessageContaining("recarregue");

        verify(groupRepository, never()).findByIdAndEmpresaIdAndOrcamentoId(any(), any(), any());
        verify(productRepository, never()).findByIdAndEmpresaId(any(), any());
    }

    @Test
    void searchesActiveTenantCatalogWithoutReturningProductCost() {
        Produto product = new Produto();
        product.setId(88L);
        product.setNome("Filtro de oleo premium");
        product.setCodigoInterno("FLT-001");
        product.setPrecoVenda(new BigDecimal("42.90"));
        product.setPrecoCusto(new BigDecimal("9.99"));
        product.setQuantidadeEstoque(new BigDecimal("3"));
        when(productRepository.searchActive(eq(TENANT_ID), eq("filtro"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(product)));
        when(serviceRepository.searchActive(eq(TENANT_ID), eq("filtro"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));
        when(kitRepository.searchActive(eq(TENANT_ID), eq("filtro"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        var response = service.searchCatalog(" filtro ");

        assertThat(response.query()).isEqualTo("filtro");
        assertThat(response.items()).hasSize(1);
        assertThat(response.items().getFirst().suggestedPrice()).isEqualByComparingTo("42.90");
        assertThat(response.items().getFirst().availabilityStatus()).isEqualTo("AVAILABLE");
        assertThat(response.items().getFirst().toString()).doesNotContain("9.99");
    }

    @Test
    void snapshotsCanonicalProductPriceAndKeepsNeededAvailabilityInformative() {
        OrdemServico budget = budget(10L, 0L);
        OrcamentoServiceGroup group = group(20L, budget, false);
        Produto product = new Produto();
        product.setId(88L);
        product.setNome("Filtro de oleo premium");
        product.setCodigoInterno("FLT-001");
        product.setPrecoVenda(new BigDecimal("12.3400"));
        product.setQuantidadeEstoque(BigDecimal.ZERO);
        product.setAtivo(true);
        product.setVersao(7);
        AtomicReference<OrcamentoLineItem> savedLine = new AtomicReference<>();

        when(budgetRepository.findBudgetForCompositionUpdate(10L, TENANT_ID, TipoOS.ORCAMENTO))
                .thenReturn(Optional.of(budget));
        when(groupRepository.findByIdAndEmpresaIdAndOrcamentoId(20L, TENANT_ID, 10L))
                .thenReturn(Optional.of(group));
        when(productRepository.findByIdAndEmpresaId(88L, TENANT_ID)).thenReturn(Optional.of(product));
        when(lineRepository.countByEmpresaIdAndGroupId(TENANT_ID, 20L)).thenReturn(0L);
        when(lineRepository.save(any())).thenAnswer(invocation -> {
            OrcamentoLineItem item = invocation.getArgument(0);
            item.setId(31L);
            savedLine.set(item);
            return item;
        });
        when(groupRepository.findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(TENANT_ID, 10L))
                .thenReturn(List.of(group));
        when(lineRepository.findCompositionLines(TENANT_ID, 10L))
                .thenAnswer(ignored -> savedLine.get() == null ? List.of() : List.of(savedLine.get()));

        OrcamentoCompositionResponse response = service.addCatalogItem(
                10L,
                20L,
                new OrcamentoAddCatalogItemRequest(0L, "PART", 88L, new BigDecimal("2.000")));

        assertThat(response.revision()).isEqualTo(1L);
        assertThat(response.requiredTotal()).isEqualByComparingTo("24.68");
        assertThat(response.recommendedTotal()).isEqualByComparingTo("0.00");
        assertThat(response.partsTotal()).isEqualByComparingTo("24.68");
        assertThat(response.groups().getFirst().lines().getFirst().unitPrice()).isEqualByComparingTo("12.3400");
        assertThat(response.groups().getFirst().lines().getFirst().catalogVersion()).isEqualTo(7);
        assertThat(response.groups().getFirst().lines().getFirst().availabilityStatus()).isEqualTo("NEEDED");
        assertThat(budget.getValorTotal()).isEqualByComparingTo("24.68");
        verify(budgetRepository).saveAndFlush(budget);
    }

    @Test
    void separatesRecommendedGroupFromRequiredTotal() {
        OrdemServico budget = budget(10L, 0L);
        OrcamentoServiceGroup group = group(20L, budget, true);
        Produto product = new Produto();
        product.setId(88L);
        product.setNome("Higienizacao recomendada");
        product.setCodigoInterno("REC-001");
        product.setPrecoVenda(new BigDecimal("50.00"));
        product.setQuantidadeEstoque(new BigDecimal("5"));
        product.setAtivo(true);
        AtomicReference<OrcamentoLineItem> savedLine = new AtomicReference<>();

        when(budgetRepository.findBudgetForCompositionUpdate(10L, TENANT_ID, TipoOS.ORCAMENTO))
                .thenReturn(Optional.of(budget));
        when(groupRepository.findByIdAndEmpresaIdAndOrcamentoId(20L, TENANT_ID, 10L))
                .thenReturn(Optional.of(group));
        when(productRepository.findByIdAndEmpresaId(88L, TENANT_ID)).thenReturn(Optional.of(product));
        when(lineRepository.countByEmpresaIdAndGroupId(TENANT_ID, 20L)).thenReturn(0L);
        when(lineRepository.save(any())).thenAnswer(invocation -> {
            OrcamentoLineItem item = invocation.getArgument(0);
            item.setId(32L);
            savedLine.set(item);
            return item;
        });
        when(groupRepository.findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(TENANT_ID, 10L))
                .thenReturn(List.of(group));
        when(lineRepository.findCompositionLines(TENANT_ID, 10L))
                .thenAnswer(ignored -> savedLine.get() == null ? List.of() : List.of(savedLine.get()));

        OrcamentoCompositionResponse response = service.addCatalogItem(
                10L, 20L, new OrcamentoAddCatalogItemRequest(0L, "PART", 88L, BigDecimal.ONE));

        assertThat(response.requiredTotal()).isEqualByComparingTo("0.00");
        assertThat(response.recommendedTotal()).isEqualByComparingTo("50.00");
        assertThat(budget.getValorTotal()).isEqualByComparingTo("0.00");
    }

    @Test
    void reordersEveryGroupWithExactIdsAndOneRevisionAdvance() {
        OrdemServico budget = budget(10L, 4L);
        OrcamentoServiceGroup first = group(20L, budget, false);
        OrcamentoServiceGroup second = group(21L, budget, false);
        second.setTitle("Suspensao");
        second.setPosition(1);
        when(budgetRepository.findBudgetForCompositionUpdate(10L, TENANT_ID, TipoOS.ORCAMENTO))
                .thenReturn(Optional.of(budget));
        when(groupRepository.findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(TENANT_ID, 10L))
                .thenReturn(List.of(first, second));
        when(lineRepository.findCompositionLines(TENANT_ID, 10L)).thenReturn(List.of());

        OrcamentoCompositionResponse response = service.reorderGroups(
                10L, new OrcamentoReorderRequest(4L, List.of(21L, 20L)));

        assertThat(response.revision()).isEqualTo(5L);
        assertThat(second.getPosition()).isZero();
        assertThat(first.getPosition()).isEqualTo(1);
        verify(groupRepository).saveAllAndFlush(List.of(second, first));
        verify(budgetRepository).saveAndFlush(budget);
    }

    @Test
    void rejectsPartialReorderBeforeChangingPositions() {
        OrdemServico budget = budget(10L, 4L);
        OrcamentoServiceGroup first = group(20L, budget, false);
        OrcamentoServiceGroup second = group(21L, budget, false);
        second.setPosition(1);
        when(budgetRepository.findBudgetForCompositionUpdate(10L, TENANT_ID, TipoOS.ORCAMENTO))
                .thenReturn(Optional.of(budget));
        when(groupRepository.findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(TENANT_ID, 10L))
                .thenReturn(List.of(first, second));

        assertThatThrownBy(() -> service.reorderGroups(
                10L, new OrcamentoReorderRequest(4L, List.of(20L))))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("nao corresponde");

        verify(groupRepository, never()).saveAllAndFlush(any());
        verify(budgetRepository, never()).saveAndFlush(any());
    }

    @Test
    void updatesQuantityWithSnapshotPriceAndRefreshesInformativeAvailability() {
        OrdemServico budget = budget(10L, 2L);
        OrcamentoServiceGroup group = group(20L, budget, false);
        OrcamentoLineItem line = line(31L, group, new BigDecimal("40.0000"), BigDecimal.ONE);
        Produto product = new Produto();
        product.setId(88L);
        product.setQuantidadeEstoque(new BigDecimal("2.000"));
        when(budgetRepository.findBudgetForCompositionUpdate(10L, TENANT_ID, TipoOS.ORCAMENTO))
                .thenReturn(Optional.of(budget));
        when(lineRepository.findByIdAndEmpresaIdAndGroupIdAndGroupOrcamentoId(31L, TENANT_ID, 20L, 10L))
                .thenReturn(Optional.of(line));
        when(productRepository.findByIdAndEmpresaId(88L, TENANT_ID)).thenReturn(Optional.of(product));
        when(groupRepository.findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(TENANT_ID, 10L))
                .thenReturn(List.of(group));
        when(lineRepository.findCompositionLines(TENANT_ID, 10L)).thenReturn(List.of(line));

        OrcamentoCompositionResponse response = service.updateLine(
                10L, 20L, 31L, new OrcamentoUpdateLineRequest(2L, new BigDecimal("3.000")));

        assertThat(response.revision()).isEqualTo(3L);
        assertThat(response.requiredTotal()).isEqualByComparingTo("120.00");
        assertThat(line.getUnitPrice()).isEqualByComparingTo("40.0000");
        assertThat(line.getAvailabilityStatus()).isEqualTo(OrcamentoLineItem.AvailabilityStatus.PARTIAL);
        assertThat(budget.getValorTotal()).isEqualByComparingTo("120.00");
    }

    @Test
    void duplicatesGroupFromPersistedSnapshotsWithoutReadingCatalogAgain() {
        OrdemServico budget = budget(10L, 7L);
        OrcamentoServiceGroup source = group(20L, budget, false);
        OrcamentoLineItem sourceLine = line(31L, source, new BigDecimal("25.0000"), new BigDecimal("2.000"));
        AtomicReference<OrcamentoServiceGroup> duplicateGroup = new AtomicReference<>();
        AtomicReference<OrcamentoLineItem> duplicateLine = new AtomicReference<>();
        when(budgetRepository.findBudgetForCompositionUpdate(10L, TENANT_ID, TipoOS.ORCAMENTO))
                .thenReturn(Optional.of(budget));
        when(groupRepository.findByIdAndEmpresaIdAndOrcamentoId(20L, TENANT_ID, 10L))
                .thenReturn(Optional.of(source));
        when(groupRepository.countByEmpresaIdAndOrcamentoId(TENANT_ID, 10L)).thenReturn(1L);
        when(groupRepository.saveAndFlush(any())).thenAnswer(invocation -> {
            OrcamentoServiceGroup copy = invocation.getArgument(0);
            copy.setId(21L);
            duplicateGroup.set(copy);
            return copy;
        });
        when(lineRepository.findByEmpresaIdAndGroupIdOrderByPositionAsc(TENANT_ID, 20L))
                .thenReturn(List.of(sourceLine));
        when(lineRepository.saveAll(any())).thenAnswer(invocation -> {
            List<OrcamentoLineItem> copies = invocation.getArgument(0);
            copies.getFirst().setId(32L);
            duplicateLine.set(copies.getFirst());
            return copies;
        });
        when(groupRepository.findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(TENANT_ID, 10L))
                .thenAnswer(ignored -> List.of(source, duplicateGroup.get()));
        when(lineRepository.findCompositionLines(TENANT_ID, 10L))
                .thenAnswer(ignored -> List.of(sourceLine, duplicateLine.get()));

        OrcamentoCompositionResponse response =
                service.duplicateGroup(10L, 20L, new OrcamentoRevisionRequest(7L));

        assertThat(response.revision()).isEqualTo(8L);
        assertThat(response.groups()).hasSize(2);
        assertThat(response.requiredTotal()).isEqualByComparingTo("100.00");
        assertThat(duplicateGroup.get().getTitle()).endsWith("(copia)");
        assertThat(duplicateLine.get().getUnitPrice()).isEqualByComparingTo("25.0000");
        assertThat(duplicateLine.get().getCatalogVersion()).isEqualTo(sourceLine.getCatalogVersion());
        verify(productRepository, never()).findByIdAndEmpresaId(any(), any());
        verify(serviceRepository, never()).findByIdAndEmpresaId(any(), any());
    }

    @Test
    void searchesPublishedVersionedKitWithSnapshotPriceAndLiveAvailabilityOnly() {
        CatalogKit kit = kit(44L, 3);
        CatalogKitVersion version = kitVersion(55L, kit, 3);
        CatalogKitVersionItem part = kitItem(
                61L, version, CatalogKitVersionItem.LineType.PART, 88L,
                new BigDecimal("2.000"), new BigDecimal("50.0000"), 0);
        CatalogKitVersionItem labor = kitItem(
                62L, version, CatalogKitVersionItem.LineType.LABOR, 99L,
                BigDecimal.ONE, new BigDecimal("100.0000"), 1);
        Produto product = new Produto();
        product.setId(88L);
        product.setQuantidadeEstoque(BigDecimal.ONE);
        when(kitRepository.searchActive(eq(TENANT_ID), eq("revisao"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(kit)));
        when(kitVersionRepository.findByEmpresaIdAndKitIdAndVersionNumberAndPublishedTrue(
                TENANT_ID, 44L, 3)).thenReturn(Optional.of(version));
        when(kitItemRepository.findByEmpresaIdAndKitVersionIdOrderByPositionAsc(TENANT_ID, 55L))
                .thenReturn(List.of(part, labor));
        when(productRepository.findByIdAndEmpresaId(88L, TENANT_ID)).thenReturn(Optional.of(product));

        var response = service.searchCatalog("revisao", "KIT");

        assertThat(response.items()).hasSize(1);
        assertThat(response.items().getFirst().lineType()).isEqualTo("KIT");
        assertThat(response.items().getFirst().suggestedPrice()).isEqualByComparingTo("200.00");
        assertThat(response.items().getFirst().availabilityStatus()).isEqualTo("PARTIAL");
        assertThat(response.items().getFirst().itemCount()).isEqualTo(2);
        assertThat(response.items().getFirst().catalogVersion()).isEqualTo(3);
        verify(productRepository, never()).searchActive(any(), any(), any());
        verify(serviceRepository, never()).searchActive(any(), any(), any());
    }

    @Test
    void instantiatesKitAtomicallyFromVersionSnapshotsWithoutMutatingMasterCatalog() {
        OrdemServico budget = budget(10L, 0L);
        CatalogKit kit = kit(44L, 3);
        CatalogKitVersion version = kitVersion(55L, kit, 3);
        CatalogKitVersionItem part = kitItem(
                61L, version, CatalogKitVersionItem.LineType.PART, 88L,
                new BigDecimal("2.000"), new BigDecimal("50.0000"), 0);
        CatalogKitVersionItem labor = kitItem(
                62L, version, CatalogKitVersionItem.LineType.LABOR, 99L,
                BigDecimal.ONE, new BigDecimal("100.0000"), 1);
        Produto product = new Produto();
        product.setId(88L);
        product.setQuantidadeEstoque(new BigDecimal("2.000"));
        AtomicReference<OrcamentoServiceGroup> groupRef = new AtomicReference<>();
        AtomicReference<List<OrcamentoLineItem>> linesRef = new AtomicReference<>(List.of());
        AtomicReference<OrcamentoKitInstantiation> instantiationRef = new AtomicReference<>();

        when(budgetRepository.findBudgetForCompositionUpdate(10L, TENANT_ID, TipoOS.ORCAMENTO))
                .thenReturn(Optional.of(budget));
        when(kitInstantiationRepository.findByEmpresaIdAndOrcamentoIdAndIdempotencyKey(
                TENANT_ID, 10L, "kit-request-1")).thenReturn(Optional.empty());
        when(kitRepository.findByIdAndEmpresaIdAndActiveTrue(44L, TENANT_ID)).thenReturn(Optional.of(kit));
        when(kitVersionRepository.findByEmpresaIdAndKitIdAndVersionNumberAndPublishedTrue(
                TENANT_ID, 44L, 3)).thenReturn(Optional.of(version));
        when(kitItemRepository.findByEmpresaIdAndKitVersionIdOrderByPositionAsc(TENANT_ID, 55L))
                .thenReturn(List.of(part, labor));
        when(groupRepository.findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(TENANT_ID, 10L))
                .thenAnswer(ignored -> groupRef.get() == null ? List.of() : List.of(groupRef.get()));
        when(groupRepository.saveAndFlush(any())).thenAnswer(invocation -> {
            OrcamentoServiceGroup group = invocation.getArgument(0);
            group.setId(20L);
            groupRef.set(group);
            return group;
        });
        when(productRepository.findByIdAndEmpresaId(88L, TENANT_ID)).thenReturn(Optional.of(product));
        when(lineRepository.saveAll(any())).thenAnswer(invocation -> {
            List<OrcamentoLineItem> lines = invocation.getArgument(0);
            for (int index = 0; index < lines.size(); index++) lines.get(index).setId(31L + index);
            linesRef.set(lines);
            return lines;
        });
        when(lineRepository.findCompositionLines(TENANT_ID, 10L)).thenAnswer(ignored -> linesRef.get());
        when(kitInstantiationRepository.save(any())).thenAnswer(invocation -> {
            OrcamentoKitInstantiation value = invocation.getArgument(0);
            value.setId(70L);
            instantiationRef.set(value);
            return value;
        });

        OrcamentoCompositionResponse response = service.instantiateKit(
                10L, 44L, "kit-request-1",
                new OrcamentoInstantiateKitRequest(0L, new BigDecimal("1.500"), 0));

        assertThat(response.revision()).isEqualTo(1L);
        assertThat(response.requiredTotal()).isEqualByComparingTo("300.00");
        assertThat(response.groups().getFirst().kitOriginId()).isEqualTo(44L);
        assertThat(response.groups().getFirst().kitOriginVersion()).isEqualTo(3);
        assertThat(linesRef.get()).extracting(OrcamentoLineItem::getSource)
                .containsOnly(OrcamentoLineItem.Source.KIT);
        assertThat(linesRef.get().getFirst().getQuantity()).isEqualByComparingTo("3.000");
        assertThat(linesRef.get().getFirst().getUnitPrice()).isEqualByComparingTo("50.0000");
        assertThat(linesRef.get().getFirst().getAvailabilityStatus())
                .isEqualTo(OrcamentoLineItem.AvailabilityStatus.PARTIAL);
        assertThat(instantiationRef.get().getRequestFingerprint()).hasSize(64);
        verify(kitRepository, never()).save(any());
        verify(kitVersionRepository, never()).save(any());
        verify(kitItemRepository, never()).save(any());
    }

    @Test
    void retryWithSameIdempotencyKeyReturnsCompositionWithoutDuplicatingKit() {
        OrdemServico budget = budget(10L, 0L);
        CatalogKit kit = kit(44L, 3);
        CatalogKitVersion version = kitVersion(55L, kit, 3);
        CatalogKitVersionItem labor = kitItem(
                62L, version, CatalogKitVersionItem.LineType.LABOR, 99L,
                BigDecimal.ONE, new BigDecimal("100.0000"), 0);
        AtomicReference<OrcamentoServiceGroup> groupRef = new AtomicReference<>();
        AtomicReference<List<OrcamentoLineItem>> linesRef = new AtomicReference<>(List.of());
        AtomicReference<OrcamentoKitInstantiation> instantiationRef = new AtomicReference<>();
        AtomicInteger groupWrites = new AtomicInteger();

        when(budgetRepository.findBudgetForCompositionUpdate(10L, TENANT_ID, TipoOS.ORCAMENTO))
                .thenReturn(Optional.of(budget));
        when(kitInstantiationRepository.findByEmpresaIdAndOrcamentoIdAndIdempotencyKey(
                TENANT_ID, 10L, "same-key")).thenAnswer(ignored -> Optional.ofNullable(instantiationRef.get()));
        when(kitRepository.findByIdAndEmpresaIdAndActiveTrue(44L, TENANT_ID)).thenReturn(Optional.of(kit));
        when(kitVersionRepository.findByEmpresaIdAndKitIdAndVersionNumberAndPublishedTrue(
                TENANT_ID, 44L, 3)).thenReturn(Optional.of(version));
        when(kitItemRepository.findByEmpresaIdAndKitVersionIdOrderByPositionAsc(TENANT_ID, 55L))
                .thenReturn(List.of(labor));
        when(groupRepository.findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(TENANT_ID, 10L))
                .thenAnswer(ignored -> groupRef.get() == null ? List.of() : List.of(groupRef.get()));
        when(groupRepository.saveAndFlush(any())).thenAnswer(invocation -> {
            groupWrites.incrementAndGet();
            OrcamentoServiceGroup group = invocation.getArgument(0);
            group.setId(20L);
            groupRef.set(group);
            return group;
        });
        when(lineRepository.saveAll(any())).thenAnswer(invocation -> {
            List<OrcamentoLineItem> lines = invocation.getArgument(0);
            lines.getFirst().setId(31L);
            linesRef.set(lines);
            return lines;
        });
        when(lineRepository.findCompositionLines(TENANT_ID, 10L)).thenAnswer(ignored -> linesRef.get());
        when(kitInstantiationRepository.save(any())).thenAnswer(invocation -> {
            OrcamentoKitInstantiation value = invocation.getArgument(0);
            value.setId(70L);
            instantiationRef.set(value);
            return value;
        });
        OrcamentoInstantiateKitRequest request =
                new OrcamentoInstantiateKitRequest(0L, BigDecimal.ONE, 0);

        OrcamentoCompositionResponse first = service.instantiateKit(10L, 44L, "same-key", request);
        OrcamentoCompositionResponse retry = service.instantiateKit(10L, 44L, "same-key", request);

        assertThat(first.revision()).isEqualTo(1L);
        assertThat(retry.revision()).isEqualTo(1L);
        assertThat(retry.groupCount()).isEqualTo(1);
        assertThat(groupWrites).hasValue(1);
        verify(kitInstantiationRepository).save(any());
    }

    private OrdemServico budget(Long id, Long revision) {
        OrdemServico budget = new OrdemServico();
        budget.setId(id);
        budget.setEmpresaId(TENANT_ID);
        budget.setTipoOS(TipoOS.ORCAMENTO);
        budget.setCompositionRevision(revision);
        budget.setValorProdutos(BigDecimal.ZERO);
        budget.setValorServicos(BigDecimal.ZERO);
        budget.setValorTotal(BigDecimal.ZERO);
        return budget;
    }

    private OrcamentoServiceGroup group(Long id, OrdemServico budget, boolean recommended) {
        OrcamentoServiceGroup group = new OrcamentoServiceGroup();
        group.setId(id);
        group.setEmpresaId(TENANT_ID);
        group.setOrcamento(budget);
        group.setTitle("Troca de oleo");
        group.setRecommended(recommended);
        group.setVisibility(OrcamentoServiceGroup.Visibility.CUSTOMER_VISIBLE);
        group.setPosition(0);
        return group;
    }

    private CatalogKit kit(Long id, int currentVersion) {
        CatalogKit kit = new CatalogKit();
        kit.setId(id);
        kit.setEmpresaId(TENANT_ID);
        kit.setName("Revisao completa");
        kit.setReference("KIT-REV");
        kit.setActive(true);
        kit.setCurrentVersion(currentVersion);
        return kit;
    }

    private CatalogKitVersion kitVersion(Long id, CatalogKit kit, int versionNumber) {
        CatalogKitVersion version = new CatalogKitVersion();
        version.setId(id);
        version.setEmpresaId(TENANT_ID);
        version.setKit(kit);
        version.setVersionNumber(versionNumber);
        version.setTitleSnapshot("Revisao completa");
        version.setDescriptionSnapshot("Pacote versionado de manutencao preventiva");
        version.setRecommendedDefault(false);
        version.setPublished(true);
        return version;
    }

    private CatalogKitVersionItem kitItem(
            Long id,
            CatalogKitVersion version,
            CatalogKitVersionItem.LineType lineType,
            Long catalogItemId,
            BigDecimal quantity,
            BigDecimal unitPrice,
            int position) {
        CatalogKitVersionItem item = new CatalogKitVersionItem();
        item.setId(id);
        item.setEmpresaId(TENANT_ID);
        item.setKitVersion(version);
        item.setLineType(lineType);
        item.setCatalogItemId(catalogItemId);
        item.setCatalogVersion(4);
        item.setDescriptionSnapshot(lineType == CatalogKitVersionItem.LineType.PART
                ? "Filtro premium"
                : "Mao de obra preventiva");
        item.setReferenceSnapshot(lineType == CatalogKitVersionItem.LineType.PART ? "FLT-88" : null);
        item.setQuantity(quantity);
        item.setUnitPriceSnapshot(unitPrice);
        item.setPosition(position);
        return item;
    }

    private OrcamentoLineItem line(
            Long id,
            OrcamentoServiceGroup group,
            BigDecimal unitPrice,
            BigDecimal quantity) {
        OrcamentoLineItem line = new OrcamentoLineItem();
        line.setId(id);
        line.setEmpresaId(TENANT_ID);
        line.setGroup(group);
        line.setLineType(OrcamentoLineItem.LineType.PART);
        line.setCatalogItemId(88L);
        line.setCatalogVersion(3);
        line.setSource(OrcamentoLineItem.Source.PRODUCT_CATALOG);
        line.setDescriptionSnapshot("Pastilha premium");
        line.setReferenceSnapshot("PST-88");
        line.setQuantity(quantity);
        line.setUnitPrice(unitPrice);
        line.setDiscountAmount(BigDecimal.ZERO.setScale(2));
        line.setTotalAmount(quantity.multiply(unitPrice).setScale(2));
        line.setAvailabilityStatus(OrcamentoLineItem.AvailabilityStatus.AVAILABLE);
        line.setPosition(0);
        return line;
    }
}
