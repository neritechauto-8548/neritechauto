package com.neritech.saas.orcamento.repository;

import com.neritech.saas.orcamento.domain.CatalogKitVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogKitVersionRepository extends JpaRepository<CatalogKitVersion, Long> {
    Optional<CatalogKitVersion> findByEmpresaIdAndKitIdAndVersionNumberAndPublishedTrue(
            Long empresaId,
            Long kitId,
            int versionNumber);
}
