package com.neritech.saas.estoque.repository;

import com.neritech.saas.estoque.domain.ItemInventario;
import com.neritech.saas.estoque.domain.enums.StatusItemInventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInventarioRepository extends JpaRepository<ItemInventario, Long> {

    Page<ItemInventario> findByInventarioId(Long inventarioId, Pageable pageable);

    Page<ItemInventario> findByInventarioIdAndStatus(Long inventarioId, StatusItemInventario status, Pageable pageable);

    void deleteByProdutoId(Long produtoId);
}
