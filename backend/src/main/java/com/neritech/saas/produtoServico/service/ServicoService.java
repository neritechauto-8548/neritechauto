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
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.empresa.repository.DepartamentoContabioRepository;
import java.math.BigDecimal;

@Service
public class ServicoService {

    private final ServicoRepository repository;
    private final ServicoMapper mapper;
    private final DepartamentoContabioRepository areaRepository;

    public ServicoService(ServicoRepository repository, ServicoMapper mapper, DepartamentoContabioRepository areaRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.areaRepository = areaRepository;
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
        return repository.findByIdAndEmpresaId(id, TenantAccess.requireCurrent())
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ServicoResponse> search(Long empresaId, String query, Long areaId, Boolean ativo, Pageable pageable) {
        Long tenantId = TenantAccess.require(empresaId);
        return repository.search(tenantId, query == null || query.isBlank() ? "" : query.trim(), areaId, ativo, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public ServicoResponse update(Long id, ServicoRequest request) {
        Servico entity = repository.findByIdAndEmpresaId(id, TenantAccess.requireCurrent())
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
        Long tenantId = TenantAccess.requireCurrent();
        if (request.tempoPrevistoHoras() == null || request.tempoPrevistoHoras().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O tempo previsto deve ser maior que zero.");
        }
        if (request.areaId() == null || areaRepository.findByIdAndEmpresaId(request.areaId(), tenantId).isEmpty()) {
            throw new EntityNotFoundException("Área não encontrada.");
        }
        boolean exists;
        if (id == null) {
            exists = repository.existsByEmpresaIdAndNomeIgnoreCase(tenantId, request.nome().trim());
        } else {
            exists = repository.existsByEmpresaIdAndNomeIgnoreCaseAndIdNot(tenantId, request.nome().trim(), id);
        }
        if (exists) {
            throw new BusinessException("Já existe um serviço cadastrado com este nome nesta empresa.");
        }
        String codigo = request.codigo() == null ? "" : request.codigo().trim();
        if (!codigo.isEmpty() && (id == null
                ? repository.existsByEmpresaIdAndCodigoIgnoreCase(tenantId, codigo)
                : repository.existsByEmpresaIdAndCodigoIgnoreCaseAndIdNot(tenantId, codigo, id))) {
            throw new BusinessException("Já existe um serviço cadastrado com este código nesta empresa.");
        }
    }

    @Transactional
    public void delete(Long id) {
        Servico entity = repository.findByIdAndEmpresaId(id, TenantAccess.requireCurrent())
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com ID: " + id));
        entity.setAtivo(false);
        repository.save(entity);
    }
}
