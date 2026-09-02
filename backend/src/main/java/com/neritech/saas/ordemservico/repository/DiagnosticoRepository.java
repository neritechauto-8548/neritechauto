package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.Diagnostico;
import com.neritech.saas.ordemservico.domain.enums.SistemaVeiculo;
import com.neritech.saas.ordemservico.domain.enums.UrgenciaDiagnostico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
    List<Diagnostico> findByOrdemServicoId(Long ordemServicoId);

    Optional<Diagnostico> findByIdAndOrdemServico_EmpresaId(Long id, Long empresaId);

    List<Diagnostico> findByOrdemServico_IdAndOrdemServico_EmpresaId(Long ordemServicoId, Long empresaId);

    Page<Diagnostico> findByOrdemServicoId(Long ordemServicoId, Pageable pageable);

    Page<Diagnostico> findBySistemaVeiculo(SistemaVeiculo sistemaVeiculo, Pageable pageable);

    Page<Diagnostico> findByUrgencia(UrgenciaDiagnostico urgencia, Pageable pageable);

    List<Diagnostico> findByMecanicoDiagnosticoId(Long mecanicoId);
}
