package com.neritech.saas.orcamento.repository;

import com.neritech.saas.orcamento.domain.OrcamentoDiscountApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface OrcamentoDiscountApprovalRequestRepository
        extends JpaRepository<OrcamentoDiscountApprovalRequest, Long> {

    Optional<OrcamentoDiscountApprovalRequest>
            findFirstByEmpresaIdAndLineItemIdAndStatusOrderByDataCadastroDesc(
                    Long empresaId,
                    Long lineItemId,
                    OrcamentoDiscountApprovalRequest.Status status);

    Optional<OrcamentoDiscountApprovalRequest> findByIdAndEmpresaIdAndOrcamentoId(
            Long id,
            Long empresaId,
            Long orcamentoId);

    List<OrcamentoDiscountApprovalRequest> findByEmpresaIdAndOrcamentoIdAndStatus(
            Long empresaId,
            Long orcamentoId,
            OrcamentoDiscountApprovalRequest.Status status);
}

