package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.ConfiguracaoSms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoSmsRepository extends JpaRepository<ConfiguracaoSms, Long> {

    Page<ConfiguracaoSms> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<ConfiguracaoSms> findByEmpresaIdAndAtivo(Long empresaId, Boolean ativo);
}

