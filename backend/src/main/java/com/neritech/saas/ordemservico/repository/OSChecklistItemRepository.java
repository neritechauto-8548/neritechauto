package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.OSChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OSChecklistItemRepository extends JpaRepository<OSChecklistItem, Long> {
    List<OSChecklistItem> findByOrdemServico_Id(Long ordemServicoId);

    List<OSChecklistItem> findByOrdemServico_IdAndOrdemServico_EmpresaId(Long ordemServicoId, Long empresaId);

    Optional<OSChecklistItem> findByIdAndOrdemServico_EmpresaId(Long id, Long empresaId);
}
