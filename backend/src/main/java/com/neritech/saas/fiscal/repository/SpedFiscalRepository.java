package com.neritech.saas.fiscal.repository;

import com.neritech.saas.fiscal.domain.SpedFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SpedFiscalRepository extends JpaRepository<SpedFiscal, Long> {
    List<SpedFiscal> findByPeriodoInicioBetween(LocalDate start, LocalDate end);

    List<SpedFiscal> findByStatus(String status);
}
