package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.Situacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SituacaoRepository extends JpaRepository<Situacao, Long>, JpaSpecificationExecutor<Situacao> {
    List<Situacao> findByEmpresaId(Long empresaId);
}

