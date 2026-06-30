package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    
    boolean existsByEmailIgnoreCaseAndEmpresaId(String email, Long empresaId);
    Optional<Usuario> findByEmailIgnoreCaseAndEmpresaId(String email, Long empresaId);
    Optional<Usuario> findByIdAndEmpresaId(Long id, Long empresaId);
    java.util.List<Usuario> findAllByEmpresaId(Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT u.id, u.nomeCompleto FROM Usuario u WHERE u.id IN :ids AND u.empresaId = :empresaId")
    java.util.List<Object[]> findNomesCompletosByIdsAndEmpresaId(@org.springframework.data.repository.query.Param("ids") java.util.Collection<Long> ids, @org.springframework.data.repository.query.Param("empresaId") Long empresaId);
}

