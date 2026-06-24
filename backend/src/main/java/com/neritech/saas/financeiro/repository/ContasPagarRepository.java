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
    java.util.List<ContasPagar> findByEmpresaId(Long empresaId);
    java.util.List<ContasPagar> findByEmpresaIdAndDataVencimentoBetween(Long empresaId, java.time.LocalDate inicio, java.time.LocalDate fim);

    Optional<ContasPagar> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<ContasPagar> findByEmpresaIdAndNumeroTitulo(Long empresaId, String numeroTitulo);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(c.valorPago) FROM ContasPagar c WHERE c.empresaId = :empresaId AND c.dataVencimento BETWEEN :inicio AND :fim")
    java.math.BigDecimal calculateDespesasMes(@org.springframework.data.repository.query.Param("empresaId") Long empresaId, @org.springframework.data.repository.query.Param("inicio") java.time.LocalDate inicio, @org.springframework.data.repository.query.Param("fim") java.time.LocalDate fim);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(c.valorPendente) FROM ContasPagar c WHERE c.empresaId = :empresaId AND c.status <> 'PAGO' AND c.status <> 'CANCELADO'")
    java.math.BigDecimal calculateTotalPendentes(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT c FROM ContasPagar c WHERE c.id IN :ids AND c.empresaId = :empresaId")
    java.util.List<ContasPagar> findAllByIdsAndEmpresaId(@org.springframework.data.repository.query.Param("ids") java.util.Collection<Long> ids, @org.springframework.data.repository.query.Param("empresaId") Long empresaId);
}

