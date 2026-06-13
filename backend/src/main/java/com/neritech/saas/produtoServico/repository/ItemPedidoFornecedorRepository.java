package com.neritech.saas.produtoServico.repository;

import com.neritech.saas.produtoServico.domain.ItemPedidoFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoFornecedorRepository extends JpaRepository<ItemPedidoFornecedor, Long> {
    List<ItemPedidoFornecedor> findByPedidoId(Long pedidoId);
}
