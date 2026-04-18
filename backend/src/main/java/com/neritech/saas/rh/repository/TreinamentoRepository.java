package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.enums.StatusTreinamento;
import com.neritech.saas.rh.domain.Treinamento;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreinamentoRepository extends JpaRepository<Treinamento, Long> {

    Page<Treinamento> findByEmpresaId(Long empresaId, Pageable pageable);

    List<Treinamento> findByStatus(StatusTreinamento status);

    List<Treinamento> findByDataInicioBetween(LocalDate inicio, LocalDate fim);

    List<Treinamento> findByEmpresaIdAndStatus(Long empresaId, StatusTreinamento status);
}
