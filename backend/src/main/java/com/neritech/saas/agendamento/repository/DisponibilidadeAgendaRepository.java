package com.neritech.saas.agendamento.repository;

import com.neritech.saas.agendamento.domain.DisponibilidadeAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisponibilidadeAgendaRepository extends JpaRepository<DisponibilidadeAgenda, Long> {
    List<DisponibilidadeAgenda> findByEmpresaId(Long empresaId);

    List<DisponibilidadeAgenda> findByFuncionarioId(Long funcionarioId);

    List<DisponibilidadeAgenda> findByFuncionarioIdAndDataDisponibilidade(Long funcionarioId, LocalDate data);

    List<DisponibilidadeAgenda> findByDiaSemana(Integer diaSemana);
}

