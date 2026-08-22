package com.neritech.saas.orcamento.repository;

import com.neritech.saas.orcamento.domain.OrcamentoServiceGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrcamentoServiceGroupRepository extends JpaRepository<OrcamentoServiceGroup, Long> {
    List<OrcamentoServiceGroup> findByEmpresaIdAndOrcamentoIdOrderByPositionAsc(Long empresaId, Long orcamentoId);
    Optional<OrcamentoServiceGroup> findByIdAndEmpresaIdAndOrcamentoId(Long id, Long empresaId, Long orcamentoId);
    long countByEmpresaIdAndOrcamentoId(Long empresaId, Long orcamentoId);
}
