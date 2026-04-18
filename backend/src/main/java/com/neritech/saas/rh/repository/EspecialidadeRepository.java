package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    Page<Especialidade> findByEmpresaId(Long empresaId, Pageable pageable);

    List<Especialidade> findByEmpresaIdAndAtivo(Long empresaId, Boolean ativo);

    List<Especialidade> findByCategoria(String categoria);
}

