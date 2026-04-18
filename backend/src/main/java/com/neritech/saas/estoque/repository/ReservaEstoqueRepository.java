package com.neritech.saas.estoque.repository;

import com.neritech.saas.estoque.domain.ReservaEstoque;
import com.neritech.saas.estoque.domain.enums.StatusReserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaEstoqueRepository extends JpaRepository<ReservaEstoque, Long> {

    Page<ReservaEstoque> findByProdutoId(Long produtoId, Pageable pageable);

    Page<ReservaEstoque> findByStatus(StatusReserva status, Pageable pageable);

    Page<ReservaEstoque> findByDocumentoTipoAndDocumentoId(String documentoTipo, Long documentoId, Pageable pageable);
}
