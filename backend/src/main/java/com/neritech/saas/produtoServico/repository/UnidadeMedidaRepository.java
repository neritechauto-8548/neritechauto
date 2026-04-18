package com.neritech.saas.produtoServico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neritech.saas.produtoServico.domain.UnidadeMedida;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Long> {
    Page<UnidadeMedida> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<UnidadeMedida> findByEmpresaIdAndSigla(Long empresaId, String sigla);

    List<UnidadeMedida> findByEmpresaIdAndAtivoTrue(Long empresaId);

    Optional<UnidadeMedida> findBySigla(String sigla);

    List<UnidadeMedida> findByAtivoTrue();
}
