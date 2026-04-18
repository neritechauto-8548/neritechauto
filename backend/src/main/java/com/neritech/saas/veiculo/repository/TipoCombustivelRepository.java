package com.neritech.saas.veiculo.repository;

import com.neritech.saas.veiculo.domain.TipoCombustivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoCombustivelRepository
        extends JpaRepository<TipoCombustivel, Long>, JpaSpecificationExecutor<TipoCombustivel> {
    Optional<TipoCombustivel> findByNome(String nome);
}
