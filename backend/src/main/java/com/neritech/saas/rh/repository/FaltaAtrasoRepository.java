package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.enums.TipoOcorrencia;
import com.neritech.saas.rh.domain.FaltaAtraso;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaltaAtrasoRepository extends JpaRepository<FaltaAtraso, Long> {

    List<FaltaAtraso> findByFuncionarioId(Long funcionarioId);

    List<FaltaAtraso> findByDataOcorrenciaBetween(LocalDate inicio, LocalDate fim);

    List<FaltaAtraso> findByFuncionarioIdAndTipoOcorrencia(Long funcionarioId, TipoOcorrencia tipoOcorrencia);

    List<FaltaAtraso> findByFuncionarioIdAndJustificada(Long funcionarioId, Boolean justificada);
}
