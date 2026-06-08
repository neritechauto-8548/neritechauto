package com.neritech.saas.rh.service;

import com.neritech.saas.gestaoUsuarios.domain.PerfilUsuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioPerfilHelper {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void updateAvatar(Long usuarioId, Long empresaId, String avatarUrl) {
        Long previousTenant = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        com.neritech.saas.common.tenancy.TenantContext.setCurrentTenant(empresaId);
        try {
            usuarioRepository.findById(usuarioId).ifPresent(usuario -> {
                if (usuario.getPerfil() != null) {
                    usuario.getPerfil().setAvatarUrl(avatarUrl);
                } else {
                    PerfilUsuario perfil = new PerfilUsuario();
                    perfil.setUsuario(usuario);
                    perfil.setAvatarUrl(avatarUrl);
                    perfil.setEmpresaId(empresaId);
                    usuario.setPerfil(perfil);
                }
                usuarioRepository.saveAndFlush(usuario);
            });
        } finally {
            com.neritech.saas.common.tenancy.TenantContext.setCurrentTenant(previousTenant);
        }
    }
}
