package com.neritech.saas.agendamento.repository;

import com.neritech.saas.agendamento.domain.BloqueioAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BloqueioAgendaRepository extends JpaRepository<BloqueioAgenda, Long> {
    List<BloqueioAgenda> findByEmpresaId(Long empresaId);

    List<BloqueioAgenda> findByFuncionarioId(Long funcionarioId);

    List<BloqueioAgenda> findByEmpresaIdAndAtivoTrue(Long empresaId);

    @Query("SELECT b FROM BloqueioAgenda b WHERE b.funcionarioId = :funcionarioId AND b.ativo = true AND :data BETWEEN b.dataInicio AND b.dataFim")
    List<BloqueioAgenda> findBloqueiosAtivosPorFuncionarioEData(@Param("funcionarioId") Long funcionarioId,
            @Param("data") LocalDate data);
}

