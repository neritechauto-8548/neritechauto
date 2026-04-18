package com.neritech.saas.produtoServico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.neritech.saas.produtoServico.domain.Promocao;

@Repository
public interface PromocaoRepository extends JpaRepository<Promocao, Long> {
    Page<Promocao> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<Promocao> findByEmpresaIdAndAtivoTrue(Long empresaId, Pageable pageable);

    boolean existsByEmpresaIdAndCodigoCupom(Long empresaId, String codigoCupom);
}

