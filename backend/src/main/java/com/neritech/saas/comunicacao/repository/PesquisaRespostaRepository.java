package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.PesquisaResposta;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PesquisaRespostaRepository extends JpaRepository<PesquisaResposta, Long> {
    Optional<PesquisaResposta> findByEmpresaIdAndChaveIdempotencia(Long empresaId, String chaveIdempotencia);
    Page<PesquisaResposta> findByQuestionarioIdAndEmpresaId(Long questionarioId, Long empresaId, Pageable pageable);
    long countByQuestionarioIdAndEmpresaId(Long questionarioId, Long empresaId);
    boolean existsByQuestionarioIdAndEmpresaId(Long questionarioId, Long empresaId);
}
