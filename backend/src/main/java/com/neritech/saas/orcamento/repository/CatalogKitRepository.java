package com.neritech.saas.orcamento.repository;

import com.neritech.saas.orcamento.domain.CatalogKit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CatalogKitRepository extends JpaRepository<CatalogKit, Long> {

    Optional<CatalogKit> findByIdAndEmpresaIdAndActiveTrue(Long id, Long empresaId);

    @Query("""
            SELECT kit FROM CatalogKit kit
            WHERE kit.empresaId = :empresaId
              AND kit.active = true
              AND (LOWER(kit.name) LIKE LOWER(CONCAT('%', :query, '%'))
                   OR LOWER(COALESCE(kit.reference, '')) LIKE LOWER(CONCAT('%', :query, '%')))
            """)
    Page<CatalogKit> searchActive(
            @Param("empresaId") Long empresaId,
            @Param("query") String query,
            Pageable pageable);
}
