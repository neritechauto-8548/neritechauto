package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.ComunicacaoEnviada;
import com.neritech.saas.comunicacao.domain.enums.StatusComunicacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComunicacaoEnviadaRepository extends JpaRepository<ComunicacaoEnviada, Long> {

    Page<ComunicacaoEnviada> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<ComunicacaoEnviada> findByDestinatarioId(Long destinatarioId, Pageable pageable);

    Page<ComunicacaoEnviada> findByStatus(StatusComunicacao status, Pageable pageable);

    Page<ComunicacaoEnviada> findByEmpresaIdAndStatus(Long empresaId, StatusComunicacao status, Pageable pageable);
}

