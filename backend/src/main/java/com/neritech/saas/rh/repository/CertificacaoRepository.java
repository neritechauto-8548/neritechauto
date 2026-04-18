package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.Certificacao;
import com.neritech.saas.rh.domain.enums.StatusCertificacao;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificacaoRepository extends JpaRepository<Certificacao, Long> {

    List<Certificacao> findByFuncionarioId(Long funcionarioId);

    List<Certificacao> findByStatus(StatusCertificacao status);

    List<Certificacao> findByDataValidadeBetween(LocalDate inicio, LocalDate fim);

    List<Certificacao> findByFuncionarioIdAndStatus(Long funcionarioId, StatusCertificacao status);
}
