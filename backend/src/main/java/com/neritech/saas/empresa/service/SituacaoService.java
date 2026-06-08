package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.Situacao;
import com.neritech.saas.empresa.dto.SituacaoRequest;
import com.neritech.saas.empresa.dto.SituacaoResponse;
import com.neritech.saas.empresa.mapper.SituacaoMapper;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.empresa.repository.SituacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neritech.saas.common.tenancy.TenantContext;

import java.util.List;

@Service
@Transactional
public class SituacaoService {

    private final SituacaoRepository repository;
    private final EmpresaRepository empresaRepository;
    private final SituacaoMapper mapper;

    public SituacaoService(SituacaoRepository repository,
                           EmpresaRepository empresaRepository,
                           SituacaoMapper mapper) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
    }

    public SituacaoResponse create(SituacaoRequest request) {
        validarSituacao(null, request);
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));

        Situacao entity = mapper.toEntity(request);
        entity.setCorSituacao(normalizarCor(request.corSituacao()));
        entity.setEmpresa(empresa);
        Situacao saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public SituacaoResponse findById(Long id) {
        Situacao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Situação não encontrada com ID: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<SituacaoResponse> findAll(Pageable pageable) {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            return repository.findAll(pageable).map(mapper::toResponse);
        }
        return repository.findByEmpresaId(tenantId, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<SituacaoResponse> findByEmpresaId(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public SituacaoResponse update(Long id, SituacaoRequest request) {
        validarSituacao(id, request);
        Situacao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Situação não encontrada com ID: " + id));

        if (!entity.getEmpresa().getId().equals(request.empresaId())) {
            Empresa empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));
            entity.setEmpresa(empresa);
        }

        mapper.updateEntityFromRequest(request, entity);
        entity.setCorSituacao(normalizarCor(request.corSituacao()));
        Situacao updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Situação não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }

    private void validarSituacao(Long id, SituacaoRequest request) {
        if (request.nmSituacao() == null || request.nmSituacao().trim().isEmpty()) {
            throw new com.neritech.saas.common.exception.BusinessException("O nome da situação é obrigatório.");
        }
        if (request.nmSituacao().trim().length() < 2) {
            throw new com.neritech.saas.common.exception.BusinessException("O nome da situação deve ter pelo menos 2 caracteres.");
        }
        if (request.nmSituacao().trim().length() > 255) {
            throw new com.neritech.saas.common.exception.BusinessException("O nome da situação não pode exceder 255 caracteres.");
        }
        if (request.dsSituacao() != null && request.dsSituacao().trim().length() > 1000) {
            throw new com.neritech.saas.common.exception.BusinessException("A explicação não pode exceder 1000 caracteres.");
        }
        if (request.empresaId() == null) {
            throw new com.neritech.saas.common.exception.BusinessException("O ID da empresa é obrigatório.");
        }

        boolean duplicado = id == null
                ? repository.existsByEmpresaIdAndNmSituacaoIgnoreCase(request.empresaId(), request.nmSituacao().trim())
                : repository.existsByEmpresaIdAndNmSituacaoIgnoreCaseAndIdNot(request.empresaId(), request.nmSituacao().trim(), id);
        if (duplicado) {
            throw new com.neritech.saas.common.exception.BusinessException("Já existe uma situação cadastrada com este nome nesta empresa.");
        }
    }

    private String normalizarCor(String cor) {
        if (cor == null || cor.trim().isEmpty()) {
            return "#2563EB";
        }
        String trimCor = cor.trim();
        if (!trimCor.startsWith("#")) {
            trimCor = "#" + trimCor;
        }
        if (!trimCor.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
            throw new com.neritech.saas.common.exception.BusinessException("Formato de cor inválido. Deve ser um código hexadecimal (ex: #2563EB).");
        }
        return trimCor;
    }
}

