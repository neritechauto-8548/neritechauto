package com.neritech.saas.produtoServico.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.neritech.saas.produtoServico.domain.CategoriaProduto;
import com.neritech.saas.produtoServico.dto.CategoriaProdutoRequest;
import com.neritech.saas.produtoServico.dto.CategoriaProdutoResponse;
import com.neritech.saas.produtoServico.mapper.CategoriaProdutoMapper;
import com.neritech.saas.produtoServico.repository.CategoriaProdutoRepository;

@Service
public class CategoriaProdutoService {

    private final CategoriaProdutoRepository repository;
    private final CategoriaProdutoMapper mapper;

    public CategoriaProdutoService(CategoriaProdutoRepository repository, CategoriaProdutoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public CategoriaProdutoResponse create(CategoriaProdutoRequest request) {
        CategoriaProduto entity = mapper.toEntity(request);
        entity.setEmpresaId(request.empresaId());

        CategoriaProduto saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CategoriaProdutoResponse> listRoots(Long empresaId) {
        Long tenantId = (empresaId != null) ? empresaId : com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        return repository.findByEmpresaId(tenantId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaProdutoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Categoria de produto não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<CategoriaProdutoResponse> findAll(Long empresaId, String search, Pageable pageable) {
        Long tenantId = (empresaId != null) ? empresaId : com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        
        Page<CategoriaProduto> result;
        if (search != null && !search.isBlank()) {
            result = repository.findByEmpresaIdAndNomeContainingIgnoreCase(tenantId, search, pageable);
        } else {
            result = repository.findByEmpresaId(tenantId, pageable);
        }
        return result.map(mapper::toResponse);
    }

    @Transactional
    public CategoriaProdutoResponse update(Long id, CategoriaProdutoRequest request) {
        CategoriaProduto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria de produto não encontrada com ID: " + id));

        if (!entity.getEmpresaId().equals(request.empresaId())) {
            throw new IllegalArgumentException("Não é permitido alterar a empresa da categoria");
        }

        mapper.updateEntityFromRequest(request, entity);

        CategoriaProduto saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Categoria de produto não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
