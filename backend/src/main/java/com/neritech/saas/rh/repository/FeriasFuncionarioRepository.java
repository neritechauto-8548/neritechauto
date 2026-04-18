package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.enums.StatusFerias;
import com.neritech.saas.rh.domain.FeriasFuncionario;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeriasFuncionarioRepository extends JpaRepository<FeriasFuncionario, Long> {

    List<FeriasFuncionario> findByFuncionarioId(Long funcionarioId);

    List<FeriasFuncionario> findByStatus(StatusFerias status);

    List<FeriasFuncionario> findByDataInicioFeriasBetween(LocalDate inicio, LocalDate fim);

    List<FeriasFuncionario> findByFuncionarioIdAndStatus(Long funcionarioId, StatusFerias status);
}
