package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.FormaPagamento;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FormaPagamentoRepository
        extends JpaRepository<FormaPagamento, Long>, JpaSpecificationExecutor<FormaPagamento> {
    Page<FormaPagamento> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<FormaPagamento> findByIdAndEmpresaId(Long id, Long empresaId);
}

