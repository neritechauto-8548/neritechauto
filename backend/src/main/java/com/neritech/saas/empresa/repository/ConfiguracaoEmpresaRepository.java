package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.ConfiguracaoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoEmpresaRepository
        extends JpaRepository<ConfiguracaoEmpresa, Long>, JpaSpecificationExecutor<ConfiguracaoEmpresa> {

    Optional<ConfiguracaoEmpresa> findByEmpresaId(Long empresaId);

    boolean existsByEmpresaId(Long empresaId);
}

