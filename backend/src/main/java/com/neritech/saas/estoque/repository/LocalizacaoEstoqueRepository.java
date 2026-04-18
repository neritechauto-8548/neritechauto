package com.neritech.saas.estoque.repository;

import com.neritech.saas.estoque.domain.LocalizacaoEstoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LocalizacaoEstoqueRepository extends JpaRepository<LocalizacaoEstoque, Long> {

    Page<LocalizacaoEstoque> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<LocalizacaoEstoque> findByEmpresaIdAndAtivoTrue(Long empresaId, Pageable pageable);

    Optional<LocalizacaoEstoque> findByEmpresaIdAndCodigo(Long empresaId, String codigo);

    boolean existsByEmpresaIdAndCodigo(Long empresaId, String codigo);
}

