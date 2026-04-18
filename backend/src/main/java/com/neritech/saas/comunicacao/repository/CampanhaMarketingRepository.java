package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.CampanhaMarketing;
import com.neritech.saas.comunicacao.domain.enums.StatusCampanha;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CampanhaMarketingRepository extends JpaRepository<CampanhaMarketing, Long> {

    Page<CampanhaMarketing> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<CampanhaMarketing> findByStatus(StatusCampanha status, Pageable pageable);

    Page<CampanhaMarketing> findByEmpresaIdAndStatus(Long empresaId, StatusCampanha status, Pageable pageable);

    Page<CampanhaMarketing> findByDataInicioBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);
}

