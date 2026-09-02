package com.neritech.saas.empresa.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.empresa.domain.EnderecoEmpresa;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.dto.EnderecoEmpresaRequest;
import com.neritech.saas.empresa.dto.EnderecoEmpresaResponse;
import com.neritech.saas.empresa.mapper.EnderecoEmpresaMapper;
import com.neritech.saas.empresa.repository.EnderecoEmpresaRepository;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EnderecoEmpresaService {

    private final EnderecoEmpresaRepository repository;
    private final EmpresaRepository empresaRepository;
    private final EnderecoEmpresaMapper mapper;

    public EnderecoEmpresaService(
            EnderecoEmpresaRepository repository,
            EmpresaRepository empresaRepository,
            EnderecoEmpresaMapper mapper) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
    }

    public EnderecoEmpresaResponse create(EnderecoEmpresaRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant(request.empresaId());
        Empresa empresa = empresaRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa autenticada não encontrada"));

        EnderecoEmpresa endereco = mapper.toEntity(request);
        endereco.setEmpresa(empresa);

        return mapper.toResponse(repository.save(endereco));
    }

    @Transactional(readOnly = true)
    public EnderecoEmpresaResponse findById(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return mapper.toResponse(findOwned(id, tenantId));
    }

    @Transactional(readOnly = true)
    public Page<EnderecoEmpresaResponse> findAll(Pageable pageable) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return repository.findByEmpresaId(tenantId, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<EnderecoEmpresaResponse> findByEmpresaId(Long empresaId) {
        Long tenantId = TenantAccess.requireCurrentTenant(empresaId);
        return repository.findByEmpresaId(tenantId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public EnderecoEmpresaResponse update(Long id, EnderecoEmpresaRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant(request.empresaId());
        EnderecoEmpresa endereco = findOwned(id, tenantId);

        // A empresa do endereço nunca pode ser trocada por input do cliente.
        mapper.updateEntityFromRequest(request, endereco);
        return mapper.toResponse(repository.save(endereco));
    }

    public void delete(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        EnderecoEmpresa endereco = findOwned(id, tenantId);
        repository.delete(endereco);
    }

    private EnderecoEmpresa findOwned(Long id, Long tenantId) {
        return repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado para a empresa autenticada"));
    }
}
