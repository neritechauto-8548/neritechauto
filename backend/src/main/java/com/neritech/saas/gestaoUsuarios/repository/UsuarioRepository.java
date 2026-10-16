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
    @org.springframework.data.jpa.repository.Query("select distinct u from Usuario u left join u.funcoes f " +
            "where u.empresaId=:empresaId and (:ativo is null or u.ativo=:ativo) " +
            "and (:q is null or lower(u.nomeCompleto) like lower(concat('%',:q,'%')) " +
            "or lower(u.email) like lower(concat('%',:q,'%')) or lower(f.nome) like lower(concat('%',:q,'%')))")
    org.springframework.data.domain.Page<Usuario> searchByTenant(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("q") String q,
            @org.springframework.data.repository.query.Param("ativo") Boolean ativo,
            org.springframework.data.domain.Pageable pageable);
    @org.springframework.data.jpa.repository.Query("select count(distinct u.id) from Usuario u join u.funcoes f " +
            "where u.empresaId = :empresaId and u.ativo = true and u.bloqueado = false " +
            "and upper(f.nome) in ('ADMIN','ADMINISTRADOR','SUPER_ADMIN')")
    long countActiveAdmins(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT u.id, u.nomeCompleto FROM Usuario u WHERE u.id IN :ids AND u.empresaId = :empresaId")
    java.util.List<Object[]> findNomesCompletosByIdsAndEmpresaId(@org.springframework.data.repository.query.Param("ids") java.util.Collection<Long> ids, @org.springframework.data.repository.query.Param("empresaId") Long empresaId);
}

