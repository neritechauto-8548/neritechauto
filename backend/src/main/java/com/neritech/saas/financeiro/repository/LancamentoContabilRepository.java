package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.LancamentoContabil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LancamentoContabilRepository
        extends JpaRepository<LancamentoContabil, Long>, JpaSpecificationExecutor<LancamentoContabil> {
    Page<LancamentoContabil> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<LancamentoContabil> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<LancamentoContabil> findByEmpresaIdAndNumeroLancamento(Long empresaId, String numeroLancamento);
}

