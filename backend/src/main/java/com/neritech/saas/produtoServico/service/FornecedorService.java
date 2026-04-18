package com.neritech.saas.produtoServico.service;

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
        if (request.cpf() != null && repository.existsByEmpresaIdAndCpf(request.empresaId(), request.cpf())) {
            throw new IllegalArgumentException("JÃ¡ existe um fornecedor com este CPF nesta empresa");
        }
        if (request.cnpj() != null && repository.existsByEmpresaIdAndCnpj(request.empresaId(), request.cnpj())) {
            throw new IllegalArgumentException("JÃ¡ existe um fornecedor com este CNPJ nesta empresa");
        }

        Fornecedor entity = mapper.toEntity(request);
        entity.setEmpresaId(request.empresaId());

        Fornecedor saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public FornecedorResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor nÃ£o encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<FornecedorResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public FornecedorResponse update(Long id, FornecedorRequest request) {
        Fornecedor entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor nÃ£o encontrado com ID: " + id));

        if (!entity.getEmpresaId().equals(request.empresaId())) {
            throw new IllegalArgumentException("NÃ£o Ã© permitido alterar a empresa do fornecedor");
        }

        if (request.cpf() != null && !request.cpf().equals(entity.getCpf()) &&
                repository.existsByEmpresaIdAndCpf(request.empresaId(), request.cpf())) {
            throw new IllegalArgumentException("JÃ¡ existe um fornecedor com este CPF nesta empresa");
        }
        if (request.cnpj() != null && !request.cnpj().equals(entity.getCnpj()) &&
                repository.existsByEmpresaIdAndCnpj(request.empresaId(), request.cnpj())) {
            throw new IllegalArgumentException("JÃ¡ existe um fornecedor com este CNPJ nesta empresa");
        }

        mapper.updateEntityFromRequest(request, entity);
        Fornecedor saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Fornecedor nÃ£o encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
