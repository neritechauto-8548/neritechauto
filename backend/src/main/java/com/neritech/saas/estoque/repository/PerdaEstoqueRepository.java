package com.neritech.saas.estoque.repository;

import com.neritech.saas.estoque.domain.PerdaEstoque;
import com.neritech.saas.estoque.domain.enums.TipoPerda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerdaEstoqueRepository extends JpaRepository<PerdaEstoque, Long> {

    Page<PerdaEstoque> findByProdutoId(Long produtoId, Pageable pageable);

    Page<PerdaEstoque> findByTipoPerda(TipoPerda tipo, Pageable pageable);
}
