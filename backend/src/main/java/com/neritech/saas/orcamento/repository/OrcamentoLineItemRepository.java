package com.neritech.saas.orcamento.repository;

import com.neritech.saas.orcamento.domain.OrcamentoLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrcamentoLineItemRepository extends JpaRepository<OrcamentoLineItem, Long> {
    @Query("""
            SELECT item FROM OrcamentoLineItem item
            JOIN FETCH item.group grp
            WHERE item.empresaId = :empresaId
              AND grp.orcamento.id = :orcamentoId
            ORDER BY grp.position, item.position
            """)
    List<OrcamentoLineItem> findCompositionLines(
            @Param("empresaId") Long empresaId,
            @Param("orcamentoId") Long orcamentoId);

    long countByEmpresaIdAndGroupId(Long empresaId, Long groupId);
}
