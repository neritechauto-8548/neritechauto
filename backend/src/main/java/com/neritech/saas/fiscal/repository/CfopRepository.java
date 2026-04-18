package com.neritech.saas.fiscal.repository;

import com.neritech.saas.fiscal.domain.Cfop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CfopRepository extends JpaRepository<Cfop, Long> {
    Optional<Cfop> findByCodigo(String codigo);
}
