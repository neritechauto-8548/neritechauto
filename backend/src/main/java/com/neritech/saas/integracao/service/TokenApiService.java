package com.neritech.saas.integracao.service;

import com.neritech.saas.integracao.domain.IntegracaoAtiva;
import com.neritech.saas.integracao.domain.TokenApi;
import com.neritech.saas.integracao.dto.TokenApiRequest;
import com.neritech.saas.integracao.dto.TokenApiResponse;
import com.neritech.saas.integracao.mapper.TokenApiMapper;
import com.neritech.saas.integracao.repository.IntegracaoAtivaRepository;
import com.neritech.saas.integracao.repository.TokenApiRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenApiService {

    private final TokenApiRepository repository;
    private final IntegracaoAtivaRepository integracaoAtivaRepository;
    private final TokenApiMapper mapper;

    @Transactional(readOnly = true)
    public List<TokenApiResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TokenApiResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Token API nÃ£o encontrado com id: " + id));
    }

    @Transactional
    public TokenApiResponse create(TokenApiRequest request) {
        if (repository.findByToken(request.token()).isPresent()) {
            throw new IllegalArgumentException("JÃ¡ existe um token com este valor.");
        }

        TokenApi entity = mapper.toEntity(request);

        if (request.integracaoId() != null) {
            IntegracaoAtiva integracao = integracaoAtivaRepository.findById(request.integracaoId())
                    .orElseThrow(() -> new EntityNotFoundException("IntegraÃ§Ã£o nÃ£o encontrada"));
            entity.setIntegracao(integracao);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public TokenApiResponse update(Long id, TokenApiRequest request) {
        TokenApi entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Token API nÃ£o encontrado com id: " + id));

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
            throw new EntityNotFoundException("Token API nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
