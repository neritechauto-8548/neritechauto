package com.neritech.saas.integracao.service;

import com.neritech.saas.integracao.domain.IntegracaoAtiva;
import com.neritech.saas.integracao.domain.MapeamentoDados;
import com.neritech.saas.integracao.dto.MapeamentoDadosRequest;
import com.neritech.saas.integracao.dto.MapeamentoDadosResponse;
import com.neritech.saas.integracao.mapper.MapeamentoDadosMapper;
import com.neritech.saas.integracao.repository.IntegracaoAtivaRepository;
import com.neritech.saas.integracao.repository.MapeamentoDadosRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapeamentoDadosService {

    private final MapeamentoDadosRepository repository;
    private final IntegracaoAtivaRepository integracaoAtivaRepository;
    private final MapeamentoDadosMapper mapper;

    @Transactional(readOnly = true)
    public List<MapeamentoDadosResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MapeamentoDadosResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Mapeamento de dados nÃ£o encontrado com id: " + id));
    }

    @Transactional
    public MapeamentoDadosResponse create(MapeamentoDadosRequest request) {
        MapeamentoDados entity = mapper.toEntity(request);

        if (request.integracaoId() != null) {
            IntegracaoAtiva integracao = integracaoAtivaRepository.findById(request.integracaoId())
                    .orElseThrow(() -> new EntityNotFoundException("IntegraÃ§Ã£o nÃ£o encontrada"));
            entity.setIntegracao(integracao);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public MapeamentoDadosResponse update(Long id, MapeamentoDadosRequest request) {
        MapeamentoDados entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mapeamento de dados nÃ£o encontrado com id: " + id));

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
            throw new EntityNotFoundException("Mapeamento de dados nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
