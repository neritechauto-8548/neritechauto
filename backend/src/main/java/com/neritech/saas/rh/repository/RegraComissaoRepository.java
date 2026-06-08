package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.RegraComissao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegraComissaoRepository extends JpaRepository<RegraComissao, Long> {
    Page<RegraComissao> findByFuncionarioId(Long funcionarioId, Pageable pageable);
    List<RegraComissao> findByFuncionarioId(Long funcionarioId);
    Optional<RegraComissao> findByIdAndEmpresaId(Long id, Long empresaId);
}
