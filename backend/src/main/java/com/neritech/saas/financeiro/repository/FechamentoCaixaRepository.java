package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.FechamentoCaixa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FechamentoCaixaRepository extends JpaRepository<FechamentoCaixa, Long> {
    Page<FechamentoCaixa> findByEmpresaIdAndDataAberturaBetween(Long empresaId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    Page<FechamentoCaixa> findByEmpresaId(Long empresaId, Pageable pageable);
}
