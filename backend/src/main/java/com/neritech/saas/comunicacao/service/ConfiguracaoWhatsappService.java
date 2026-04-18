package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.ConfiguracaoWhatsapp;
import com.neritech.saas.comunicacao.dto.ConfiguracaoWhatsappRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoWhatsappResponse;
import com.neritech.saas.comunicacao.mapper.ConfiguracaoWhatsappMapper;
import com.neritech.saas.comunicacao.repository.ConfiguracaoWhatsappRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConfiguracaoWhatsappService {

    private final ConfiguracaoWhatsappRepository repository;
    private final ConfiguracaoWhatsappMapper mapper;

    @Transactional(readOnly = true)
    public Page<ConfiguracaoWhatsappResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ConfiguracaoWhatsappResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("ConfiguraÃ§Ã£o de WhatsApp nÃ£o encontrada com id: " + id));
    }

    public ConfiguracaoWhatsappResponse create(ConfiguracaoWhatsappRequest request) {
        ConfiguracaoWhatsapp entity = mapper.toEntity(request);
        ConfiguracaoWhatsapp savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public ConfiguracaoWhatsappResponse update(Long id, ConfiguracaoWhatsappRequest request) {
        ConfiguracaoWhatsapp entity = repository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("ConfiguraÃ§Ã£o de WhatsApp nÃ£o encontrada com id: " + id));

        mapper.updateEntity(request, entity);
        ConfiguracaoWhatsapp updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ConfiguraÃ§Ã£o de WhatsApp nÃ£o encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
