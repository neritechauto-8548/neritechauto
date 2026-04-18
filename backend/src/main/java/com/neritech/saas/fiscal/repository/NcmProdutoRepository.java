package com.neritech.saas.fiscal.repository;

import com.neritech.saas.fiscal.domain.NcmProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NcmProdutoRepository extends JpaRepository<NcmProduto, Long> {
    Optional<NcmProduto> findByCodigo(String codigo);
}
