package com.neritech.saas.produtoServico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.neritech.saas.produtoServico.domain.CategoriaProduto;

@Repository
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {
    Page<CategoriaProduto> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<CategoriaProduto> findByEmpresaIdAndAtivoTrue(Long empresaId, Pageable pageable);

    java.util.List<CategoriaProduto> findByEmpresaId(Long empresaId);

    Page<CategoriaProduto> findByEmpresaIdAndNomeContainingIgnoreCase(Long empresaId, String nome, Pageable pageable);

    Page<CategoriaProduto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
