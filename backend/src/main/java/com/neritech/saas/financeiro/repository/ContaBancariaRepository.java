package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.ContaBancaria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaBancariaRepository
        extends JpaRepository<ContaBancaria, Long>, JpaSpecificationExecutor<ContaBancaria> {
    Page<ContaBancaria> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<ContaBancaria> findByIdAndEmpresaId(Long id, Long empresaId);
}

