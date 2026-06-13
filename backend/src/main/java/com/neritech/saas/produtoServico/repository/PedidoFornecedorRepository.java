package com.neritech.saas.produtoServico.repository;

import com.neritech.saas.produtoServico.domain.PedidoFornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoFornecedorRepository extends JpaRepository<PedidoFornecedor, Long> {

    @Query(value = "SELECT p FROM PedidoFornecedor p JOIN FETCH p.fornecedor WHERE p.empresaId = :empresaId",
           countQuery = "SELECT COUNT(p) FROM PedidoFornecedor p WHERE p.empresaId = :empresaId")
    Page<PedidoFornecedor> findByEmpresaId(@Param("empresaId") Long empresaId, Pageable pageable);

    @Query(value = "SELECT p FROM PedidoFornecedor p JOIN FETCH p.fornecedor f " +
           "WHERE p.empresaId = :empresaId " +
           "AND (:termo IS NULL OR :termo = '' OR " +
           "CAST(p.numeroPedido AS string) LIKE %:termo% OR " +
           "LOWER(f.nomeFantasia) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(p.responsavel) LIKE LOWER(CONCAT('%', :termo, '%')))",
           countQuery = "SELECT COUNT(p) FROM PedidoFornecedor p JOIN p.fornecedor f " +
           "WHERE p.empresaId = :empresaId " +
           "AND (:termo IS NULL OR :termo = '' OR " +
           "CAST(p.numeroPedido AS string) LIKE %:termo% OR " +
           "LOWER(f.nomeFantasia) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(p.responsavel) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<PedidoFornecedor> findByEmpresaIdAndTermo(@Param("empresaId") Long empresaId,
                                                    @Param("termo") String termo,
                                                    Pageable pageable);

    @Query("SELECT COALESCE(MAX(p.numeroPedido), 0) + 1 FROM PedidoFornecedor p WHERE p.empresaId = :empresaId")
    Long nextNumeroPedido(@Param("empresaId") Long empresaId);
}
