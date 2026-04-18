package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.ConfiguracaoFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConfiguracaoFiscalRepository
        extends JpaRepository<ConfiguracaoFiscal, Long>, JpaSpecificationExecutor<ConfiguracaoFiscal> {
    Optional<ConfiguracaoFiscal> findByEmpresaId(Long empresaId);

    boolean existsByEmpresaId(Long empresaId);
}

