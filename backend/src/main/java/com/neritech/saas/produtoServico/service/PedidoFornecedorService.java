package com.neritech.saas.produtoServico.service;

import com.neritech.saas.produtoServico.domain.PedidoFornecedor;
import com.neritech.saas.produtoServico.domain.ItemPedidoFornecedor;
import com.neritech.saas.produtoServico.dto.PedidoFornecedorRequest;
import com.neritech.saas.produtoServico.dto.PedidoFornecedorResponse;
import com.neritech.saas.produtoServico.mapper.PedidoFornecedorMapper;
import com.neritech.saas.produtoServico.repository.PedidoFornecedorRepository;
import com.neritech.saas.produtoServico.repository.ItemPedidoFornecedorRepository;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;
import com.neritech.saas.estoque.service.MovimentacaoEstoqueService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoFornecedorService {

    private final PedidoFornecedorRepository repository;
    private final PedidoFornecedorMapper mapper;
    private final com.neritech.saas.estoque.repository.EstoqueRepository estoqueRepository;
    private final ItemPedidoFornecedorRepository itemRepository;
    private final MovimentacaoEstoqueService movimentacaoEstoqueService;
    private final ProdutoRepository produtoRepository;

    public PedidoFornecedorService(PedidoFornecedorRepository repository, 
                                   PedidoFornecedorMapper mapper, 
                                   com.neritech.saas.estoque.repository.EstoqueRepository estoqueRepository,
                                   ItemPedidoFornecedorRepository itemRepository,
                                   MovimentacaoEstoqueService movimentacaoEstoqueService,
                                   ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.estoqueRepository = estoqueRepository;
        this.itemRepository = itemRepository;
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public PedidoFornecedorResponse create(PedidoFornecedorRequest request) {
        PedidoFornecedor entity = mapper.toEntity(request);
        entity.setEmpresaId(request.empresaId());
        entity.setFornecedorId(request.fornecedorId());
        entity.setNumeroPedido(repository.nextNumeroPedido(request.empresaId()));
        
        PedidoFornecedor saved = repository.save(entity);
        
        if (request.itens() != null) {
            for (com.neritech.saas.produtoServico.dto.ItemPedidoFornecedorRequest itemReq : request.itens()) {
                ItemPedidoFornecedor item = mapper.toItemEntity(itemReq);
                item.setPedido(saved);
                item.setEmpresaId(request.empresaId());
                itemRepository.save(item);
            }
        }
        
        return mapper.toResponse(repository.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado após salvar")));
    }

    @Transactional(readOnly = true)
    public PedidoFornecedorResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de fornecedor não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<PedidoFornecedorResponse> findAll(Long empresaId, String termo, Pageable pageable) {
        if (termo != null && !termo.isBlank()) {
            return repository.findByEmpresaIdAndTermo(empresaId, termo, pageable).map(mapper::toResponse);
        }
        return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
    }

    @Transactional
    public PedidoFornecedorResponse update(Long id, PedidoFornecedorRequest request) {
        PedidoFornecedor entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de fornecedor não encontrado com ID: " + id));
        if (entity.getStatus() == com.neritech.saas.produtoServico.domain.enums.StatusPedidoFornecedor.RECEBIDO) {
            throw new IllegalStateException("Não é possível alterar um pedido que já foi recebido.");
        }
        
        mapper.updateEntityFromRequest(request, entity);
        entity.setFornecedorId(request.fornecedorId());
        
        PedidoFornecedor saved = repository.save(entity);

        // Delete old items manually and add new ones
        List<ItemPedidoFornecedor> oldItems = itemRepository.findByPedidoId(saved.getId());
        itemRepository.deleteAllInBatch(oldItems);
        
        if (request.itens() != null) {
            for (com.neritech.saas.produtoServico.dto.ItemPedidoFornecedorRequest itemReq : request.itens()) {
                ItemPedidoFornecedor item = mapper.toItemEntity(itemReq);
                item.setPedido(saved);
                item.setEmpresaId(entity.getEmpresaId());
                itemRepository.save(item);
            }
        }
        
        return mapper.toResponse(repository.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado após atualizar")));
    }

    @Transactional
    public PedidoFornecedorResponse updateStatus(Long id, com.neritech.saas.produtoServico.domain.enums.StatusPedidoFornecedor newStatus) {
        PedidoFornecedor entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de fornecedor não encontrado com ID: " + id));
        
        if (entity.getStatus() != com.neritech.saas.produtoServico.domain.enums.StatusPedidoFornecedor.RECEBIDO &&
            newStatus == com.neritech.saas.produtoServico.domain.enums.StatusPedidoFornecedor.RECEBIDO) {
            
            for (com.neritech.saas.produtoServico.domain.ItemPedidoFornecedor item : entity.getItens()) {
                // 1. Registrar a movimentação de estoque (que atualiza o saldo da tabela estoques)
                movimentacaoEstoqueService.create(new com.neritech.saas.estoque.dto.MovimentacaoEstoqueRequest(
                        entity.getEmpresaId(),
                        item.getProdutoId(),
                        com.neritech.saas.estoque.domain.enums.TipoMovimentacao.ENTRADA,
                        null, // subtipoMovimentacao
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        null, // localizacaoOrigemId
                        null, // localizacaoDestinoId
                        "PEDIDO_FORNECEDOR", // documentoTipo
                        entity.getNumeroPedido() != null ? String.valueOf(entity.getNumeroPedido()) : null, // documentoNumero
                        entity.getId(), // documentoId
                        entity.getFornecedorId(), // fornecedorId
                        null, // loteNumero
                        null, // dataValidade
                        null, // dataFabricacao
                        "Entrada automática por recebimento do Pedido de Fornecedor #" + entity.getNumeroPedido(), // motivo
                        null, // observacoes
                        entity.getCriadoPor() != null ? entity.getCriadoPor() : 1L, // usuarioResponsavel
                        null // ordemServicoId
                ));

                // 2. Atualizar o precoCustoLote na tabela de estoques
                com.neritech.saas.estoque.domain.Estoque estoque = estoqueRepository.findFirstByEmpresaIdAndProdutoId(entity.getEmpresaId(), item.getProdutoId())
                        .orElse(null);
                if (estoque != null && item.getPrecoUnitario() != null) {
                    estoque.setPrecoCustoLote(item.getPrecoUnitario());
                    estoqueRepository.save(estoque);
                }

                // 3. Atualizar precoCusto e precoCompra na tabela de produtos
                com.neritech.saas.produtoServico.domain.Produto produto = produtoRepository.findById(item.getProdutoId())
                        .orElse(null);
                if (produto != null && item.getPrecoUnitario() != null) {
                    produto.setPrecoCusto(item.getPrecoUnitario());
                    produto.setPrecoCompra(item.getPrecoUnitario());
                    produtoRepository.save(produto);
                }
            }
        }
        
        entity.setStatus(newStatus);
        PedidoFornecedor saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        PedidoFornecedor entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de fornecedor não encontrado com ID: " + id));
        if (entity.getStatus() == com.neritech.saas.produtoServico.domain.enums.StatusPedidoFornecedor.RECEBIDO) {
            throw new IllegalStateException("Não é possível excluir um pedido que já foi recebido.");
        }
        
        List<ItemPedidoFornecedor> oldItems = itemRepository.findByPedidoId(id);
        itemRepository.deleteAllInBatch(oldItems);
        
        repository.delete(entity);
    }
}
