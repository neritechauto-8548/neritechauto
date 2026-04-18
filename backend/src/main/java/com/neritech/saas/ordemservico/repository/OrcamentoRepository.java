package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.Orcamento;
import com.neritech.saas.ordemservico.domain.enums.StatusOrcamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    List<Orcamento> findByOrdemServicoId(Long ordemServicoId);

    Page<Orcamento> findByOrdemServicoId(Long ordemServicoId, Pageable pageable);

    Page<Orcamento> findByStatus(StatusOrcamento status, Pageable pageable);

    Optional<Orcamento> findByNumeroOrcamento(String numeroOrcamento);

    boolean existsByNumeroOrcamento(String numeroOrcamento);
}
