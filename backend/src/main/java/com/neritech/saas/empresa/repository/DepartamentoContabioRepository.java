package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.DepartamentoContabio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DepartamentoContabioRepository extends JpaRepository<DepartamentoContabio, Long>, JpaSpecificationExecutor<DepartamentoContabio> {
    List<DepartamentoContabio> findByEmpresaId(Long empresaId);
}

