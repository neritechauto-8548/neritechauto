package com.neritech.saas.produtoServico.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.estoque.service.MovimentacaoEstoqueService;
import com.neritech.saas.produtoServico.domain.ItemPedidoFornecedor;
import com.neritech.saas.produtoServico.domain.PedidoFornecedor;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.domain.enums.StatusPedidoFornecedor;
import com.neritech.saas.produtoServico.dto.ItemPedidoFornecedorRequest;
import com.neritech.saas.produtoServico.dto.PedidoFornecedorRequest;
import com.neritech.saas.produtoServico.dto.PedidoFornecedorResponse;
import com.neritech.saas.produtoServico.mapper.PedidoFornecedorMapper;
import com.neritech.saas.produtoServico.repository.FornecedorRepository;
import com.neritech.saas.produtoServico.repository.ItemPedidoFornecedorRepository;
import com.neritech.saas.produtoServico.repository.PedidoFornecedorRepository;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;
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
    private final FornecedorRepository fornecedorRepository;

    public PedidoFornecedorService(
            PedidoFornecedorRepository repository,
            PedidoFornecedorMapper mapper,
            com.neritech.saas.estoque.repository.EstoqueRepository estoqueRepository,
            ItemPedidoFornecedorRepository itemRepository,
            MovimentacaoEstoqueService movimentacaoEstoqueService,
            ProdutoRepository produtoRepository,
            FornecedorRepository fornecedorRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.estoqueRepository = estoqueRepository;
        this.itemRepository = itemRepository;
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional
    public PedidoFornecedorResponse create(PedidoFornecedorRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        requireOwnedSupplier(request.fornecedorId(), tenantId);
        validateOwnedProducts(request.itens(), tenantId);

        PedidoFornecedor entity = mapper.toEntity(request);
        entity.setEmpresaId(tenantId);
        entity.setFornecedorId(request.fornecedorId());
        entity.setNumeroPedido(repository.nextNumeroPedido(tenantId));

        PedidoFornecedor saved = repository.save(entity);
        replaceItems(saved, request.itens(), tenantId);

        return mapper.toResponse(findOwned(saved.getId(), tenantId));
    }

    @Transactional(readOnly = true)
    public PedidoFornecedorResponse findById(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return mapper.toResponse(findOwned(id, tenantId));
    }

    @Transactional(readOnly = true)
    public Page<PedidoFornecedorResponse> findAll(String termo, Pageable pageable) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        if (termo != null && !termo.isBlank()) {
            return repository.findByEmpresaIdAndTermo(tenantId, termo, pageable).map(mapper::toResponse);
        }
        return repository.findByEmpresaId(tenantId, pageable).map(mapper::toResponse);
    }

    @Transactional
    public PedidoFornecedorResponse update(Long id, PedidoFornecedorRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        PedidoFornecedor entity = findOwned(id, tenantId);
        if (entity.getStatus() == StatusPedidoFornecedor.RECEBIDO) {
            throw new IllegalStateException("Não é possível alterar um pedido que já foi recebido.");
        }

        requireOwnedSupplier(request.fornecedorId(), tenantId);
        validateOwnedProducts(request.itens(), tenantId);

        mapper.updateEntityFromRequest(request, entity);
        entity.setFornecedorId(request.fornecedorId());

        PedidoFornecedor saved = repository.save(entity);
        replaceItems(saved, request.itens(), tenantId);

        return mapper.toResponse(findOwned(saved.getId(), tenantId));
    }

    @Transactional
    public PedidoFornecedorResponse updateStatus(Long id, StatusPedidoFornecedor newStatus) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        PedidoFornecedor entity = findOwned(id, tenantId);

        if (entity.getStatus() != StatusPedidoFornecedor.RECEBIDO
                && newStatus == StatusPedidoFornecedor.RECEBIDO) {
            receiveIntoInventory(entity, tenantId);
        }

        entity.setStatus(newStatus);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        PedidoFornecedor entity = findOwned(id, tenantId);
        if (entity.getStatus() == StatusPedidoFornecedor.RECEBIDO) {
            throw new IllegalStateException("Não é possível excluir um pedido que já foi recebido.");
        }

        List<ItemPedidoFornecedor> oldItems = itemRepository.findByPedidoId(id);
        itemRepository.deleteAllInBatch(oldItems);
        repository.delete(entity);
    }

    private PedidoFornecedor findOwned(Long id, Long tenantId) {
        return repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Pedido de fornecedor não encontrado para a empresa autenticada"));
    }

    private void requireOwnedSupplier(Long fornecedorId, Long tenantId) {
        fornecedorRepository.findByIdAndEmpresaId(fornecedorId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fornecedor não encontrado para a empresa autenticada"));
    }

    private void validateOwnedProducts(List<ItemPedidoFornecedorRequest> itens, Long tenantId) {
        if (itens == null) {
            return;
        }
        for (ItemPedidoFornecedorRequest item : itens) {
            produtoRepository.findByIdAndEmpresaId(item.produtoId(), tenantId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Produto não encontrado para a empresa autenticada: " + item.produtoId()));
        }
    }

    private void replaceItems(PedidoFornecedor pedido, List<ItemPedidoFornecedorRequest> itens, Long tenantId) {
        List<ItemPedidoFornecedor> oldItems = itemRepository.findByPedidoId(pedido.getId());
        if (!oldItems.isEmpty()) {
            itemRepository.deleteAllInBatch(oldItems);
        }

        if (itens == null) {
            return;
        }
        for (ItemPedidoFornecedorRequest itemRequest : itens) {
            ItemPedidoFornecedor item = mapper.toItemEntity(itemRequest);
            item.setPedido(pedido);
            item.setEmpresaId(tenantId);
            itemRepository.save(item);
        }
    }

    private void receiveIntoInventory(PedidoFornecedor entity, Long tenantId) {
        for (ItemPedidoFornecedor item : entity.getItens()) {
            Produto produto = produtoRepository.findByIdAndEmpresaId(item.getProdutoId(), tenantId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Produto do pedido não pertence à empresa autenticada: " + item.getProdutoId()));

            movimentacaoEstoqueService.create(new com.neritech.saas.estoque.dto.MovimentacaoEstoqueRequest(
                    tenantId,
                    item.getProdutoId(),
                    com.neritech.saas.estoque.domain.enums.TipoMovimentacao.ENTRADA,
                    null,
                    item.getQuantidade(),
                    item.getPrecoUnitario(),
                    null,
                    null,
                    "PEDIDO_FORNECEDOR",
                    entity.getNumeroPedido() != null ? String.valueOf(entity.getNumeroPedido()) : null,
                    entity.getId(),
                    entity.getFornecedorId(),
                    null,
                    null,
                    null,
                    "Entrada automática por recebimento do Pedido de Fornecedor #" + entity.getNumeroPedido(),
                    null,
                    entity.getCriadoPor() != null ? entity.getCriadoPor() : 1L,
                    null
            ));

            com.neritech.saas.estoque.domain.Estoque estoque = estoqueRepository
                    .findFirstByEmpresaIdAndProdutoId(tenantId, item.getProdutoId())
                    .orElse(null);
            if (estoque != null && item.getPrecoUnitario() != null) {
                estoque.setPrecoCustoLote(item.getPrecoUnitario());
                estoqueRepository.save(estoque);
            }

            if (item.getPrecoUnitario() != null) {
                produto.setPrecoCusto(item.getPrecoUnitario());
                produto.setPrecoCompra(item.getPrecoUnitario());
                produtoRepository.save(produto);
            }
        }
    }
}
