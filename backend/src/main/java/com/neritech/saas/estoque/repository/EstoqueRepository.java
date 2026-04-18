package com.neritech.saas.estoque.repository;

import com.neritech.saas.estoque.domain.Estoque;
import com.neritech.saas.estoque.domain.enums.StatusEstoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Page<Estoque> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<Estoque> findByEmpresaIdAndStatus(Long empresaId, StatusEstoque status, Pageable pageable);

    Page<Estoque> findByEmpresaIdAndProdutoId(Long empresaId, Long produtoId, Pageable pageable);

    java.util.Optional<Estoque> findFirstByEmpresaIdAndProdutoId(Long empresaId, Long produtoId);

    void deleteByProdutoId(Long produtoId);
}

