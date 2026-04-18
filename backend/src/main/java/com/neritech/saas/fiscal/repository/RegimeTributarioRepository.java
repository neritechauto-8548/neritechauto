package com.neritech.saas.fiscal.repository;

import com.neritech.saas.fiscal.domain.RegimeTributario;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegimeTributarioRepository extends JpaRepository<RegimeTributario, Long> {
    Optional<RegimeTributario> findByCodigo(String codigo);
}
