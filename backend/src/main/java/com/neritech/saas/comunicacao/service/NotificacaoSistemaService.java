package com.neritech.saas.comunicacao.service;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.comunicacao.domain.NotificacaoSistema;
import com.neritech.saas.comunicacao.dto.NotificacaoSistemaRequest;
import com.neritech.saas.comunicacao.dto.NotificacaoSistemaResponse;
import com.neritech.saas.comunicacao.mapper.NotificacaoSistemaMapper;
import com.neritech.saas.comunicacao.repository.NotificacaoSistemaRepository;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificacaoSistemaService {
    private final NotificacaoSistemaRepository repository;
    private final NotificacaoSistemaMapper mapper;
    private final UsuarioRepository usuarioRepository;

    private Long currentTenant() {
        Long empresaId = TenantContext.getCurrentTenant();
        if (empresaId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Empresa não identificada");
        }
        return empresaId;
    }

    private void requireTenant(Long empresaId) {
        if (!currentTenant().equals(empresaId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado à empresa informada");
        }
    }

    private Usuario currentUser() {
        Long empresaId = currentTenant();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sessão não autenticada");
        }
        return usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário fora da empresa"));
    }

    @Transactional(readOnly = true)
    public Page<NotificacaoSistemaResponse> findMine(Pageable pageable) {
        Usuario user = currentUser();
        return repository.findByEmpresaIdAndUsuarioDestinatarioId(currentTenant(), user.getId(), pageable)
                .map(mapper::toResponse);
    }

    /** Idempotent: read state is persisted only for the authenticated recipient. */
    public NotificacaoSistemaResponse markMineAsRead(Long id) {
        Usuario user = currentUser();
        NotificacaoSistema notification = repository
                .findByIdAndEmpresaIdAndUsuarioDestinatarioId(id, currentTenant(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));
        if (!Boolean.TRUE.equals(notification.getLida())) {
            notification.setLida(true);
            notification.setDataLeitura(LocalDateTime.now());
            notification = repository.save(notification);
        }
        return mapper.toResponse(notification);
    }

    @Transactional(readOnly = true)
    public Page<NotificacaoSistemaResponse> findAll(Long empresaId, Pageable pageable) {
        requireTenant(empresaId);
        return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificacaoSistemaResponse> findByUsuarioDestinatario(Long usuarioId, Pageable pageable) {
        return repository.findByEmpresaIdAndUsuarioDestinatarioId(currentTenant(), usuarioId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public NotificacaoSistemaResponse findById(Long id) {
        return repository.findByIdAndEmpresaId(id, currentTenant())
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));
    }

    public NotificacaoSistemaResponse create(NotificacaoSistemaRequest request) {
        requireTenant(request.empresaId());
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    public NotificacaoSistemaResponse update(Long id, NotificacaoSistemaRequest request) {
        requireTenant(request.empresaId());
        NotificacaoSistema entity = repository.findByIdAndEmpresaId(id, currentTenant())
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        NotificacaoSistema entity = repository.findByIdAndEmpresaId(id, currentTenant())
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));
        repository.delete(entity);
    }
}
