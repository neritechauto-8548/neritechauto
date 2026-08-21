package com.neritech.saas.agendamento.repository;

import com.neritech.saas.agendamento.domain.TipoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoAgendamentoRepository extends JpaRepository<TipoAgendamento, Long> {
    List<TipoAgendamento> findByEmpresaId(Long empresaId);

    List<TipoAgendamento> findByEmpresaIdAndAtivoTrue(Long empresaId);

    Optional<TipoAgendamento> findByIdAndEmpresaId(Long id, Long empresaId);
}
