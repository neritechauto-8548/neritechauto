package com.neritech.saas.relatorios.repository;

import com.neritech.saas.relatorios.domain.MetricaPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetricaPerformanceRepository extends JpaRepository<MetricaPerformance, Long> {
    List<MetricaPerformance> findByEmpresaId(Long empresaId);

    List<MetricaPerformance> findByEmpresaIdAndMetrica(Long empresaId, String metrica);
}

