package com.neritech.saas.relatorios.repository;

import com.neritech.saas.relatorios.domain.RelatorioSalvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RelatorioSalvoRepository extends JpaRepository<RelatorioSalvo, Long> {
    List<RelatorioSalvo> findByEmpresaId(Long empresaId);

    List<RelatorioSalvo> findByEmpresaIdAndAtivoTrue(Long empresaId);

    List<RelatorioSalvo> findByEmpresaIdAndCategoria(Long empresaId, String categoria);
}

