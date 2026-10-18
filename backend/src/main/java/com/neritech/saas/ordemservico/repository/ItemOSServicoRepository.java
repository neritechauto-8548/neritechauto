package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.enums.StatusExecucao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemOSServicoRepository extends JpaRepository<ItemOSServico, Long> {
    Optional<ItemOSServico> findByIdAndOrdemServicoEmpresaId(Long id, Long empresaId);
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select i from ItemOSServico i where i.id=:id and i.ordemServico.empresaId=:empresaId")
    Optional<ItemOSServico> lockForPatio(@org.springframework.data.repository.query.Param("id") Long id,
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId);
    List<ItemOSServico> findByOrdemServicoId(Long ordemServicoId);

    Page<ItemOSServico> findByOrdemServicoId(Long ordemServicoId, Pageable pageable);

    Page<ItemOSServico> findByOrdemServicoIdAndStatusExecucao(Long ordemServicoId, StatusExecucao status,
            Pageable pageable);

    List<ItemOSServico> findByMecanicoExecutorId(Long mecanicoId);
}
