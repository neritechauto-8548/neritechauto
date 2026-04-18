package com.neritech.saas.produtoServico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.neritech.saas.produtoServico.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findByEmpresaId(Long empresaId, Pageable pageable);

    boolean existsByEmpresaIdAndCodigoInterno(Long empresaId, String codigoInterno);

    Page<Produto> findByEmpresaIdAndAtivoTrue(Long empresaId, Pageable pageable);

    Page<Produto> findByEmpresaIdAndCategoriaId(Long empresaId, Long categoriaId, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM Produto p WHERE p.empresaId = :empresaId AND (LOWER(p.nome) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.codigoInterno) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Produto> search(Long empresaId, String search, Pageable pageable);
}

