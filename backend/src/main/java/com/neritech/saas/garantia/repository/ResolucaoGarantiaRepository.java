package com.neritech.saas.garantia.repository;

import com.neritech.saas.garantia.domain.ResolucaoGarantia;
import com.neritech.saas.garantia.domain.TipoResolucao;
import com.neritech.saas.garantia.domain.QualidadeResolucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para ResolucaoGarantia
 */
@Repository
public interface ResolucaoGarantiaRepository extends JpaRepository<ResolucaoGarantia, Long> {

    Optional<ResolucaoGarantia> findByReclamacaoId(Long reclamacaoId);

    List<ResolucaoGarantia> findByTipoResolucao(TipoResolucao tipoResolucao);

    List<ResolucaoGarantia> findByQualidadeResolucao(QualidadeResolucao qualidadeResolucao);

    List<ResolucaoGarantia> findByFuncionarioExecutorId(Long funcionarioId);

    List<ResolucaoGarantia> findByNovaGarantiaGerada(Boolean novaGarantiaGerada);

    List<ResolucaoGarantia> findByAprovadaGerencia(Boolean aprovadaGerencia);
}
