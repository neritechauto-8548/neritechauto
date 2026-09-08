package com.neritech.saas.produtoServico.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.dto.FornecedorRequest;
import com.neritech.saas.produtoServico.dto.FornecedorResponse;
import com.neritech.saas.produtoServico.mapper.FornecedorMapper;
import com.neritech.saas.produtoServico.repository.FornecedorRepository;

@Service
public class FornecedorService {

    private final FornecedorRepository repository;
    private final FornecedorMapper mapper;

    public FornecedorService(FornecedorRepository repository, FornecedorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public FornecedorResponse create(FornecedorRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        validateUniqueDocuments(tenantId, request, null);

        Fornecedor entity = mapper.toEntity(request);
        entity.setEmpresaId(tenantId);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public FornecedorResponse findById(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return mapper.toResponse(findOwned(id, tenantId));
    }

    @Transactional(readOnly = true)
    public Page<FornecedorResponse> findAll(Pageable pageable) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return repository.findByEmpresaId(tenantId, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<FornecedorResponse> search(String query, Pageable pageable) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return repository.search(tenantId, query, pageable).map(mapper::toResponse);
    }

    @Transactional
    public FornecedorResponse update(Long id, FornecedorRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        Fornecedor entity = findOwned(id, tenantId);
        validateUniqueDocuments(tenantId, request, entity);

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        repository.delete(findOwned(id, tenantId));
    }

    private Fornecedor findOwned(Long id, Long tenantId) {
        return repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fornecedor não encontrado para a empresa autenticada"));
    }

    private void validateUniqueDocuments(Long tenantId, FornecedorRequest request, Fornecedor current) {
        if (request.cpf() != null
                && (current == null || !request.cpf().equals(current.getCpf()))
                && repository.existsByEmpresaIdAndCpf(tenantId, request.cpf())) {
            throw new IllegalArgumentException("Já existe um fornecedor com este CPF nesta empresa");
        }
        if (request.cnpj() != null
                && (current == null || !request.cnpj().equals(current.getCnpj()))
                && repository.existsByEmpresaIdAndCnpj(tenantId, request.cnpj())) {
            throw new IllegalArgumentException("Já existe um fornecedor com este CNPJ nesta empresa");
        }
    }
}
