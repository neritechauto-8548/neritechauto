package com.neritech.saas.veiculo.repository;

import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeloVeiculoRepository
        extends JpaRepository<ModeloVeiculo, Long>, JpaSpecificationExecutor<ModeloVeiculo> {
    Optional<ModeloVeiculo> findByMarcaIdAndNome(Long marcaId, String nome);

    List<ModeloVeiculo> findByMarcaId(Long marcaId);
}
