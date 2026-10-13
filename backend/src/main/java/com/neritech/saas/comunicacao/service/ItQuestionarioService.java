package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.comunicacao.domain.ItQuestionario;
import com.neritech.saas.comunicacao.domain.Questionario;
import com.neritech.saas.comunicacao.domain.enums.StatusPesquisa;
import com.neritech.saas.comunicacao.dto.ItQuestionarioRequest;
import com.neritech.saas.comunicacao.dto.ItQuestionarioResponse;
import com.neritech.saas.comunicacao.mapper.ItQuestionarioMapper;
import com.neritech.saas.comunicacao.repository.ItQuestionarioRepository;
import com.neritech.saas.comunicacao.repository.QuestionarioRepository;
import com.neritech.saas.comunicacao.repository.PesquisaRespostaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.neritech.saas.common.exception.BusinessException;

@Service("comunicacaoItQuestionarioService")
@Transactional
@RequiredArgsConstructor
public class ItQuestionarioService {

    private final ItQuestionarioRepository repository;
    private final QuestionarioRepository questionarioRepository;
    private final ItQuestionarioMapper mapper;
    private final PesquisaRespostaRepository respostas;

    private void validate(ItQuestionarioRequest request, Long id) {
        if (request.questionarioId() == null) {
            throw new BusinessException("O ID do questionário é obrigatório.");
        }
        if (request.dsItQuestionario() == null || request.dsItQuestionario().trim().isEmpty()) {
            throw new BusinessException("A descrição da pergunta é obrigatória.");
        }
        String ds = request.dsItQuestionario().trim();
        if (ds.length() < 2) {
            throw new BusinessException("A descrição da pergunta deve ter pelo menos 2 caracteres.");
        }
        if (ds.length() > 255) {
            throw new BusinessException("A descrição da pergunta não pode exceder 255 caracteres.");
        }
        if (request.tpItQuestionario() == null) {
            throw new BusinessException("O tipo de resposta da pergunta é obrigatório.");
        }

        boolean exists;
        if (id == null) {
            exists = repository.existsByQuestionarioIdAndDsItQuestionarioIgnoreCase(request.questionarioId(), ds);
        } else {
            exists = repository.existsByQuestionarioIdAndDsItQuestionarioIgnoreCaseAndIdNot(request.questionarioId(), ds, id);
        }
        if (exists) {
            throw new BusinessException("Já existe uma pergunta cadastrada com esta descrição neste questionário.");
        }
    }

    private void assertEditable(Long questionarioId) {
        Questionario questionario = questionarioRepository.findById(questionarioId)
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado"));
        TenantAccess.require(questionario.getEmpresaId());
        if (questionario.getStatusPesquisa() != StatusPesquisa.DRAFT) {
            throw new BusinessException("Perguntas de pesquisas publicadas, pausadas ou encerradas não podem ser alteradas.");
        }
        if (respostas.existsByQuestionarioIdAndEmpresaId(questionarioId, TenantAccess.requireCurrent())) {
            throw new BusinessException("O questionário já recebeu respostas: suas perguntas estão protegidas.");
        }
    }

    public ItQuestionarioResponse create(ItQuestionarioRequest request) {
        if (request.questionarioId() == null) {
            throw new BusinessException("O ID do questionário é obrigatório.");
        }
        // Autorizar o questionário antes de consultar nomes de perguntas;
        // validações de duplicidade não devem revelar conteúdo de outro tenant.
        Questionario questionario = questionarioRepository.findById(request.questionarioId())
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + request.questionarioId()));
        TenantAccess.require(questionario.getEmpresaId());
        assertEditable(request.questionarioId());
        validate(request, null);

        ItQuestionario entity = mapper.toEntity(request);
        entity.setQuestionario(questionario);

        ItQuestionario saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ItQuestionarioResponse findById(Long id) {
        ItQuestionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada com ID: " + id));
        TenantAccess.require(entity.getQuestionario().getEmpresaId());
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<ItQuestionarioResponse> findAll(Pageable pageable) {
        return repository.findByQuestionarioEmpresaId(TenantAccess.requireCurrent(), pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ItQuestionarioResponse> findByQuestionarioId(Long questionarioId) {
        Questionario questionario = questionarioRepository.findById(questionarioId)
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + questionarioId));
        TenantAccess.require(questionario.getEmpresaId());
        return repository.findByQuestionarioId(questionarioId)
                .stream().map(mapper::toResponse).toList();
    }

    public ItQuestionarioResponse update(Long id, ItQuestionarioRequest request) {
        ItQuestionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada com ID: " + id));

        TenantAccess.require(entity.getQuestionario().getEmpresaId());
        assertEditable(entity.getQuestionario().getId());
        if (request.questionarioId() == null) {
            throw new BusinessException("O ID do questionário é obrigatório.");
        }
        Questionario destino = null;
        if (!entity.getQuestionario().getId().equals(request.questionarioId())) {
            destino = questionarioRepository.findById(request.questionarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + request.questionarioId()));
            TenantAccess.require(destino.getEmpresaId());
            assertEditable(request.questionarioId());
        }
        validate(request, id);
        if (destino != null) entity.setQuestionario(destino);

        mapper.updateEntityFromRequest(request, entity);
        ItQuestionario updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        ItQuestionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada com ID: " + id));
        TenantAccess.require(entity.getQuestionario().getEmpresaId());
        assertEditable(entity.getQuestionario().getId());
        repository.delete(entity);
    }
}
