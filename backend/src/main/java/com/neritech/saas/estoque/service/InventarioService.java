package com.neritech.saas.estoque.service;

import com.neritech.saas.estoque.domain.Inventario;
import com.neritech.saas.estoque.domain.enums.StatusInventario;
import com.neritech.saas.estoque.dto.InventarioRequest;
import com.neritech.saas.estoque.dto.InventarioResponse;
import com.neritech.saas.estoque.mapper.InventarioMapper;
import com.neritech.saas.estoque.repository.InventarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventarioService {

    private final InventarioRepository repository;
    private final InventarioMapper mapper;

    public InventarioService(InventarioRepository repository, InventarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public InventarioResponse create(InventarioRequest request) {
        if (repository.existsByEmpresaIdAndCodigo(request.empresaId(), request.codigo())) {
            throw new IllegalArgumentException("JÃ¡ existe um inventÃ¡rio com o cÃ³digo " + request.codigo());
        }

        Inventario entity = mapper.toEntity(request);
        Inventario saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public InventarioResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("InventÃ¡rio nÃ£o encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<InventarioResponse> findByEmpresa(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<InventarioResponse> findByEmpresaAndStatus(Long empresaId, StatusInventario status, Pageable pageable) {
        return repository.findByEmpresaIdAndStatus(empresaId, status, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public InventarioResponse update(Long id, InventarioRequest request) {
        Inventario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("InventÃ¡rio nÃ£o encontrado"));

        if (!entity.getEmpresaId().equals(request.empresaId())) {
            throw new IllegalArgumentException("NÃ£o Ã© permitido alterar o ID da empresa");
        }

        if (!entity.getCodigo().equals(request.codigo()) &&
                repository.existsByEmpresaIdAndCodigo(request.empresaId(), request.codigo())) {
            throw new IllegalArgumentException("JÃ¡ existe um inventÃ¡rio com o cÃ³digo " + request.codigo());
        }

        mapper.updateEntityFromRequest(request, entity);
        Inventario saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("InventÃ¡rio nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
