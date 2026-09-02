package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.domain.OSComment;
import com.neritech.saas.ordemservico.dto.OSCommentCreateRequest;
import com.neritech.saas.ordemservico.dto.OSCommentResponse;
import com.neritech.saas.ordemservico.repository.OSCommentRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OSCommentService {

    private final OSCommentRepository repository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final UsuarioRepository usuarioRepository;

    public OSCommentService(
            OSCommentRepository repository,
            OrdemServicoRepository ordemServicoRepository,
            UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.ordemServicoRepository = ordemServicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<OSCommentResponse> list(Long ordemServicoId) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        requireOwnedOrder(ordemServicoId, tenantId);
        return repository.findByEmpresaIdAndOrdemServicoIdOrderByDataCadastroDescIdDesc(tenantId, ordemServicoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public OSCommentResponse create(Long ordemServicoId, OSCommentCreateRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        requireOwnedOrder(ordemServicoId, tenantId);
        Usuario actor = requireAuthenticatedUser(tenantId);
        String content = normalizeContent(request.content());

        OSComment comment = new OSComment();
        comment.setEmpresaId(tenantId);
        comment.setOrdemServicoId(ordemServicoId);
        comment.setAuthorUserId(actor.getId());
        comment.setAuthorNameSnapshot(normalizeActorName(actor));
        comment.setContent(content);
        comment.setVisibility("INTERNAL");
        comment.setCriadoPor(actor.getId());

        return toResponse(repository.save(comment));
    }

    private void requireOwnedOrder(Long ordemServicoId, Long tenantId) {
        ordemServicoRepository.findByIdAndEmpresaId(ordemServicoId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ordem de serviço não encontrada para a empresa autenticada"));
    }

    private Usuario requireAuthenticatedUser(Long tenantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken
                || authentication.getName() == null
                || authentication.getName().isBlank()) {
            throw new AccessDeniedException("Usuário autenticado não disponível para comentar na OS.");
        }

        return usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(authentication.getName(), tenantId)
                .orElseThrow(() -> new AccessDeniedException(
                        "Usuário autenticado não pertence à empresa da sessão."));
    }

    private String normalizeContent(String raw) {
        String content = raw == null ? "" : raw.trim();
        if (content.isBlank()) {
            throw new IllegalArgumentException("O comentário não pode ficar vazio.");
        }
        return content;
    }

    private String normalizeActorName(Usuario actor) {
        String name = actor.getNomeCompleto();
        if (name == null || name.isBlank()) {
            return "Usuário #" + actor.getId();
        }
        return name.trim();
    }

    private OSCommentResponse toResponse(OSComment comment) {
        return new OSCommentResponse(
                comment.getId(),
                comment.getOrdemServicoId(),
                comment.getAuthorUserId(),
                comment.getAuthorNameSnapshot(),
                comment.getContent(),
                comment.getVisibility(),
                comment.getDataCadastro());
    }
}
