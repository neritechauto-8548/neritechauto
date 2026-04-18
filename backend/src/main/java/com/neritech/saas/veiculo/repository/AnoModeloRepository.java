package com.neritech.saas.veiculo.repository;

import com.neritech.saas.veiculo.domain.AnoModelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnoModeloRepository extends JpaRepository<AnoModelo, Long>, JpaSpecificationExecutor<AnoModelo> {
    List<AnoModelo> findByModeloId(Long modeloId);

    Optional<AnoModelo> findByModeloIdAndAnoFabricacaoAndAnoModelo(Long modeloId, Integer anoFabricacao,
            Integer anoModelo);
}
