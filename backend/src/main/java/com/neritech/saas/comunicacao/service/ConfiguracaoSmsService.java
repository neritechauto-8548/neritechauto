package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.ConfiguracaoSms;
import com.neritech.saas.comunicacao.dto.ConfiguracaoSmsRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoSmsResponse;
import com.neritech.saas.comunicacao.mapper.ConfiguracaoSmsMapper;
import com.neritech.saas.comunicacao.repository.ConfiguracaoSmsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConfiguracaoSmsService {

    private final ConfiguracaoSmsRepository repository;
    private final ConfiguracaoSmsMapper mapper;

    @Transactional(readOnly = true)
    public Page<ConfiguracaoSmsResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ConfiguracaoSmsResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o SMS nÃ£o encontrada com id: " + id));
    }

    public ConfiguracaoSmsResponse create(ConfiguracaoSmsRequest request) {
        ConfiguracaoSms entity = mapper.toEntity(request);
        ConfiguracaoSms savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public ConfiguracaoSmsResponse update(Long id, ConfiguracaoSmsRequest request) {
        ConfiguracaoSms entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o SMS nÃ£o encontrada com id: " + id));

        mapper.updateEntity(request, entity);
        ConfiguracaoSms updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ConfiguraÃ§Ã£o SMS nÃ£o encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
