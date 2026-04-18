package com.neritech.saas.garantia.repository;

import com.neritech.saas.comunicacao.domain.enums.Prioridade;
import com.neritech.saas.garantia.domain.PrioridadeReclamacao;
import com.neritech.saas.garantia.domain.ReclamacaoGarantia;
import com.neritech.saas.garantia.domain.StatusReclamacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository para ReclamacaoGarantia
 */
@Repository
public interface ReclamacaoGarantiaRepository extends JpaRepository<ReclamacaoGarantia, Long> {

    Optional<ReclamacaoGarantia> findByNumeroReclamacao(String numeroReclamacao);

    List<ReclamacaoGarantia> findByGarantiaId(Long garantiaId);

    Page<ReclamacaoGarantia> findByGarantiaEmpresaId(Long empresaId, Pageable pageable);

    List<ReclamacaoGarantia> findByStatus(StatusReclamacao status);

    List<ReclamacaoGarantia> findByGarantiaEmpresaIdAndStatus(Long empresaId, StatusReclamacao status);

    List<ReclamacaoGarantia> findByPrioridade(PrioridadeReclamacao prioridade);

    List<ReclamacaoGarantia> findByTecnicoResponsavelId(Long tecnicoId);

    @Query("SELECT r FROM ReclamacaoGarantia r WHERE r.garantia.empresaId = :empresaId AND r.status IN ('ABERTA', 'EM_ANALISE', 'EM_EXECUCAO')")
    List<ReclamacaoGarantia> findReclamacoesAbertas(@Param("empresaId") Long empresaId);

    @Query("SELECT r FROM ReclamacaoGarantia r WHERE r.garantia.empresaId = :empresaId AND r.prioridade = 'URGENTE' AND r.status IN ('ABERTA', 'EM_ANALISE')")
    List<ReclamacaoGarantia> findReclamacoesUrgentes(@Param("empresaId") Long empresaId);
}
