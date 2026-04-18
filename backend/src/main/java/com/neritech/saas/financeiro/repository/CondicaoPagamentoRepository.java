package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.CondicaoPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CondicaoPagamentoRepository
        extends JpaRepository<CondicaoPagamento, Long>, JpaSpecificationExecutor<CondicaoPagamento> {
    Page<CondicaoPagamento> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<CondicaoPagamento> findByIdAndEmpresaId(Long id, Long empresaId);
}

