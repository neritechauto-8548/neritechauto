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

@Service("comunicacaoItQuestionarioService")
@Transactional
@RequiredArgsConstructor
public class ItQuestionarioService {

    private final ItQuestionarioRepository repository;
    private final QuestionarioRepository questionarioRepository;
    private final ItQuestionarioMapper mapper;

    public ItQuestionarioResponse create(ItQuestionarioRequest request) {
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
                .orElseThrow(() -> new EntityNotFoundException("Item de checklist não encontrado com ID: " + id));
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
                .orElseThrow(() -> new EntityNotFoundException("Item de checklist não encontrado com ID: " + id));

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
            throw new EntityNotFoundException("Item de checklist não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
