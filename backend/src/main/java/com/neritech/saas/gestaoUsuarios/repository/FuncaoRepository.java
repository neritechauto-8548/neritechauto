package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncaoRepository extends JpaRepository<Funcao, Long> {
    Optional<Funcao> findByNome(String nome);
    
    List<Funcao> findAllByEmpresaId(Long empresaId);
    Optional<Funcao> findByNomeAndEmpresaId(String nome, Long empresaId);
    Optional<Funcao> findByIdAndEmpresaId(Long id, Long empresaId);
    boolean existsByEmpresaIdAndNomeIgnoreCase(Long empresaId, String nome);
    boolean existsByEmpresaIdAndNomeIgnoreCaseAndIdNot(Long empresaId, String nome, Long id);

    @org.springframework.data.jpa.repository.Query("select f from Funcao f where f.empresaId=:empresaId " +
            "and (:q is null or lower(f.nome) like lower(concat('%',:q,'%')) or lower(f.descricao) like lower(concat('%',:q,'%'))) " +
            "and (:ativo is null or f.ativo=:ativo)")
    Page<Funcao> search(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("q") String q,
            @org.springframework.data.repository.query.Param("ativo") Boolean ativo, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("select count(distinct u.id) from Usuario u join u.funcoes f where u.empresaId=:empresaId and f.id=:funcaoId")
    long countUsers(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("funcaoId") Long funcaoId);

    @org.springframework.data.jpa.repository.Query("select count(distinct u.id) from Usuario u join u.funcoes f where u.empresaId=:empresaId " +
            "and u.ativo=true and u.bloqueado=false and upper(f.nome) in ('ADMIN','ADMINISTRADOR','SUPER_ADMIN') and f.ativo=true and f.id<>:funcaoId")
    long countAdminsOutsideRole(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("funcaoId") Long funcaoId);
}
