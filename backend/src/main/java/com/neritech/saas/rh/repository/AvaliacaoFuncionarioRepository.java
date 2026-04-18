package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.AvaliacaoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoFuncionarioRepository extends JpaRepository<AvaliacaoFuncionario, Long> {

    List<AvaliacaoFuncionario> findByFuncionarioId(Long funcionarioId);

    List<AvaliacaoFuncionario> findByAvaliadorId(Long avaliadorId);

    List<AvaliacaoFuncionario> findByPeriodoAvaliado(String periodoAvaliado);

    List<AvaliacaoFuncionario> findByFuncionarioIdOrderByDataAvaliacaoDesc(Long funcionarioId);
}
