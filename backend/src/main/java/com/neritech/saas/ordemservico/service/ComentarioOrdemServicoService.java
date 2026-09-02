package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.domain.ComentarioOrdemServico;
import com.neritech.saas.ordemservico.dto.ComentarioOrdemServicoCriacaoRequest;
import com.neritech.saas.ordemservico.dto.ComentarioOrdemServicoResposta;
import com.neritech.saas.ordemservico.repository.ComentarioOrdemServicoRepository;
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
public class ComentarioOrdemServicoService {

    private final ComentarioOrdemServicoRepository repositorio;
    private final OrdemServicoRepository ordemServicoRepository;
    private final UsuarioRepository usuarioRepository;

    public ComentarioOrdemServicoService(
            ComentarioOrdemServicoRepository repositorio,
            OrdemServicoRepository ordemServicoRepository,
            UsuarioRepository usuarioRepository) {
        this.repositorio = repositorio;
        this.ordemServicoRepository = ordemServicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<ComentarioOrdemServicoResposta> listar(Long ordemServicoId) {
        Long empresaId = TenantAccess.requireCurrentTenant();
        exigirOrdemDaEmpresa(ordemServicoId, empresaId);
        return repositorio.findByEmpresaIdAndOrdemServicoIdOrderByDataCadastroDescIdDesc(empresaId, ordemServicoId)
                .stream()
                .map(this::paraResposta)
                .toList();
    }

    @Transactional
    public ComentarioOrdemServicoResposta criar(
            Long ordemServicoId,
            ComentarioOrdemServicoCriacaoRequest requisicao) {
        Long empresaId = TenantAccess.requireCurrentTenant();
        exigirOrdemDaEmpresa(ordemServicoId, empresaId);
        Usuario usuarioAutor = obterUsuarioAutenticado(empresaId);
        String conteudo = normalizarConteudo(requisicao.conteudo());

        ComentarioOrdemServico comentario = new ComentarioOrdemServico();
        comentario.setEmpresaId(empresaId);
        comentario.setOrdemServicoId(ordemServicoId);
        comentario.setUsuarioAutorId(usuarioAutor.getId());
        comentario.setNomeAutorRegistrado(normalizarNomeAutor(usuarioAutor));
        comentario.setConteudo(conteudo);
        comentario.setVisibilidade("INTERNO");
        comentario.setCriadoPor(usuarioAutor.getId());

        return paraResposta(repositorio.save(comentario));
    }

    private void exigirOrdemDaEmpresa(Long ordemServicoId, Long empresaId) {
        ordemServicoRepository.findByIdAndEmpresaId(ordemServicoId, empresaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ordem de serviço não encontrada para a empresa autenticada"));
    }

    private Usuario obterUsuarioAutenticado(Long empresaId) {
        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null
                || !autenticacao.isAuthenticated()
                || autenticacao instanceof AnonymousAuthenticationToken
                || autenticacao.getName() == null
                || autenticacao.getName().isBlank()) {
            throw new AccessDeniedException("Usuário autenticado não disponível para comentar na OS.");
        }

        return usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(autenticacao.getName(), empresaId)
                .orElseThrow(() -> new AccessDeniedException(
                        "Usuário autenticado não pertence à empresa da sessão."));
    }

    private String normalizarConteudo(String conteudoInformado) {
        String conteudo = conteudoInformado == null ? "" : conteudoInformado.trim();
        if (conteudo.isBlank()) {
            throw new IllegalArgumentException("O comentário não pode ficar vazio.");
        }
        return conteudo;
    }

    private String normalizarNomeAutor(Usuario usuarioAutor) {
        String nome = usuarioAutor.getNomeCompleto();
        if (nome == null || nome.isBlank()) {
            return "Usuário #" + usuarioAutor.getId();
        }
        return nome.trim();
    }

    private ComentarioOrdemServicoResposta paraResposta(ComentarioOrdemServico comentario) {
        return new ComentarioOrdemServicoResposta(
                comentario.getId(),
                comentario.getOrdemServicoId(),
                comentario.getUsuarioAutorId(),
                comentario.getNomeAutorRegistrado(),
                comentario.getConteudo(),
                comentario.getVisibilidade(),
                comentario.getDataCadastro());
    }
}
