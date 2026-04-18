package com.neritech.saas.estoque.repository;

import com.neritech.saas.estoque.domain.LoteProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoteProdutoRepository extends JpaRepository<LoteProduto, Long> {

    Page<LoteProduto> findByProdutoId(Long produtoId, Pageable pageable);

    Page<LoteProduto> findByNumeroLote(String numeroLote, Pageable pageable);
}
