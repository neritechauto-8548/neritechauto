package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.Checklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    Optional<Checklist> findByIdAndEmpresaId(Long id, Long empresaId);

    Page<Checklist> findByEmpresaId(Long empresaId, Pageable pageable);

    boolean existsByEmpresaIdAndDsChecklistIgnoreCase(Long empresaId, String dsChecklist);

    boolean existsByEmpresaIdAndDsChecklistIgnoreCaseAndIdNot(Long empresaId, String dsChecklist, Long id);
}
