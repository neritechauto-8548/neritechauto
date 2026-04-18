package com.neritech.saas.integracao.repository;

import com.neritech.saas.integracao.domain.TokenApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenApiRepository extends JpaRepository<TokenApi, Long> {
    Optional<TokenApi> findByToken(String token);

    List<TokenApi> findByIntegracaoId(Long integracaoId);

    List<TokenApi> findByAtivoTrue();
}
