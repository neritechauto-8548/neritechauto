package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.PlanoConta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanoContaRepository extends JpaRepository<PlanoConta, Long>, JpaSpecificationExecutor<PlanoConta> {
    Page<PlanoConta> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<PlanoConta> findById(Long id);
}

