package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.ComunicacaoEnviada;
import com.neritech.saas.comunicacao.dto.ComunicacaoEnviadaRequest;
import com.neritech.saas.comunicacao.dto.ComunicacaoEnviadaResponse;
import com.neritech.saas.comunicacao.mapper.ComunicacaoEnviadaMapper;
import com.neritech.saas.comunicacao.repository.ComunicacaoEnviadaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ComunicacaoEnviadaService {

    private final ComunicacaoEnviadaRepository repository;
    private final ComunicacaoEnviadaMapper mapper;

    @Transactional(readOnly = true)
    public Page<ComunicacaoEnviadaResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ComunicacaoEnviadaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ComunicaÃ§Ã£o nÃ£o encontrada com id: " + id));
    }

    public ComunicacaoEnviadaResponse create(ComunicacaoEnviadaRequest request) {
        ComunicacaoEnviada entity = mapper.toEntity(request);
        ComunicacaoEnviada savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public ComunicacaoEnviadaResponse update(Long id, ComunicacaoEnviadaRequest request) {
        ComunicacaoEnviada entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ComunicaÃ§Ã£o nÃ£o encontrada com id: " + id));

        mapper.updateEntity(request, entity);
        ComunicacaoEnviada updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ComunicaÃ§Ã£o nÃ£o encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
