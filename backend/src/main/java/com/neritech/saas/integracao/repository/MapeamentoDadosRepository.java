package com.neritech.saas.integracao.repository;

import com.neritech.saas.integracao.domain.MapeamentoDados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapeamentoDadosRepository extends JpaRepository<MapeamentoDados, Long> {
    List<MapeamentoDados> findByIntegracaoId(Long integracaoId);

    List<MapeamentoDados> findByEntidade(String entidade);

    List<MapeamentoDados> findByAtivoTrue();
}
