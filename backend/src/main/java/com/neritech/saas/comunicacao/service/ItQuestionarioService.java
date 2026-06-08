package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.ItQuestionario;
import com.neritech.saas.comunicacao.domain.Questionario;
import com.neritech.saas.comunicacao.dto.ItQuestionarioRequest;
import com.neritech.saas.comunicacao.dto.ItQuestionarioResponse;
import com.neritech.saas.comunicacao.mapper.ItQuestionarioMapper;
import com.neritech.saas.comunicacao.repository.ItQuestionarioRepository;
import com.neritech.saas.comunicacao.repository.QuestionarioRepository;
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

    public ItQuestionarioResponse create(ItQuestionarioRequest request) {
        validate(request, null);
        Questionario questionario = questionarioRepository.findById(request.questionarioId())
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + request.questionarioId()));

        ItQuestionario entity = mapper.toEntity(request);
        entity.setQuestionario(questionario);

        ItQuestionario saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ItQuestionarioResponse findById(Long id) {
        ItQuestionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada com ID: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<ItQuestionarioResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ItQuestionarioResponse> findByQuestionarioId(Long questionarioId) {
        return repository.findByQuestionarioId(questionarioId)
                .stream().map(mapper::toResponse).toList();
    }

    public ItQuestionarioResponse update(Long id, ItQuestionarioRequest request) {
        ItQuestionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada com ID: " + id));

        validate(request, id);

        if (!entity.getQuestionario().getId().equals(request.questionarioId())) {
            Questionario questionario = questionarioRepository.findById(request.questionarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado com ID: " + request.questionarioId()));
            entity.setQuestionario(questionario);
        }

        mapper.updateEntityFromRequest(request, entity);
        ItQuestionario updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Pergunta não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
