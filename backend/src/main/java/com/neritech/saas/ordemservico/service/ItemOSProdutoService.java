package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.ordemservico.domain.ItemOSProduto;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.ItemOSProdutoRequest;
import com.neritech.saas.ordemservico.dto.ItemOSProdutoResponse;
import com.neritech.saas.ordemservico.mapper.ItemOSProdutoMapper;
import com.neritech.saas.ordemservico.repository.ItemOSProdutoRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.produtoServico.repository.FornecedorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemOSProdutoService {

    private final ItemOSProdutoRepository repository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ItemOSProdutoMapper mapper;

    public ItemOSProdutoService(
            ItemOSProdutoRepository repository,
            OrdemServicoRepository ordemServicoRepository,
            FornecedorRepository fornecedorRepository,
            ItemOSProdutoMapper mapper) {
        this.repository = repository;
        this.ordemServicoRepository = ordemServicoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.mapper = mapper;
    }

    public ItemOSProdutoResponse create(ItemOSProdutoRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OrdemServico ordemServico = requireOwnedOrder(request.ordemServicoId(), tenantId);

        ItemOSProduto entity = mapper.toEntity(request);
        entity.setOrdemServico(ordemServico);
        applyFornecedor(entity, request.fornecedorId(), tenantId);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public ItemOSProdutoResponse findById(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return mapper.toResponse(requireOwnedItem(id, tenantId));
    }

    @Transactional(readOnly = true)
    public List<ItemOSProdutoResponse> findByOrdemServicoId(Long ordemServicoId) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        requireOwnedOrder(ordemServicoId, tenantId);
        return repository.findByOrdemServico_IdAndOrdemServico_EmpresaId(ordemServicoId, tenantId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ItemOSProdutoResponse update(Long id, ItemOSProdutoRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        ItemOSProduto entity = requireOwnedItem(id, tenantId);
        OrdemServico ordemServico = requireOwnedOrder(request.ordemServicoId(), tenantId);

        mapper.updateEntityFromRequest(request, entity);
        entity.setOrdemServico(ordemServico);
        applyFornecedor(entity, request.fornecedorId(), tenantId);
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        repository.delete(requireOwnedItem(id, tenantId));
    }

    private OrdemServico requireOwnedOrder(Long ordemServicoId, Long tenantId) {
        return ordemServicoRepository.findByIdAndEmpresaId(ordemServicoId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ordem de serviço não encontrada para a empresa autenticada"));
    }

    private ItemOSProduto requireOwnedItem(Long id, Long tenantId) {
        return repository.findByIdAndOrdemServico_EmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item de produto não encontrado para a empresa autenticada"));
    }

    private void applyFornecedor(ItemOSProduto entity, Long fornecedorId, Long tenantId) {
        if (fornecedorId == null) {
            entity.setFornecedor(null);
            return;
        }
        entity.setFornecedor(fornecedorRepository.findByIdAndEmpresaId(fornecedorId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fornecedor não encontrado para a empresa autenticada")));
    }
}
