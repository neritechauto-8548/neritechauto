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

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        Set<GrantedAuthority> authorities = new HashSet<>();

        if (usuario.getFuncoes() != null) {
            usuario.getFuncoes().stream()
                    .filter(funcao -> Boolean.TRUE.equals(funcao.getAtivo()))
                    .forEach(funcao -> {
                        String nomeFuncao = funcao.getNome();
                        if (nomeFuncao != null && !nomeFuncao.isBlank()) {
                            // A função persistida é uma autoridade explícita. Não existe promoção
                            // implícita baseada em "contains ADMIN".
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + nomeFuncao));
                        }

                        if (funcao.getPermissoes() != null) {
                            funcao.getPermissoes().stream()
                                    .filter(permissao -> permissao.getValor() != null && !permissao.getValor().isBlank())
                                    .forEach(permissao -> authorities.add(
                                            new SimpleGrantedAuthority(permissao.getValor())));
                        }
                    });
        }

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .disabled(!Boolean.TRUE.equals(usuario.getAtivo()))
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(Boolean.TRUE.equals(usuario.getBloqueado()))
                .authorities(authorities)
                .build();
    }
}
