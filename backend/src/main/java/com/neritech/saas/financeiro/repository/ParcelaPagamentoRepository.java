package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.ParcelaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelaPagamentoRepository
        extends JpaRepository<ParcelaPagamento, Long>, JpaSpecificationExecutor<ParcelaPagamento> {
    List<ParcelaPagamento> findByPagamentoId(Long pagamentoId);
}
