package com.neritech.saas.garantia.repository;

import com.neritech.saas.garantia.domain.Garantia;
import com.neritech.saas.garantia.domain.StatusGarantia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para Garantia
 */
@Repository
public interface GarantiaRepository extends JpaRepository<Garantia, Long> {

    Page<Garantia> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<Garantia> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<Garantia> findByNumeroGarantia(String numeroGarantia);

    List<Garantia> findByEmpresaIdAndStatus(Long empresaId, StatusGarantia status);

    List<Garantia> findByClienteId(Long clienteId);

    List<Garantia> findByVeiculoId(Long veiculoId);

    List<Garantia> findByOrdemServicoId(Long ordemServicoId);

    @Query("SELECT g FROM Garantia g WHERE g.empresaId = :empresaId AND g.status = 'ATIVA' AND g.dataFim < :dataLimite AND g.alertaVencimentoEnviado = false")
    List<Garantia> findGarantiasProximasVencimento(@Param("empresaId") Long empresaId,
            @Param("dataLimite") LocalDate dataLimite);

    @Query("SELECT g FROM Garantia g WHERE g.empresaId = :empresaId AND g.status = 'ATIVA' AND g.dataFim < CURRENT_DATE")
    List<Garantia> findGarantiasExpiradas(@Param("empresaId") Long empresaId);
}

