package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.Diagnostico;
import com.neritech.saas.ordemservico.domain.enums.SistemaVeiculo;
import com.neritech.saas.ordemservico.domain.enums.UrgenciaDiagnostico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
    List<Diagnostico> findByOrdemServicoId(Long ordemServicoId);

    Page<Diagnostico> findByOrdemServicoId(Long ordemServicoId, Pageable pageable);

    Page<Diagnostico> findBySistemaVeiculo(SistemaVeiculo sistemaVeiculo, Pageable pageable);

    Page<Diagnostico> findByUrgencia(UrgenciaDiagnostico urgencia, Pageable pageable);

    List<Diagnostico> findByMecanicoDiagnosticoId(Long mecanicoId);
}
