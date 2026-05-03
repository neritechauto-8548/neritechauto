package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Long>, JpaSpecificationExecutor<Setor> {
    List<Setor> findByEmpresaId(Long empresaId);
    Page<Setor> findByEmpresaId(Long empresaId, Pageable pageable);
}

