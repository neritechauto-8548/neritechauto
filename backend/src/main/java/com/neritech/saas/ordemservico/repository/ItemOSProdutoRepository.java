package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.ItemOSProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemOSProdutoRepository extends JpaRepository<ItemOSProduto, Long> {
    Optional<ItemOSProduto> findByIdAndOrdemServicoEmpresaId(Long id, Long empresaId);
    List<ItemOSProduto> findByOrdemServicoId(Long ordemServicoId);

    Page<ItemOSProduto> findByOrdemServicoId(Long ordemServicoId, Pageable pageable);

    List<ItemOSProduto> findByProdutoId(Long produtoId);

    List<ItemOSProduto> findByLoteNumero(String loteNumero);
}
