package com.neritech.saas.agendamento.repository;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("select a from Agendamento a where a.id = :id and a.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Optional<Agendamento> findByIdScoped(@Param("id") Long id);

    @Query("select a from Agendamento a where a.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()} order by a.dataAgendamento, a.horaInicio")
    List<Agendamento> findAllScoped();

    List<Agendamento> findByEmpresaId(Long empresaId);

    boolean existsByNumeroAgendamento(String numeroAgendamento);

    @Query("select a from Agendamento a where a.numeroAgendamento = :numero and a.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Optional<Agendamento> findByNumeroAgendamentoScoped(@Param("numero") String numeroAgendamento);

    @Query("select a from Agendamento a where a.clienteId = :clienteId and a.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    List<Agendamento> findByClienteIdScoped(@Param("clienteId") Long clienteId);

    @Query("select a from Agendamento a where a.veiculoId = :veiculoId and a.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    List<Agendamento> findByVeiculoIdScoped(@Param("veiculoId") Long veiculoId);

    @Query("select a from Agendamento a where a.status = :status and a.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    List<Agendamento> findByStatus(@Param("status") StatusAgendamento status);

    @Query("select a from Agendamento a where a.dataAgendamento = :data and a.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    List<Agendamento> findByDataAgendamento(@Param("data") LocalDate dataAgendamento);

    @Query("select a from Agendamento a where a.mecanicoAlocadoId = :mecanicoId and a.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    List<Agendamento> findByMecanicoAlocadoId(@Param("mecanicoId") Long mecanicoId);

    @Query("SELECT a FROM Agendamento a WHERE a.empresaId = :empresaId AND a.dataAgendamento BETWEEN :dataInicio AND :dataFim")
    List<Agendamento> findByEmpresaAndPeriodo(@Param("empresaId") Long empresaId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);

    @Query("SELECT a FROM Agendamento a WHERE a.status = :status AND a.dataAgendamento = :data AND a.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    List<Agendamento> findByStatusAndData(@Param("status") StatusAgendamento status,
            @Param("data") LocalDate data);

    @Query("SELECT a FROM Agendamento a WHERE a.empresaId = :empresaId AND a.confirmadoCliente = false AND a.dataAgendamento >= :dataAtual")
    List<Agendamento> findAgendamentosNaoConfirmados(@Param("empresaId") Long empresaId,
            @Param("dataAtual") LocalDate dataAtual);

    @Query("SELECT a FROM Agendamento a WHERE a.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()} " +
           "AND a.veiculoId = :veiculoId " +
           "AND a.dataAgendamento = :dataAgendamento " +
           "AND a.status <> com.neritech.saas.agendamento.domain.enums.StatusAgendamento.CANCELADO " +
           "AND (:id IS NULL OR a.id <> :id) " +
           "AND a.horaInicio < :horaFim AND a.horaFim > :horaInicio")
    List<Agendamento> findConflictingAgendamentos(@Param("veiculoId") Long veiculoId,
            @Param("dataAgendamento") LocalDate dataAgendamento,
            @Param("horaInicio") java.time.LocalTime horaInicio,
            @Param("horaFim") java.time.LocalTime horaFim,
            @Param("id") Long id);
}
