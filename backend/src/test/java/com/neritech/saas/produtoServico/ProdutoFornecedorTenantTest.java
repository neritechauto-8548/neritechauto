package com.neritech.saas.produtoServico;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.domain.ProdutoFornecedor;
import com.neritech.saas.produtoServico.dto.ProdutoFornecedorRequest;
import com.neritech.saas.produtoServico.mapper.ProdutoFornecedorMapper;
import com.neritech.saas.produtoServico.repository.FornecedorRepository;
import com.neritech.saas.produtoServico.repository.ProdutoFornecedorRepository;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;
import com.neritech.saas.produtoServico.service.ProdutoFornecedorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoFornecedorTenantTest {
    @Mock ProdutoFornecedorRepository ligacoes;
    @Mock ProdutoRepository produtos;
    @Mock FornecedorRepository fornecedores;
    @Mock ProdutoFornecedorMapper mapper;
    @InjectMocks ProdutoFornecedorService service;

    @AfterEach void resetTenant() { TenantContext.clear(); }

    private ProdutoFornecedorRequest request() {
        ProdutoFornecedorRequest req = mock(ProdutoFornecedorRequest.class);
        when(req.produtoId()).thenReturn(10L);
        lenient().when(req.fornecedorId()).thenReturn(20L);
        return req;
    }

    @Test void impedeCotacaoProdutoFornecedorDeOutroTenant() {
        TenantContext.setCurrentTenant(1L);
        var req = request();
        Produto produto = new Produto();
        produto.setEmpresaId(1L);
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setEmpresaId(2L);
        when(produtos.findById(10L)).thenReturn(Optional.of(produto));
        when(fornecedores.findById(20L)).thenReturn(Optional.of(fornecedor));

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(e -> assertThat(((ResponseStatusException) e).getStatusCode())
                        .isEqualTo(HttpStatus.FORBIDDEN));
        verify(ligacoes, never()).save(any());
    }

    @Test void impedeNovoVinculoSeFornecedorInativo() {
        TenantContext.setCurrentTenant(1L);
        var req = request();
        Produto produto = new Produto();
        produto.setEmpresaId(1L);
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setEmpresaId(1L);
        fornecedor.setAtivo(false);
        when(produtos.findById(10L)).thenReturn(Optional.of(produto));
        when(fornecedores.findById(20L)).thenReturn(Optional.of(fornecedor));

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(e -> assertThat(((ResponseStatusException) e).getStatusCode())
                        .isEqualTo(HttpStatus.CONFLICT));
        verify(ligacoes, never()).save(any());
    }

    @Test void permiteVinculoDeProdutoComFornecedorAtivoDoTenant() {
        TenantContext.setCurrentTenant(1L);
        var req = request();
        Produto produto = new Produto();
        produto.setEmpresaId(1L);
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setEmpresaId(1L);
        fornecedor.setAtivo(true);
        ProdutoFornecedor entity = new ProdutoFornecedor();
        when(produtos.findById(10L)).thenReturn(Optional.of(produto));
        when(fornecedores.findById(20L)).thenReturn(Optional.of(fornecedor));
        when(mapper.toEntity(req)).thenReturn(entity);
        when(ligacoes.save(entity)).thenReturn(entity);

        service.create(req);
        verify(ligacoes).save(entity);
        assertThat(entity.getFornecedor()).isSameAs(fornecedor);
        assertThat(entity.getProduto()).isSameAs(produto);
    }
}
