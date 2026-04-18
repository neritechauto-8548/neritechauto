package com.neritech.saas.garantia.repository;

import com.neritech.saas.garantia.domain.ItemGarantia;
import com.neritech.saas.garantia.domain.TipoItemGarantia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para ItemGarantia
 */
@Repository
public interface ItemGarantiaRepository extends JpaRepository<ItemGarantia, Long> {

    List<ItemGarantia> findByGarantiaId(Long garantiaId);

    List<ItemGarantia> findByGarantiaIdAndAtivo(Long garantiaId, Boolean ativo);

    List<ItemGarantia> findByGarantiaIdAndTipoItem(Long garantiaId, TipoItemGarantia tipoItem);

    List<ItemGarantia> findByServicoId(Long servicoId);

    List<ItemGarantia> findByProdutoId(Long produtoId);
}
