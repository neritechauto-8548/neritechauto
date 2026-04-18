package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.ConciliacaoBancaria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConciliacaoBancariaRepository
        extends JpaRepository<ConciliacaoBancaria, Long>, JpaSpecificationExecutor<ConciliacaoBancaria> {
    List<ConciliacaoBancaria> findByContaBancariaId(Long contaBancariaId);

    Page<ConciliacaoBancaria> findByContaBancariaId(Long contaBancariaId, Pageable pageable);
}
