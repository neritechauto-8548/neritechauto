package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.Setor;
import com.neritech.saas.empresa.dto.SetorRequest;
import com.neritech.saas.empresa.dto.SetorResponse;
import com.neritech.saas.empresa.mapper.SetorMapper;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.empresa.repository.SetorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neritech.saas.common.tenancy.TenantContext;

import java.util.List;

@Service
@Transactional
public class SetorService {

    private final SetorRepository repository;
    private final EmpresaRepository empresaRepository;
    private final SetorMapper mapper;

    public SetorService(SetorRepository repository,
                        EmpresaRepository empresaRepository,
                        SetorMapper mapper) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
    }

    public SetorResponse create(SetorRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));

        Setor setor = mapper.toEntity(request);
        setor.setEmpresa(empresa);

        Setor saved = repository.save(setor);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public SetorResponse findById(Long id) {
        Setor setor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado com ID: " + id));
        return mapper.toResponse(setor);
    }

    @Transactional(readOnly = true)
    public Page<SetorResponse> findAll(Pageable pageable) {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            return repository.findAll(pageable).map(mapper::toResponse);
        }
        return repository.findByEmpresaId(tenantId, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<SetorResponse> findByEmpresaId(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public SetorResponse update(Long id, SetorRequest request) {
        Setor setor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado com ID: " + id));

        if (!setor.getEmpresa().getId().equals(request.empresaId())) {
            Empresa empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));
            setor.setEmpresa(empresa);
        }

        mapper.updateEntityFromRequest(request, setor);
        Setor updated = repository.save(setor);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Setor não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}

