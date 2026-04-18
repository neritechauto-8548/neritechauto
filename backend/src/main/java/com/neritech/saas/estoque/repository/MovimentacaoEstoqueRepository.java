package com.neritech.saas.estoque.repository;

import com.neritech.saas.estoque.domain.MovimentacaoEstoque;
import com.neritech.saas.estoque.domain.enums.TipoMovimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    Page<MovimentacaoEstoque> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<MovimentacaoEstoque> findByEmpresaIdAndProdutoId(Long empresaId, Long produtoId, Pageable pageable);

    Page<MovimentacaoEstoque> findByEmpresaIdAndTipoMovimentacao(Long empresaId, TipoMovimentacao tipo,
            Pageable pageable);
}

