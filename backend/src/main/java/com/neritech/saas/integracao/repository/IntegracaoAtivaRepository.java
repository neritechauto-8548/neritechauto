package com.neritech.saas.integracao.repository;

import com.neritech.saas.integracao.domain.IntegracaoAtiva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntegracaoAtivaRepository extends JpaRepository<IntegracaoAtiva, Long> {
    List<IntegracaoAtiva> findByAtivoTrue();

    List<IntegracaoAtiva> findByTipo(String tipo);
}
