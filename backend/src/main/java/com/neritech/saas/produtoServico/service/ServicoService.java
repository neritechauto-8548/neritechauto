package com.neritech.saas.produtoServico.service;

import com.neritech.saas.produtoServico.domain.Servico;
import com.neritech.saas.produtoServico.dto.ServicoRequest;
import com.neritech.saas.produtoServico.dto.ServicoResponse;
import com.neritech.saas.produtoServico.mapper.ServicoMapper;
import com.neritech.saas.produtoServico.repository.ServicoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import java.math.BigDecimal;

@Service
public class ServicoService {

    private final ServicoRepository repository;
    private final ServicoMapper mapper;

    public ServicoService(ServicoRepository repository, ServicoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ServicoResponse create(ServicoRequest request) {
        validate(request, null);
        Servico entity = mapper.toEntity(request);
        Servico saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ServicoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ServicoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ServicoResponse> search(Long empresaId, String query, Pageable pageable) {
        return repository.search(empresaId, query, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public ServicoResponse update(Long id, ServicoRequest request) {
        Servico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com ID: " + id));

        validate(request, id);
        mapper.updateEntityFromRequest(request, entity);
        Servico saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    private void validate(ServicoRequest request, Long id) {
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            throw new BusinessException("O nome do serviço é obrigatório.");
        }
        if (request.nome().trim().length() < 2 || request.nome().trim().length() > 255) {
            throw new BusinessException("O nome do serviço deve ter entre 2 e 255 caracteres.");
        }
        if (request.precoBase() == null) {
            throw new BusinessException("O preço base é obrigatório.");
        }
        if (request.precoBase().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("O preço base não pode ser negativo.");
        }
        if (request.custo() == null) {
            throw new BusinessException("O custo é obrigatório.");
        }
        if (request.custo().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("O custo não pode ser negativo.");
        }
        if (request.precoBase().compareTo(request.custo()) < 0) {
            throw new BusinessException("O preço base de venda do serviço não pode ser inferior ao seu custo de execução.");
        }

        Long tenantId = TenantContext.getCurrentTenant();
        boolean exists;
        if (id == null) {
            exists = repository.existsByEmpresaIdAndNomeIgnoreCase(tenantId, request.nome().trim());
        } else {
            exists = repository.existsByEmpresaIdAndNomeIgnoreCaseAndIdNot(tenantId, request.nome().trim(), id);
        }
        if (exists) {
            throw new BusinessException("Já existe um serviço cadastrado com este nome nesta empresa.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Serviço não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
