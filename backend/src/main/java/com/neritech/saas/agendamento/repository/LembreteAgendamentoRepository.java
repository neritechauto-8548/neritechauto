package com.neritech.saas.agendamento.repository;

import com.neritech.saas.agendamento.domain.LembreteAgendamento;
import com.neritech.saas.agendamento.domain.enums.StatusEnvioLembrete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LembreteAgendamentoRepository extends JpaRepository<LembreteAgendamento, Long> {
    List<LembreteAgendamento> findByAgendamentoId(Long agendamentoId);

    List<LembreteAgendamento> findByStatusEnvio(StatusEnvioLembrete statusEnvio);
}
