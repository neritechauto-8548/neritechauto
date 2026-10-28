package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.DepartamentoContabio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface DepartamentoContabioRepository extends JpaRepository<DepartamentoContabio, Long>, JpaSpecificationExecutor<DepartamentoContabio> {
    List<DepartamentoContabio> findByEmpresaId(Long empresaId);
    Page<DepartamentoContabio> findByEmpresaId(Long empresaId, Pageable pageable);
    boolean existsByEmpresaIdAndDescricaoIgnoreCase(Long empresaId, String descricao);
    boolean existsByEmpresaIdAndDescricaoIgnoreCaseAndIdNot(Long empresaId, String descricao, Long id);
    boolean existsByEmpresaIdAndCodigoIgnoreCase(Long empresaId, String codigo);
    boolean existsByEmpresaIdAndCodigoIgnoreCaseAndIdNot(Long empresaId, String codigo, Long id);
    java.util.Optional<DepartamentoContabio> findByIdAndEmpresaId(Long id, Long empresaId);

    @org.springframework.data.jpa.repository.Query("select d from DepartamentoContabio d where d.empresa.id=:empresaId " +
            "and (:q is null or lower(d.descricao) like lower(concat('%',:q,'%')) or lower(d.codigo) like lower(concat('%',:q,'%'))) " +
            "and (:ativo is null or d.ativo=:ativo) and (:tipo is null or d.tipoOperacao=:tipo)")
    Page<DepartamentoContabio> search(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("q") String q,
            @org.springframework.data.repository.query.Param("ativo") Boolean ativo,
            @org.springframework.data.repository.query.Param("tipo") String tipo, Pageable pageable);
}

