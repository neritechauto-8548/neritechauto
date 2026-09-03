package com.neritech.saas.orcamento.repository;

import com.neritech.saas.orcamento.domain.CatalogKitVersionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogKitVersionItemRepository extends JpaRepository<CatalogKitVersionItem, Long> {
    List<CatalogKitVersionItem> findByEmpresaIdAndKitVersionIdOrderByPositionAsc(
            Long empresaId,
            Long kitVersionId);
}
