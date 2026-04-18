package com.neritech.saas.integracao.repository;

import com.neritech.saas.integracao.domain.LogIntegracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogIntegracaoRepository extends JpaRepository<LogIntegracao, Long> {
    List<LogIntegracao> findByIntegracaoId(Long integracaoId);

    List<LogIntegracao> findByStatus(String status);

    List<LogIntegracao> findByDataExecucaoBetween(LocalDateTime inicio, LocalDateTime fim);
}
