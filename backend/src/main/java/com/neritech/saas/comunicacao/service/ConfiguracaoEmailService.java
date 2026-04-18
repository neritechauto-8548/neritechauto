package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.ConfiguracaoEmail;
import com.neritech.saas.comunicacao.dto.ConfiguracaoEmailRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoEmailResponse;
import com.neritech.saas.comunicacao.mapper.ConfiguracaoEmailMapper;
import com.neritech.saas.comunicacao.repository.ConfiguracaoEmailRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConfiguracaoEmailService {

    private final ConfiguracaoEmailRepository repository;
    private final ConfiguracaoEmailMapper mapper;

    @Transactional(readOnly = true)
    public Page<ConfiguracaoEmailResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ConfiguracaoEmailResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o de Email nÃ£o encontrada com id: " + id));
    }

    public ConfiguracaoEmailResponse create(ConfiguracaoEmailRequest request) {
        ConfiguracaoEmail entity = mapper.toEntity(request);
        ConfiguracaoEmail savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public ConfiguracaoEmailResponse update(Long id, ConfiguracaoEmailRequest request) {
        ConfiguracaoEmail entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o de Email nÃ£o encontrada com id: " + id));

        mapper.updateEntity(request, entity);
        ConfiguracaoEmail updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ConfiguraÃ§Ã£o de Email nÃ£o encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
