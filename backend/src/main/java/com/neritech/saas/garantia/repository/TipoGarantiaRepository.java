package com.neritech.saas.garantia.repository;

import com.neritech.saas.garantia.domain.TipoGarantia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para TipoGarantia
 */
@Repository
public interface TipoGarantiaRepository extends JpaRepository<TipoGarantia, Long> {

    Page<TipoGarantia> findByEmpresaId(Long empresaId, Pageable pageable);

    Optional<TipoGarantia> findByIdAndEmpresaId(Long id, Long empresaId);

    List<TipoGarantia> findByEmpresaIdAndAtivo(Long empresaId, Boolean ativo);

    List<TipoGarantia> findByEmpresaIdAndPadraoServicos(Long empresaId, Boolean padraoServicos);

    List<TipoGarantia> findByEmpresaIdAndPadraoProdutos(Long empresaId, Boolean padraoProdutos);
}

