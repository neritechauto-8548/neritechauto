package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.Cargo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    Page<Cargo> findByEmpresaId(Long empresaId, Pageable pageable);
    Optional<Cargo> findByIdAndEmpresaId(Long id, Long empresaId);

    @org.springframework.data.jpa.repository.Query("select c from Cargo c where c.empresaId=:empresaId " +
            "and (:q is null or lower(c.nome) like lower(concat('%',:q,'%')) or lower(c.codigoCbo) like lower(concat('%',:q,'%'))) " +
            "and (:ativo is null or c.ativo=:ativo) and (:areaId is null or c.areaId=:areaId)")
    Page<Cargo> search(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("q") String q,
            @org.springframework.data.repository.query.Param("ativo") Boolean ativo,
            @org.springframework.data.repository.query.Param("areaId") Long areaId,
            Pageable pageable);

    List<Cargo> findByEmpresaIdAndAtivo(Long empresaId, Boolean ativo);

    Optional<Cargo> findByEmpresaIdAndNome(Long empresaId, String nome);

    boolean existsByEmpresaIdAndNome(Long empresaId, String nome);
    boolean existsByEmpresaIdAndNomeIgnoreCase(Long empresaId, String nome);
    boolean existsByEmpresaIdAndNomeIgnoreCaseAndIdNot(Long empresaId, String nome, Long id);
    boolean existsByEmpresaIdAndCodigoCboIgnoreCase(Long empresaId, String codigoCbo);
    boolean existsByEmpresaIdAndCodigoCboIgnoreCaseAndIdNot(Long empresaId, String codigoCbo, Long id);
    long countByEmpresaIdAndAreaId(Long empresaId, Long areaId);
}

