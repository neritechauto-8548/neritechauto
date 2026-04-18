package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.ConfiguracaoOficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConfiguracaoOficinaRepository
        extends JpaRepository<ConfiguracaoOficina, Long>, JpaSpecificationExecutor<ConfiguracaoOficina> {
    Optional<ConfiguracaoOficina> findByEmpresaId(Long empresaId);

    boolean existsByEmpresaId(Long empresaId);
}

