package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.enums.StatusExecucao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemOSServicoRepository extends JpaRepository<ItemOSServico, Long> {
    List<ItemOSServico> findByOrdemServicoId(Long ordemServicoId);

    Optional<ItemOSServico> findByIdAndOrdemServicoId(Long id, Long ordemServicoId);

    Page<ItemOSServico> findByOrdemServicoId(Long ordemServicoId, Pageable pageable);

    Page<ItemOSServico> findByOrdemServicoIdAndStatusExecucao(Long ordemServicoId, StatusExecucao status,
            Pageable pageable);

    List<ItemOSServico> findByMecanicoExecutorId(Long mecanicoId);
}
