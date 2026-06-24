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

/**
 * Repository para Agendamento
 */
@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByEmpresaId(Long empresaId);

    Optional<Agendamento> findByNumeroAgendamento(String numeroAgendamento);

    List<Agendamento> findByClienteId(Long clienteId);

    List<Agendamento> findByVeiculoId(Long veiculoId);

    List<Agendamento> findByStatus(StatusAgendamento status);

    List<Agendamento> findByDataAgendamento(LocalDate dataAgendamento);

    List<Agendamento> findByMecanicoAlocadoId(Long mecanicoId);

    @Query("SELECT a FROM Agendamento a WHERE a.empresaId = :empresaId AND a.dataAgendamento BETWEEN :dataInicio AND :dataFim")
    List<Agendamento> findByEmpresaAndPeriodo(@Param("empresaId") Long empresaId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);

    @Query("SELECT a FROM Agendamento a WHERE a.status = :status AND a.dataAgendamento = :data")
    List<Agendamento> findByStatusAndData(@Param("status") StatusAgendamento status,
            @Param("data") LocalDate data);

    @Query("SELECT a FROM Agendamento a WHERE a.empresaId = :empresaId AND a.confirmadoCliente = false AND a.dataAgendamento >= :dataAtual")
    List<Agendamento> findAgendamentosNaoConfirmados(@Param("empresaId") Long empresaId,
            @Param("dataAtual") LocalDate dataAtual);

    @Query("SELECT a FROM Agendamento a WHERE a.veiculoId = :veiculoId " +
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

