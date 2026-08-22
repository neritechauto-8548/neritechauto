package com.neritech.saas.orcamento.repository;

import com.neritech.saas.orcamento.domain.OrcamentoDiscountAuthorityLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface OrcamentoDiscountAuthorityLimitRepository
        extends JpaRepository<OrcamentoDiscountAuthorityLimit, Long> {

    @Query(value = """
            SELECT MAX(l.max_percentage)
            FROM estimate_discount_authority_limits l
            JOIN usuarios_funcoes uf
              ON uf.funcao_id = l.funcao_id
             AND uf.empresa_id = l.empresa_id
            WHERE l.empresa_id = :empresaId
              AND uf.usuario_id = :usuarioId
              AND l.active = TRUE
            """, nativeQuery = true)
    Optional<BigDecimal> findMaximumForUser(
            @Param("empresaId") Long empresaId,
            @Param("usuarioId") Long usuarioId);
}

