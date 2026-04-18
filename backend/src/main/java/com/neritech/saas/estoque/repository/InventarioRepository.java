package com.neritech.saas.estoque.repository;

import com.neritech.saas.estoque.domain.Inventario;
import com.neritech.saas.estoque.domain.enums.StatusInventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Page<Inventario> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<Inventario> findByEmpresaIdAndStatus(Long empresaId, StatusInventario status, Pageable pageable);

    Optional<Inventario> findByEmpresaIdAndCodigo(Long empresaId, String codigo);

    boolean existsByEmpresaIdAndCodigo(Long empresaId, String codigo);
}

