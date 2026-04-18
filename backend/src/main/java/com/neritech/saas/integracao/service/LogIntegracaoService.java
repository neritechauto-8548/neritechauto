package com.neritech.saas.integracao.service;

import com.neritech.saas.integracao.domain.IntegracaoAtiva;
import com.neritech.saas.integracao.domain.LogIntegracao;
import com.neritech.saas.integracao.dto.LogIntegracaoRequest;
import com.neritech.saas.integracao.dto.LogIntegracaoResponse;
import com.neritech.saas.integracao.mapper.LogIntegracaoMapper;
import com.neritech.saas.integracao.repository.IntegracaoAtivaRepository;
import com.neritech.saas.integracao.repository.LogIntegracaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogIntegracaoService {

    private final LogIntegracaoRepository repository;
    private final IntegracaoAtivaRepository integracaoAtivaRepository;
    private final LogIntegracaoMapper mapper;

    @Transactional(readOnly = true)
    public List<LogIntegracaoResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LogIntegracaoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Log de integraÃ§Ã£o nÃ£o encontrado com id: " + id));
    }

    @Transactional
    public LogIntegracaoResponse create(LogIntegracaoRequest request) {
        LogIntegracao entity = mapper.toEntity(request);

        if (request.integracaoId() != null) {
            IntegracaoAtiva integracao = integracaoAtivaRepository.findById(request.integracaoId())
                    .orElseThrow(() -> new EntityNotFoundException("IntegraÃ§Ã£o nÃ£o encontrada"));
            entity.setIntegracao(integracao);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public LogIntegracaoResponse update(Long id, LogIntegracaoRequest request) {
        LogIntegracao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Log de integraÃ§Ã£o nÃ£o encontrado com id: " + id));

        mapper.updateEntityFromRequest(request, entity);

        if (request.integracaoId() != null) {
            IntegracaoAtiva integracao = integracaoAtivaRepository.findById(request.integracaoId())
                    .orElseThrow(() -> new EntityNotFoundException("IntegraÃ§Ã£o nÃ£o encontrada"));
            entity.setIntegracao(integracao);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Log de integraÃ§Ã£o nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
