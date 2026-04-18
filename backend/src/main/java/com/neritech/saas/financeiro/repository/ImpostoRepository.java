package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.Imposto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImpostoRepository extends JpaRepository<Imposto, Long>, JpaSpecificationExecutor<Imposto> {
    Page<Imposto> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<Imposto> findByIdAndEmpresaId(Long id, Long empresaId);
}

