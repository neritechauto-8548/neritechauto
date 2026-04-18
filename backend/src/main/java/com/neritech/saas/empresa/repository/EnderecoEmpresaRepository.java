package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.EnderecoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnderecoEmpresaRepository
        extends JpaRepository<EnderecoEmpresa, Long>, JpaSpecificationExecutor<EnderecoEmpresa> {
    List<EnderecoEmpresa> findByEmpresaId(Long empresaId);

    List<EnderecoEmpresa> findByEmpresaIdAndAtivoTrue(Long empresaId);
}

