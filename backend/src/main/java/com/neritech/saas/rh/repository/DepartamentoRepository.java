package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.Departamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Page<Departamento> findByEmpresaId(Long empresaId, Pageable pageable);

    List<Departamento> findByEmpresaIdAndAtivo(Long empresaId, Boolean ativo);

    Optional<Departamento> findByEmpresaIdAndCodigo(Long empresaId, String codigo);

    boolean existsByEmpresaIdAndCodigo(Long empresaId, String codigo);
}

