package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.Fatura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long>, JpaSpecificationExecutor<Fatura> {
    Page<Fatura> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<Fatura> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<Fatura> findByEmpresaIdAndNumeroFatura(Long empresaId, String numeroFatura);

    Optional<Fatura> findByOrdemServicoIdAndEmpresaId(Long ordemServicoId, Long empresaId);
}

