package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.gestaoUsuarios.domain.TentativaLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TentativaLoginRepository extends JpaRepository<TentativaLogin, Long> {
    long countByEmailAndSucessoFalseAndDataTentativaAfter(String email, LocalDateTime data);
    
    long countByEmailAndEmpresaIdAndSucessoFalseAndDataTentativaAfter(String email, Long empresaId, LocalDateTime data);
    
    void deleteByEmailAndEmpresaId(String email, Long empresaId);
}
