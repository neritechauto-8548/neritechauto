package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.DepartamentoContabio;
import com.neritech.saas.empresa.dto.DepartamentoContabioRequest;
import com.neritech.saas.empresa.dto.DepartamentoContabioResponse;
import com.neritech.saas.empresa.mapper.DepartamentoContabioMapper;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.empresa.repository.DepartamentoContabioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neritech.saas.common.tenancy.TenantContext;

import java.util.List;

@Service
@Transactional
public class DepartamentoContabioService {

    private final DepartamentoContabioRepository repository;
    private final EmpresaRepository empresaRepository;
    private final DepartamentoContabioMapper mapper;

    public DepartamentoContabioService(DepartamentoContabioRepository repository,
                                       EmpresaRepository empresaRepository,
                                       DepartamentoContabioMapper mapper) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
    }

    public DepartamentoContabioResponse create(DepartamentoContabioRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));

        DepartamentoContabio entity = mapper.toEntity(request);
        entity.setEmpresa(empresa);

        DepartamentoContabio saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DepartamentoContabioResponse findById(Long id) {
        DepartamentoContabio entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento Contábio não encontrado com ID: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<DepartamentoContabioResponse> findAll(Pageable pageable) {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            return repository.findAll(pageable).map(mapper::toResponse);
        }
        return repository.findByEmpresaId(tenantId, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<DepartamentoContabioResponse> findByEmpresaId(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public DepartamentoContabioResponse update(Long id, DepartamentoContabioRequest request) {
        DepartamentoContabio entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento Contábio não encontrado com ID: " + id));

        if (!entity.getEmpresa().getId().equals(request.empresaId())) {
            Empresa empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));
            entity.setEmpresa(empresa);
        }

        mapper.updateEntityFromRequest(request, entity);
        DepartamentoContabio updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Departamento Contábio não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}

