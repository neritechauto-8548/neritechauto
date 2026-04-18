package com.neritech.saas.agendamento.repository;

import com.neritech.saas.agendamento.domain.Reagendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReagendamentoRepository extends JpaRepository<Reagendamento, Long> {
    List<Reagendamento> findByAgendamentoOriginalId(Long agendamentoOriginalId);

    List<Reagendamento> findByAgendamentoNovoId(Long agendamentoNovoId);
}
