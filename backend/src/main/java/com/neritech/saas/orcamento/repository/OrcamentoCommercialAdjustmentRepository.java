package com.neritech.saas.orcamento.repository;

import com.neritech.saas.orcamento.domain.OrcamentoCommercialAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrcamentoCommercialAdjustmentRepository
        extends JpaRepository<OrcamentoCommercialAdjustment, Long> {
}

