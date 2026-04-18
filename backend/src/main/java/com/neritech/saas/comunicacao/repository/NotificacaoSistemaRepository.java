package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.NotificacaoSistema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoSistemaRepository extends JpaRepository<NotificacaoSistema, Long> {

    Page<NotificacaoSistema> findByUsuarioDestinatarioId(Long usuarioDestinatarioId, Pageable pageable);

    Page<NotificacaoSistema> findByUsuarioDestinatarioIdAndLida(Long usuarioDestinatarioId, Boolean lida,
            Pageable pageable);

    Page<NotificacaoSistema> findByEmpresaId(Long empresaId, Pageable pageable);
}

