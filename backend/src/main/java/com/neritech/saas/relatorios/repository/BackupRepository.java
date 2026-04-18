package com.neritech.saas.relatorios.repository;

import com.neritech.saas.relatorios.domain.Backup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BackupRepository extends JpaRepository<Backup, Long> {
    List<Backup> findByEmpresaId(Long empresaId);

    List<Backup> findByEmpresaIdOrderByDataInicioDesc(Long empresaId);
}

