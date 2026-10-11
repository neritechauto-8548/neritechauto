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
    Page<NotificacaoSistema> findByEmpresaIdAndUsuarioDestinatarioId(Long empresaId, Long usuarioDestinatarioId, Pageable pageable);
    java.util.Optional<NotificacaoSistema> findByIdAndEmpresaId(Long id, Long empresaId);
    java.util.Optional<NotificacaoSistema> findByIdAndEmpresaIdAndUsuarioDestinatarioId(Long id, Long empresaId, Long usuarioDestinatarioId);
}

