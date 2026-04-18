package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.OSChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OSChecklistItemRepository extends JpaRepository<OSChecklistItem, Long> {
    List<OSChecklistItem> findByOrdemServico_Id(Long ordemServicoId);
}
