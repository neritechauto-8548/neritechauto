package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.ComissaoFuncionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComissaoFuncionarioRepository
        extends JpaRepository<ComissaoFuncionario, Long>, JpaSpecificationExecutor<ComissaoFuncionario> {
    List<ComissaoFuncionario> findByFuncionarioId(Long funcionarioId);

    Page<ComissaoFuncionario> findByFuncionarioId(Long funcionarioId, Pageable pageable);

    Page<ComissaoFuncionario> findByEmpresaId(Long empresaId, Pageable pageable);
}

