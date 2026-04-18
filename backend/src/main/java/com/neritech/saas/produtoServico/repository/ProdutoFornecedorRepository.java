package com.neritech.saas.produtoServico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.neritech.saas.produtoServico.domain.ProdutoFornecedor;

@Repository
public interface ProdutoFornecedorRepository extends JpaRepository<ProdutoFornecedor, Long> {
    Page<ProdutoFornecedor> findByProdutoId(Long produtoId, Pageable pageable);

    Page<ProdutoFornecedor> findByFornecedorId(Long fornecedorId, Pageable pageable);
}
