package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssinaturaEmpresaRepository
        extends JpaRepository<AssinaturaEmpresa, Long>, JpaSpecificationExecutor<AssinaturaEmpresa> {
    List<AssinaturaEmpresa> findByEmpresaId(Long empresaId);

    Optional<AssinaturaEmpresa> findByEmpresaIdAndStatus(Long empresaId, StatusAssinatura status);
}

