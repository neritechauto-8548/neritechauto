package com.neritech.saas.integracao.repository;

import com.neritech.saas.integracao.domain.Sincronizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SincronizacaoRepository extends JpaRepository<Sincronizacao, Long> {
    List<Sincronizacao> findByIntegracaoId(Long integracaoId);

    List<Sincronizacao> findByStatus(String status);

    List<Sincronizacao> findByEntidade(String entidade);
}
