package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.CentroCusto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CentroCustoRepository extends JpaRepository<CentroCusto, Long>, JpaSpecificationExecutor<CentroCusto> {
    Page<CentroCusto> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<CentroCusto> findById(Long id);
}

