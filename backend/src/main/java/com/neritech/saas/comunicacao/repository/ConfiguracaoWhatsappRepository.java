package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.ConfiguracaoWhatsapp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoWhatsappRepository extends JpaRepository<ConfiguracaoWhatsapp, Long> {

    Page<ConfiguracaoWhatsapp> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<ConfiguracaoWhatsapp> findByEmpresaIdAndIntegracaoAtiva(Long empresaId, Boolean integracaoAtiva);
}

