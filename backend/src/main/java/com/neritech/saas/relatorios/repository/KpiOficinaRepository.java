package com.neritech.saas.relatorios.repository;

import com.neritech.saas.relatorios.domain.KpiOficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KpiOficinaRepository extends JpaRepository<KpiOficina, Long> {
    List<KpiOficina> findByEmpresaId(Long empresaId);

    List<KpiOficina> findByEmpresaIdAndAtivoTrue(Long empresaId);

    List<KpiOficina> findByEmpresaIdAndCategoria(Long empresaId, String categoria);
}

