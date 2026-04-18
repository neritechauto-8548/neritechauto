package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.ItChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("ordemServicoItChecklistRepository")
public interface ItChecklistRepository extends JpaRepository<ItChecklist, Long> {
    List<ItChecklist> findByChecklist_Id(Long checklistId);
}
