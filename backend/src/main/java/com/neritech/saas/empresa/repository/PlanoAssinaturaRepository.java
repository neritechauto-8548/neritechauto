package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.PlanoAssinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanoAssinaturaRepository
        extends JpaRepository<PlanoAssinatura, Long>, JpaSpecificationExecutor<PlanoAssinatura> {
    List<PlanoAssinatura> findByAtivoTrue();

    Optional<PlanoAssinatura> findFirstByNivelAndAtivoTrue(Integer nivel);

    Optional<PlanoAssinatura> findFirstByNomeIgnoreCaseAndAtivoTrue(String nome);
}
