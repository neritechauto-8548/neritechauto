package com.neritech.saas.integracao.service;

import com.neritech.saas.integracao.domain.IntegracaoAtiva;
import com.neritech.saas.integracao.domain.Sincronizacao;
import com.neritech.saas.integracao.dto.SincronizacaoRequest;
import com.neritech.saas.integracao.dto.SincronizacaoResponse;
import com.neritech.saas.integracao.mapper.SincronizacaoMapper;
import com.neritech.saas.integracao.repository.IntegracaoAtivaRepository;
import com.neritech.saas.integracao.repository.SincronizacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SincronizacaoService {

    private final SincronizacaoRepository repository;
    private final IntegracaoAtivaRepository integracaoAtivaRepository;
    private final SincronizacaoMapper mapper;

    @Transactional(readOnly = true)
    public List<SincronizacaoResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SincronizacaoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("SincronizaÃ§Ã£o nÃ£o encontrada com id: " + id));
    }

    @Transactional
    public SincronizacaoResponse create(SincronizacaoRequest request) {
        Sincronizacao entity = mapper.toEntity(request);

        if (request.integracaoId() != null) {
            IntegracaoAtiva integracao = integracaoAtivaRepository.findById(request.integracaoId())
                    .orElseThrow(() -> new EntityNotFoundException("IntegraÃ§Ã£o nÃ£o encontrada"));
            entity.setIntegracao(integracao);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public SincronizacaoResponse update(Long id, SincronizacaoRequest request) {
        Sincronizacao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SincronizaÃ§Ã£o nÃ£o encontrada com id: " + id));

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
            throw new EntityNotFoundException("SincronizaÃ§Ã£o nÃ£o encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
