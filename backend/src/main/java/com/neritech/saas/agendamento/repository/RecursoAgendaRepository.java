package com.neritech.saas.agendamento.repository;

import com.neritech.saas.agendamento.domain.RecursoAgenda;
import com.neritech.saas.agendamento.domain.enums.TipoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface RecursoAgendaRepository extends JpaRepository<RecursoAgenda, Long> {
    @Query("SELECT r FROM RecursoAgenda r WHERE r.empresaId = :empresaId")
    List<RecursoAgenda> findByEmpresaId(@Param("empresaId") Long empresaId);

    List<RecursoAgenda> findByTipoRecurso(TipoRecurso tipoRecurso);

    @Query("SELECT r FROM RecursoAgenda r WHERE r.empresaId = :empresaId AND r.disponivel = true")
    List<RecursoAgenda> findByEmpresaIdAndDisponivelTrue(@Param("empresaId") Long empresaId);
}

