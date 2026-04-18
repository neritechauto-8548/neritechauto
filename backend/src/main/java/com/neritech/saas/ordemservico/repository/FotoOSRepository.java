package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.FotoOS;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FotoOSRepository extends JpaRepository<FotoOS, Long> {
    List<FotoOS> findByOrdemServicoIdOrderByIdAsc(Long ordemServicoId);
}
