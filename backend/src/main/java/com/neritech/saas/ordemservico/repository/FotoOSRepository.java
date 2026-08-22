package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.FotoOS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FotoOSRepository extends JpaRepository<FotoOS, Long> {
    Optional<FotoOS> findByIdAndEmpresaId(Long id, Long empresaId);

    List<FotoOS> findByOrdemServicoIdAndEmpresaIdOrderByIdAsc(Long ordemServicoId, Long empresaId);
}
