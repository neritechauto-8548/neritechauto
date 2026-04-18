package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.ContasPagar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContasPagarRepository extends JpaRepository<ContasPagar, Long>, JpaSpecificationExecutor<ContasPagar> {
    Page<ContasPagar> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<ContasPagar> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<ContasPagar> findByEmpresaIdAndNumeroTitulo(Long empresaId, String numeroTitulo);
}

