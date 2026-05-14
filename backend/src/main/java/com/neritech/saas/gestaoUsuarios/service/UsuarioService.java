package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.service.StripeService;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FuncaoRepository funcaoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AssinaturaEmpresaRepository assinaturaEmpresaRepository;
    private final StripeService stripeService;
    private final EmpresaRepository empresaRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> findAll() {
        Long empresaId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        if (empresaId == null) {
            return Collections.emptyList();
        }
        return usuarioRepository.findAllByEmpresaId(empresaId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponse create(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nomeCompleto(request.getNomeCompleto())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .ativo(request.isAtivo())
                .bloqueado(request.isBloqueado())
                .build();

        syncFuncoes(usuario, request.getFuncoesIds());

        usuario = usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findById(Long id) {
        Long empresaId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        return usuarioRepository.findByIdAndEmpresaId(id, empresaId)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou acesso negado"));
    }

    @Transactional
    public UsuarioResponse update(Long id, UsuarioRequest request) {
        Long empresaId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        Usuario usuario = usuarioRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou acesso negado"));

        usuario.setNomeCompleto(request.getNomeCompleto());
        usuario.setEmail(request.getEmail());
        usuario.setAtivo(request.isAtivo());
        usuario.setBloqueado(request.isBloqueado());
        
        if (request.getSenha() != null && !request.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        syncFuncoes(usuario, request.getFuncoesIds());

        usuario = usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    private void syncFuncoes(Usuario usuario, java.util.Set<Long> funcoesIds) {
        if (usuario.getFuncoes() == null) {
            usuario.setFuncoes(new java.util.HashSet<>());
        }
        usuario.getFuncoes().clear();
        if (funcoesIds != null && !funcoesIds.isEmpty()) {
            List<Funcao> funcoesGerenciadas = funcaoRepository.findAllById(funcoesIds);
            usuario.getFuncoes().addAll(funcoesGerenciadas);
        }
    }

    @Transactional
    public void delete(Long id) {
        Long empresaId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        Usuario usuario = usuarioRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou acesso negado"));
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Usuário não autenticado");
        }
        
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
        
        return toResponse(usuario);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        Long empresaId = usuario.getEmpresaId();
        boolean assinaturaAtiva = false;
        String stripeUrl = null;
        Integer planoNivel = 1;

        if (empresaId != null) {
            java.util.Optional<AssinaturaEmpresa> assinaturaOpt = assinaturaEmpresaRepository
                .findFirstByEmpresaIdOrderByDataFimDesc(empresaId);
            
            if (assinaturaOpt.isPresent()) {
                AssinaturaEmpresa assinatura = assinaturaOpt.get();
                com.neritech.saas.empresa.domain.enums.StatusAssinatura status = assinatura.getStatus();
                
                // Validação de período de graça (Grace Period)
                if (status == com.neritech.saas.empresa.domain.enums.StatusAssinatura.ATRASADO && 
                    assinatura.getGracePeriodEndsAt() != null && 
                    assinatura.getGracePeriodEndsAt().isBefore(java.time.LocalDateTime.now())) {
                    status = com.neritech.saas.empresa.domain.enums.StatusAssinatura.SUSPENSO;
                }

                // Validação de assinatura ativa/liberada
                assinaturaAtiva = (status == com.neritech.saas.empresa.domain.enums.StatusAssinatura.ATIVO || 
                                   status == com.neritech.saas.empresa.domain.enums.StatusAssinatura.TESTE ||
                                   "ACTIVE".equals(status.name()) || 
                                   "TRIAL".equals(status.name()));
                
                log.info("Empresa: {} - Status Assinatura: {} - Ativa: {}", empresaId, status, assinaturaAtiva);
                
                planoNivel = (assinatura.getPlano() != null) ? assinatura.getPlano().getNivel() : 1;
                
                UsuarioResponse response = UsuarioResponse.builder()
                        .id(usuario.getId())
                        .empresaId(empresaId)
                        .nomeCompleto(usuario.getNomeCompleto())
                        .email(usuario.getEmail())
                        .ativo(usuario.getAtivo())
                        .bloqueado(usuario.getBloqueado())
                        .ultimoAcesso(usuario.getUltimoAcesso())
                        .cargo(usuario.getPerfil() != null ? usuario.getPerfil().getCargo() : null)
                        .departamento(usuario.getPerfil() != null ? usuario.getPerfil().getDepartamento() : null)
                        .telefone(usuario.getPerfil() != null ? usuario.getPerfil().getTelefone() : null)
                        .avatarUrl(usuario.getPerfil() != null ? usuario.getPerfil().getAvatarUrl() : null)
                        .funcoes(usuario.getFuncoes().stream().map(f -> f.getNome()).collect(java.util.stream.Collectors.toSet()))
                        .funcoesIds(usuario.getFuncoes().stream().map(f -> f.getId()).collect(java.util.stream.Collectors.toSet()))
                        .permissions(usuario.getFuncoes().stream()
                            .flatMap(f -> f.getPermissoes().stream())
                            .map(p -> p.getChave())
                            .collect(java.util.stream.Collectors.toSet()))
                        .assinaturaAtiva(assinaturaAtiva)
                        .subscriptionStatus(status)
                        .planoNivel(planoNivel)
                        .build();

                if (!assinaturaAtiva && stripeService.isConfigured()) {
                    try {
                        String customerId = empresaRepository.findById(empresaId)
                            .map(e -> e.getStripeCustomerId())
                            .orElse(null);
                        
                        if (customerId != null) {
                            response.setStripeUrl(stripeService.createBillingPortalSession(customerId, "https://app.neritechauto.com.br/auth/login"));
                        }
                    } catch (Exception e) {
                        // Log error
                    }
                }
                return response;
            }
        }

        return UsuarioResponse.builder()
                .id(usuario.getId())
                .empresaId(empresaId)
                .nomeCompleto(usuario.getNomeCompleto())
                .email(usuario.getEmail())
                .ativo(usuario.getAtivo())
                .bloqueado(usuario.getBloqueado())
                .ultimoAcesso(usuario.getUltimoAcesso())
                .cargo(usuario.getPerfil() != null ? usuario.getPerfil().getCargo() : null)
                .departamento(usuario.getPerfil() != null ? usuario.getPerfil().getDepartamento() : null)
                .telefone(usuario.getPerfil() != null ? usuario.getPerfil().getTelefone() : null)
                .avatarUrl(usuario.getPerfil() != null ? usuario.getPerfil().getAvatarUrl() : null)
                .funcoes(usuario.getFuncoes() != null 
                    ? usuario.getFuncoes().stream().map(f -> f.getNome()).collect(Collectors.toSet()) 
                    : Collections.emptySet())
                .funcoesIds(usuario.getFuncoes() != null 
                    ? usuario.getFuncoes().stream().map(f -> f.getId()).collect(Collectors.toSet()) 
                    : Collections.emptySet())
                .permissions(usuario.getFuncoes() != null
                    ? usuario.getFuncoes().stream()
                        .flatMap(f -> f.getPermissoes().stream())
                        .map(p -> p.getChave())
                        .collect(Collectors.toSet())
                    : Collections.emptySet())
                .planoNivel(planoNivel)
                .assinaturaAtiva(false)
                .subscriptionStatus(com.neritech.saas.empresa.domain.enums.StatusAssinatura.INATIVO)
                .stripeUrl(stripeUrl)
                .build();
    }
}
