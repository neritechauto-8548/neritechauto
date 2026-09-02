package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.WorkSession;
import com.neritech.saas.ordemservico.domain.enums.WorkSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface WorkSessionRepository extends JpaRepository<WorkSession, Long> {

    Optional<WorkSession> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<WorkSession> findFirstByEmpresaIdAndTechnicianUserIdAndStatusIn(
            Long empresaId,
            Long technicianUserId,
            Collection<WorkSessionStatus> statuses);

    List<WorkSession> findByOrdemServicoIdAndEmpresaIdOrderByStartedAtDesc(
            Long ordemServicoId,
            Long empresaId);

    List<WorkSession> findByItemServicoIdAndEmpresaIdOrderByStartedAtDesc(
            Long itemServicoId,
            Long empresaId);
}
