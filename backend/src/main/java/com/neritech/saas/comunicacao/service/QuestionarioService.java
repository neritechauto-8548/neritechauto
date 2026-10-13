package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.comunicacao.domain.Questionario;
import com.neritech.saas.comunicacao.dto.QuestionarioRequest;
import com.neritech.saas.comunicacao.dto.QuestionarioResponse;
import com.neritech.saas.comunicacao.mapper.QuestionarioMapper;
import com.neritech.saas.comunicacao.repository.ItQuestionarioRepository;
import com.neritech.saas.comunicacao.repository.QuestionarioRepository;
import com.neritech.saas.comunicacao.repository.PesquisaRespostaRepository;
import java.util.Objects;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neritech.saas.common.exception.BusinessException;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionarioService {

    private final QuestionarioRepository repository;
    private final ItQuestionarioRepository itQuestionarioRepository;
    private final QuestionarioMapper mapper;
    private final PesquisaRespostaRepository respostas;

    private void validate(QuestionarioRequest request, Long id) {
        TenantAccess.require(request.empresaId());
        if (request.empresaId() == null) {
            throw new BusinessException("O ID da empresa é obrigatório.");
        }
        if (request.dsQuestionario() == null || request.dsQuestionario().trim().isEmpty()) {
            throw new BusinessException("O nome do questionário é obrigatório.");
        }
        String ds = request.dsQuestionario().trim();
        if (ds.length() < 2) {
            throw new BusinessException("O nome do questionário deve ter pelo menos 2 caracteres.");
        }
        if (ds.length() > 255) {
            throw new BusinessException("O nome do questionário não pode exceder 255 caracteres.");
        }

        boolean exists;
        if (id == null) {
            exists = repository.existsByEmpresaIdAndDsQuestionarioIgnoreCase(request.empresaId(), ds);
        } else {
            exists = repository.existsByEmpresaIdAndDsQuestionarioIgnoreCaseAndIdNot(request.empresaId(), ds, id);
        }
        if (exists) {
            throw new BusinessException("Já existe um questionário cadastrado com este nome nesta empresa.");
        }
    }

    public QuestionarioResponse create(QuestionarioRequest request) {
        validate(request, null);
        Questionario entity = mapper.toEntity(request);
        Questionario saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public QuestionarioResponse findById(Long id) {
        Questionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + id));
        TenantAccess.require(entity.getEmpresaId());
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<QuestionarioResponse> findAll(Long empresaId, Pageable pageable) {
        TenantAccess.require(empresaId);
        return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
    }

    public QuestionarioResponse update(Long id, QuestionarioRequest request) {
        Questionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + id));
        TenantAccess.require(entity.getEmpresaId());
        if (respostas.existsByQuestionarioIdAndEmpresaId(id, TenantAccess.requireCurrent())
                && (!Objects.equals(entity.getDsQuestionario(), request.dsQuestionario())
                || !Objects.equals(entity.getUrlImagem(), request.urlImagem()))) {
            throw new BusinessException("Este questionário já possui respostas: apenas ativar ou inativar é permitido.");
        }
        validate(request, id);
        mapper.updateEntityFromRequest(request, entity);
        Questionario updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        Questionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + id));
        TenantAccess.require(entity.getEmpresaId());
        if (respostas.existsByQuestionarioIdAndEmpresaId(id, TenantAccess.requireCurrent())) {
            throw new BusinessException("Um questionário com respostas não pode ser excluído.");
        }
        itQuestionarioRepository.deleteByQuestionarioId(id);
        repository.delete(entity);
    }
}

