package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.OSAdditionalRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OSAdditionalRequestRepository extends JpaRepository<OSAdditionalRequest, Long> {
    Optional<OSAdditionalRequest> findByIdAndEmpresaId(Long id, Long empresaId);
    List<OSAdditionalRequest> findByOrdemServicoIdAndEmpresaIdOrderByDataCadastroDesc(Long ordemServicoId, Long empresaId);
    Optional<OSAdditionalRequest> findByTokenHash(String tokenHash);
}
