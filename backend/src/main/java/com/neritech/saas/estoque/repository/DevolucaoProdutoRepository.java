package com.neritech.saas.estoque.repository;

import com.neritech.saas.estoque.domain.DevolucaoProduto;
import com.neritech.saas.estoque.domain.enums.MotivoDevolucao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevolucaoProdutoRepository extends JpaRepository<DevolucaoProduto, Long> {

    Page<DevolucaoProduto> findByProdutoId(Long produtoId, Pageable pageable);

    Page<DevolucaoProduto> findByMotivoDevolucao(MotivoDevolucao motivo, Pageable pageable);

    Page<DevolucaoProduto> findByOrdemServicoId(Long ordemServicoId, Pageable pageable);
}
