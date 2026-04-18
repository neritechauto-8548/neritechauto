package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.EscalaTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EscalaTrabalhoRepository extends JpaRepository<EscalaTrabalho, Long> {

    List<EscalaTrabalho> findByEmpresaId(Long empresaId);

    List<EscalaTrabalho> findByFuncionarioId(Long funcionarioId);

    List<EscalaTrabalho> findByDataInicioBetween(LocalDate inicio, LocalDate fim);

    List<EscalaTrabalho> findByFuncionarioIdAndAtivo(Long funcionarioId, Boolean ativo);
}

