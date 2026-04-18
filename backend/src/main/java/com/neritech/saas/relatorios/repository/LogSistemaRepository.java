package com.neritech.saas.relatorios.repository;

import com.neritech.saas.relatorios.domain.enums.NivelLog;
import com.neritech.saas.relatorios.domain.LogSistema;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogSistemaRepository extends JpaRepository<LogSistema, Long> {
    List<LogSistema> findByEmpresaId(Long empresaId);

    List<LogSistema> findByEmpresaIdAndNivelLog(Long empresaId, NivelLog nivelLog);

    List<LogSistema> findByEmpresaIdAndDataOcorrenciaBetween(Long empresaId, LocalDateTime start, LocalDateTime end);
}

