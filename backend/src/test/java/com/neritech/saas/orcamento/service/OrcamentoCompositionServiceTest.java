package com.neritech.saas.orcamento.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.orcamento.domain.OrcamentoLineItem;
import com.neritech.saas.orcamento.domain.OrcamentoServiceGroup;
import com.neritech.saas.orcamento.dto.OrcamentoAddCatalogItemRequest;
import com.neritech.saas.orcamento.dto.OrcamentoCompositionResponse;
import com.neritech.saas.orcamento.dto.OrcamentoReorderRequest;
import com.neritech.saas.orcamento.dto.OrcamentoRevisionRequest;
import com.neritech.saas.orcamento.dto.OrcamentoUpdateLineRequest;
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

    private OrcamentoCompositionService service;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TENANT_ID);
        service = new OrcamentoCompositionService(
                budgetRepository, groupRepository, lineRepository, productRepository, serviceRepository);
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

