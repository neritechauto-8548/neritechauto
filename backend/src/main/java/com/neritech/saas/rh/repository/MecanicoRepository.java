package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {

    Optional<Mecanico> findByFuncionarioId(Long funcionarioId);

    Optional<Mecanico> findByCodigoMecanico(String codigoMecanico);

    List<Mecanico> findByAtivoExecucao(Boolean ativoExecucao);

    boolean existsByCodigoMecanico(String codigoMecanico);
}
