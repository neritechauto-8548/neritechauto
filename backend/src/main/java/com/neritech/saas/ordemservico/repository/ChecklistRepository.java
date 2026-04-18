package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.Checklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    Page<Checklist> findByEmpresaId(Long empresaId, Pageable pageable);
}

