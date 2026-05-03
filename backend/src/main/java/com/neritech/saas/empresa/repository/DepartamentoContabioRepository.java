package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.DepartamentoContabio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface DepartamentoContabioRepository extends JpaRepository<DepartamentoContabio, Long>, JpaSpecificationExecutor<DepartamentoContabio> {
    List<DepartamentoContabio> findByEmpresaId(Long empresaId);
    Page<DepartamentoContabio> findByEmpresaId(Long empresaId, Pageable pageable);
}

