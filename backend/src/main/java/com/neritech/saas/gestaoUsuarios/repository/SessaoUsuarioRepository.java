package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.gestaoUsuarios.domain.SessaoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessaoUsuarioRepository extends JpaRepository<SessaoUsuario, Long> {
    Optional<SessaoUsuario> findByTokenSessao(String tokenSessao);
    Optional<SessaoUsuario> findByRefreshToken(String refreshToken);
    List<SessaoUsuario> findByUsuarioIdAndAtivoTrue(Long usuarioId);
}
