package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.AlertaAutomatico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaAutomaticoRepository extends JpaRepository<AlertaAutomatico, Long> {

    Page<AlertaAutomatico> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<AlertaAutomatico> findByEmpresaIdAndAtivo(Long empresaId, Boolean ativo, Pageable pageable);
}

