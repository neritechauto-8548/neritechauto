package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.Pagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>, JpaSpecificationExecutor<Pagamento> {
    Page<Pagamento> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<Pagamento> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<Pagamento> findByEmpresaIdAndNumeroPagamento(Long empresaId, String numeroPagamento);

    Page<Pagamento> findByEmpresaIdAndFatura_Id(Long empresaId, Long faturaId, Pageable pageable);
}

