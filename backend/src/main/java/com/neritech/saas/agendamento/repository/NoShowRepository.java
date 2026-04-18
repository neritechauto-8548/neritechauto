package com.neritech.saas.agendamento.repository;

import com.neritech.saas.agendamento.domain.NoShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoShowRepository extends JpaRepository<NoShow, Long> {
    List<NoShow> findByAgendamentoId(Long agendamentoId);

    List<NoShow> findByClienteId(Long clienteId);
}
