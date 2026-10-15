package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.gestaoUsuarios.domain.TokenRecuperacaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;

import java.util.Optional;

@Repository
public interface TokenRecuperacaoSenhaRepository extends JpaRepository<TokenRecuperacaoSenha, Long> {
    Optional<TokenRecuperacaoSenha> findByToken(String token);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TokenRecuperacaoSenha> findWithLockByToken(String token);
    long countByUsuarioIdAndDataCriacaoAfter(Long usuarioId, LocalDateTime dataCriacao);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    java.util.List<TokenRecuperacaoSenha> findByUsuarioIdAndUsadoFalse(Long usuarioId);
}
