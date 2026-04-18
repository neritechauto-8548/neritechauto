package com.neritech.saas.security;

import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        Set<GrantedAuthority> authorities = new HashSet<>();

        // Add Roles
        usuario.getFuncoes().forEach(funcao -> {
            if (funcao.getAtivo()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + funcao.getNome()));
                
                // Add Permissions
                funcao.getPermissoes().forEach(permissao -> {
                    authorities.add(new SimpleGrantedAuthority(permissao.getNome()));
                });
            }
        });

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .disabled(!usuario.getAtivo())
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(usuario.getBloqueado())
                .authorities(authorities)
                .build();
    }
}
