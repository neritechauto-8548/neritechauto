package com.neritech.saas.ordemservico.service;

import com.neritech.saas.ordemservico.domain.ItemOSProduto;
import com.neritech.saas.ordemservico.dto.ItemOSProdutoRequest;
import com.neritech.saas.ordemservico.dto.ItemOSProdutoResponse;
import com.neritech.saas.ordemservico.mapper.ItemOSProdutoMapper;
import com.neritech.saas.ordemservico.repository.ItemOSProdutoRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.produtoServico.repository.FornecedorRepository;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.common.exception.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemOSProdutoService {

    private final ItemOSProdutoRepository repository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ItemOSProdutoMapper mapper;

    public ItemOSProdutoService(ItemOSProdutoRepository repository, OrdemServicoRepository ordemServicoRepository,
            FornecedorRepository fornecedorRepository, ItemOSProdutoMapper mapper) {
        this.repository = repository;
        this.ordemServicoRepository = ordemServicoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.mapper = mapper;
    }

    public ItemOSProdutoResponse create(ItemOSProdutoRequest request) {
        ItemOSProduto entity = mapper.toEntity(request);
        entity.setOrdemServico(ordemServicoRepository.findByIdAndEmpresaId(request.ordemServicoId(), TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada")));

        if (request.fornecedorId() != null) {
            entity.setFornecedor(fornecedorRepository.findById(request.fornecedorId())
                    .filter(f -> TenantAccess.requireCurrent().equals(f.getEmpresaId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado")));
        }

        ItemOSProduto saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ItemOSProdutoResponse findById(Long id) {
        ItemOSProduto entity = findOwned(id);
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<ItemOSProdutoResponse> findByOrdemServicoId(Long ordemServicoId) {
        ordemServicoRepository.findByIdAndEmpresaId(ordemServicoId, TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada"));
        return repository.findByOrdemServicoId(ordemServicoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public ItemOSProdutoResponse update(Long id, ItemOSProdutoRequest request) {
        ItemOSProduto entity = findOwned(id);
        if (!entity.getOrdemServico().getId().equals(request.ordemServicoId())) {
            throw new ResourceNotFoundException("Ordem de Serviço não encontrada");
        }
        mapper.updateEntityFromRequest(request, entity);
        ItemOSProduto updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        repository.delete(findOwned(id));
    }

    private ItemOSProduto findOwned(Long id) {
        return repository.findByIdAndOrdemServicoEmpresaId(id, TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
    }
}
