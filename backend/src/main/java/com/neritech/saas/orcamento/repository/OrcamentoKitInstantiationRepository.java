package com.neritech.saas.orcamento.repository;

import com.neritech.saas.orcamento.domain.OrcamentoKitInstantiation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrcamentoKitInstantiationRepository extends JpaRepository<OrcamentoKitInstantiation, Long> {
    Optional<OrcamentoKitInstantiation> findByEmpresaIdAndOrcamentoIdAndIdempotencyKey(
            Long empresaId,
            Long orcamentoId,
            String idempotencyKey);
}
