package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    
    boolean existsByEmailAndEmpresaId(String email, Long empresaId);
    Optional<Usuario> findByEmailAndEmpresaId(String email, Long empresaId);
    Optional<Usuario> findByIdAndEmpresaId(Long id, Long empresaId);
}
