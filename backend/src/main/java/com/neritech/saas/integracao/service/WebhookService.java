package com.neritech.saas.integracao.service;

import com.neritech.saas.integracao.domain.Webhook;
import com.neritech.saas.integracao.dto.WebhookRequest;
import com.neritech.saas.integracao.dto.WebhookResponse;
import com.neritech.saas.integracao.mapper.WebhookMapper;
import com.neritech.saas.integracao.repository.WebhookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final WebhookRepository repository;
    private final WebhookMapper mapper;

    @Transactional(readOnly = true)
    public List<WebhookResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WebhookResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Webhook nÃ£o encontrado com id: " + id));
    }

    @Transactional
    public WebhookResponse create(WebhookRequest request) {
        Webhook entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public WebhookResponse update(Long id, WebhookRequest request) {
        Webhook entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Webhook nÃ£o encontrado com id: " + id));

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Webhook nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
