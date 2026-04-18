package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.Questionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionarioRepository extends JpaRepository<Questionario, Long>, JpaSpecificationExecutor<Questionario> {
    Page<Questionario> findByEmpresaId(Long empresaId, Pageable pageable);
}

