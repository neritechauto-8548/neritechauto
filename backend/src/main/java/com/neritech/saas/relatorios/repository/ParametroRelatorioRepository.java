package com.neritech.saas.relatorios.repository;

import com.neritech.saas.relatorios.domain.ParametroRelatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParametroRelatorioRepository extends JpaRepository<ParametroRelatorio, Long> {
    List<ParametroRelatorio> findByRelatorioId(Long relatorioId);

    List<ParametroRelatorio> findByRelatorioIdAndAtivoTrueOrderByOrdemExibicaoAsc(Long relatorioId);
}
