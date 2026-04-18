package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.NotificacaoSistema;
import com.neritech.saas.comunicacao.dto.NotificacaoSistemaRequest;
import com.neritech.saas.comunicacao.dto.NotificacaoSistemaResponse;
import com.neritech.saas.comunicacao.mapper.NotificacaoSistemaMapper;
import com.neritech.saas.comunicacao.repository.NotificacaoSistemaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificacaoSistemaService {

    private final NotificacaoSistemaRepository repository;
    private final NotificacaoSistemaMapper mapper;

    @Transactional(readOnly = true)
    public Page<NotificacaoSistemaResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificacaoSistemaResponse> findByUsuarioDestinatario(Long usuarioId, Pageable pageable) {
        return repository.findByUsuarioDestinatarioId(usuarioId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public NotificacaoSistemaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("NotificaÃ§Ã£o nÃ£o encontrada com id: " + id));
    }

    public NotificacaoSistemaResponse create(NotificacaoSistemaRequest request) {
        NotificacaoSistema entity = mapper.toEntity(request);
        NotificacaoSistema savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public NotificacaoSistemaResponse update(Long id, NotificacaoSistemaRequest request) {
        NotificacaoSistema entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NotificaÃ§Ã£o nÃ£o encontrada com id: " + id));

        mapper.updateEntity(request, entity);
        NotificacaoSistema updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("NotificaÃ§Ã£o nÃ£o encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
