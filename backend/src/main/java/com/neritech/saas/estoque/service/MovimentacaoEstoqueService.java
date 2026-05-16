package com.neritech.saas.estoque.service;

import com.neritech.saas.estoque.domain.LocalizacaoEstoque;
import com.neritech.saas.estoque.domain.MovimentacaoEstoque;
import com.neritech.saas.estoque.domain.enums.TipoMovimentacao;
import com.neritech.saas.estoque.dto.MovimentacaoEstoqueRequest;
import com.neritech.saas.estoque.dto.MovimentacaoEstoqueResponse;
import com.neritech.saas.estoque.mapper.MovimentacaoEstoqueMapper;
import com.neritech.saas.estoque.repository.LocalizacaoEstoqueRepository;
import com.neritech.saas.estoque.repository.MovimentacaoEstoqueRepository;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.repository.FornecedorRepository;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository repository;
    private final ProdutoRepository produtoRepository;
    private final LocalizacaoEstoqueRepository localizacaoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final com.neritech.saas.estoque.repository.EstoqueRepository estoqueRepository;
    private final com.neritech.saas.financeiro.service.ContasPagarService contasPagarService;
    private final MovimentacaoEstoqueMapper mapper;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository repository,
            ProdutoRepository produtoRepository,
            LocalizacaoEstoqueRepository localizacaoRepository,
            FornecedorRepository fornecedorRepository,
            com.neritech.saas.estoque.repository.EstoqueRepository estoqueRepository,
            com.neritech.saas.financeiro.service.ContasPagarService contasPagarService,
            MovimentacaoEstoqueMapper mapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.localizacaoRepository = localizacaoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.estoqueRepository = estoqueRepository;
        this.contasPagarService = contasPagarService;
        this.mapper = mapper;
    }

    @Transactional
    public MovimentacaoEstoqueResponse create(MovimentacaoEstoqueRequest request) {
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado"));

        MovimentacaoEstoque entity = mapper.toEntity(request);
        entity.setProduto(produto);
        entity.setDataMovimentacao(LocalDateTime.now());

        if (request.localizacaoOrigemId() != null) {
            LocalizacaoEstoque origem = localizacaoRepository.findById(request.localizacaoOrigemId())
                    .orElseThrow(() -> new EntityNotFoundException("LocalizaÃ§Ã£o de origem nÃ£o encontrada"));
            entity.setLocalizacaoOrigem(origem);
        }

        if (request.localizacaoDestinoId() != null) {
            LocalizacaoEstoque destino = localizacaoRepository.findById(request.localizacaoDestinoId())
                    .orElseThrow(() -> new EntityNotFoundException("LocalizaÃ§Ã£o de destino nÃ£o encontrada"));
            entity.setLocalizacaoDestino(destino);
        }

        if (request.fornecedorId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                    .orElseThrow(() -> new EntityNotFoundException("Fornecedor nÃ£o encontrado"));
            entity.setFornecedor(fornecedor);
        }

        if (request.valorUnitario() != null && request.quantidade() != null) {
            entity.setValorTotal(request.valorUnitario().multiply(request.quantidade()));
        }

        MovimentacaoEstoque saved = repository.save(entity);

        // Atualizar o saldo no estoque (tabela estoques)
        atualizarSaldoEstoque(request.empresaId(), request.produtoId(), request.tipoMovimentacao(), request.quantidade());

        // Se for Entrada e tiver fornecedor, gerar Contas a Pagar
        if (request.tipoMovimentacao() == TipoMovimentacao.ENTRADA && request.fornecedorId() != null) {
            gerarContasPagar(request);
        }

        return mapper.toResponse(saved);
    }

    private void gerarContasPagar(MovimentacaoEstoqueRequest request) {
        contasPagarService.create(request.empresaId(), new com.neritech.saas.financeiro.dto.ContasPagarRequest(
                "Compra de Produto - Mov. Estoque",
                request.fornecedorId(),
                request.documentoNumero() != null ? request.documentoNumero() : "MOV-" + System.currentTimeMillis(), // numeroDocumento
                java.time.LocalDate.now(), // dataEmissao
                java.time.LocalDate.now().plusDays(30), // dataVencimento
                null, // dataPagamento
                null, // dataAgendamento
                request.valorUnitario().multiply(request.quantidade()), // valorOriginal
                java.math.BigDecimal.ZERO, // valorPago
                java.math.BigDecimal.ZERO, // valorJuros
                java.math.BigDecimal.ZERO, // valorMulta
                java.math.BigDecimal.ZERO, // valorDesconto
                request.valorUnitario().multiply(request.quantidade()), // saldoDevedor
                com.neritech.saas.financeiro.domain.enums.StatusTitulo.ABERTO,
                com.neritech.saas.financeiro.domain.enums.TipoTitulo.DUPLICATA,
                null, // formaPagamentoId
                null, // contaBancariaId
                null, // centroCustoId
                null, // planoContasId
                request.documentoNumero() != null ? request.documentoNumero() : "MOV-" + System.currentTimeMillis(), // numeroTitulo
                null, // codigoBarras
                "Gerado automaticamente pela entrada de estoque do produto ID: " + request.produtoId() // observacoes
        ));
    }

    private void atualizarSaldoEstoque(Long empresaId, Long produtoId, TipoMovimentacao tipo, java.math.BigDecimal qtd) {
        com.neritech.saas.estoque.domain.Estoque estoque = estoqueRepository.findFirstByEmpresaIdAndProdutoId(empresaId, produtoId)
                .orElseGet(() -> {
                    com.neritech.saas.estoque.domain.Estoque novo = new com.neritech.saas.estoque.domain.Estoque();
                    novo.setEmpresaId(empresaId);
                    novo.setProduto(produtoRepository.getReferenceById(produtoId));
                    novo.setQuantidadeAtual(java.math.BigDecimal.ZERO);
                    return novo;
                });

        if (tipo == TipoMovimentacao.ENTRADA) {
            estoque.setQuantidadeAtual(estoque.getQuantidadeAtual().add(qtd));
        } else if (tipo == TipoMovimentacao.SAIDA) {
            estoque.setQuantidadeAtual(estoque.getQuantidadeAtual().subtract(qtd));
        }

        estoqueRepository.save(estoque);
    }

    @Transactional(readOnly = true)
    public MovimentacaoEstoqueResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("MovimentaÃ§Ã£o nÃ£o encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponse> findByEmpresa(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponse> findByEmpresaAndProduto(Long empresaId, Long produtoId,
            Pageable pageable) {
        return repository.findByEmpresaIdAndProdutoId(empresaId, produtoId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponse> findByEmpresaAndTipo(Long empresaId, TipoMovimentacao tipo,
            Pageable pageable) {
        return repository.findByEmpresaIdAndTipoMovimentacao(empresaId, tipo, pageable)
                .map(mapper::toResponse);
    }
}
