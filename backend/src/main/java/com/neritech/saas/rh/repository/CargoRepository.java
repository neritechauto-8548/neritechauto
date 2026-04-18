package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.Cargo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    Page<Cargo> findByEmpresaId(Long empresaId, Pageable pageable);

    List<Cargo> findByEmpresaIdAndAtivo(Long empresaId, Boolean ativo);

    Optional<Cargo> findByEmpresaIdAndNome(Long empresaId, String nome);

    boolean existsByEmpresaIdAndNome(Long empresaId, String nome);
}

