package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.ConfiguracaoEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoEmailRepository extends JpaRepository<ConfiguracaoEmail, Long> {

    Page<ConfiguracaoEmail> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<ConfiguracaoEmail> findByEmpresaIdAndAtivo(Long empresaId, Boolean ativo);
}

