package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.FluxoCaixa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FluxoCaixaRepository extends JpaRepository<FluxoCaixa, Long>, JpaSpecificationExecutor<FluxoCaixa> {
    Page<FluxoCaixa> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<FluxoCaixa> findByIdAndEmpresaId(Long id, Long empresaId);
}

