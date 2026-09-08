package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.EnderecoEmpresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecoEmpresaRepository
        extends JpaRepository<EnderecoEmpresa, Long>, JpaSpecificationExecutor<EnderecoEmpresa> {

    Optional<EnderecoEmpresa> findByIdAndEmpresaId(Long id, Long empresaId);

    Page<EnderecoEmpresa> findByEmpresaId(Long empresaId, Pageable pageable);

    List<EnderecoEmpresa> findByEmpresaId(Long empresaId);

    List<EnderecoEmpresa> findByEmpresaIdAndAtivoTrue(Long empresaId);
}
