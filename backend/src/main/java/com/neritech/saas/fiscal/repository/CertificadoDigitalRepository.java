package com.neritech.saas.fiscal.repository;

import com.neritech.saas.fiscal.domain.CertificadoDigital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificadoDigitalRepository extends JpaRepository<CertificadoDigital, Long> {
    Optional<CertificadoDigital> findByAtivoTrue();
}
