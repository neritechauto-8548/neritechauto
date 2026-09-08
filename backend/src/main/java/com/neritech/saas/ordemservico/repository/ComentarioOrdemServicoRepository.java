package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.ComentarioOrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioOrdemServicoRepository extends JpaRepository<ComentarioOrdemServico, Long> {
    List<ComentarioOrdemServico> findByEmpresaIdAndOrdemServicoIdOrderByDataCadastroDescIdDesc(Long empresaId, Long ordemServicoId);
}
