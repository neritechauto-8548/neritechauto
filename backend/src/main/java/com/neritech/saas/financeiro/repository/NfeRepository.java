package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.Nfe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NfeRepository extends JpaRepository<Nfe, Long>, JpaSpecificationExecutor<Nfe> {
    Page<Nfe> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<Nfe> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<Nfe> findByChaveNfe(String chaveNfe);
}

