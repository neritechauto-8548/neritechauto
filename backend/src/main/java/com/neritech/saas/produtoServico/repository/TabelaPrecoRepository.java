package com.neritech.saas.produtoServico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.neritech.saas.produtoServico.domain.TabelaPreco;

@Repository
public interface TabelaPrecoRepository extends JpaRepository<TabelaPreco, Long> {
    Page<TabelaPreco> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<TabelaPreco> findByEmpresaIdAndAtivoTrue(Long empresaId, Pageable pageable);
}

