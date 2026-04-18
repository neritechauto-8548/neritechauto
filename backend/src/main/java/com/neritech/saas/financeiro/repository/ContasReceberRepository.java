package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.ContasReceber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContasReceberRepository
        extends JpaRepository<ContasReceber, Long>, JpaSpecificationExecutor<ContasReceber> {
    Page<ContasReceber> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<ContasReceber> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<ContasReceber> findByEmpresaIdAndNumeroTitulo(Long empresaId, String numeroTitulo);
}

