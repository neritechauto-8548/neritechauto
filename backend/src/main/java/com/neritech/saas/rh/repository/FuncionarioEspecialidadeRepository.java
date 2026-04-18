package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.FuncionarioEspecialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioEspecialidadeRepository extends JpaRepository<FuncionarioEspecialidade, Long> {

    List<FuncionarioEspecialidade> findByFuncionarioId(Long funcionarioId);

    List<FuncionarioEspecialidade> findByEspecialidadeId(Long especialidadeId);

    List<FuncionarioEspecialidade> findByFuncionarioIdAndAtivo(Long funcionarioId, Boolean ativo);
}
