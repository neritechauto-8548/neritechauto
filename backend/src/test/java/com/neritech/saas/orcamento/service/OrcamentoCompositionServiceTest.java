package com.neritech.saas.orcamento.service;

import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.orcamento.domain.OrcamentoLineItem;
import com.neritech.saas.orcamento.domain.OrcamentoServiceGroup;
import com.neritech.saas.orcamento.dto.OrcamentoAddCatalogItemRequest;
import com.neritech.saas.orcamento.dto.OrcamentoCompositionResponse;
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
}
