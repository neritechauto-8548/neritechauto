package com.neritech.saas.fiscal.repository;

import com.neritech.saas.fiscal.domain.CstProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CstProdutoRepository extends JpaRepository<CstProduto, Long> {
    Optional<CstProduto> findByCodigo(String codigo);
}
