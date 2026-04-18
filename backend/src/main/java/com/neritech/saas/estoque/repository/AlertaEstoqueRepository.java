package com.neritech.saas.estoque.repository;


import com.neritech.saas.estoque.domain.AlertaEstoque;
import com.neritech.saas.estoque.domain.enums.StatusAlerta;
import com.neritech.saas.estoque.domain.enums.TipoAlerta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaEstoqueRepository extends JpaRepository<AlertaEstoque, Long> {

    Page<AlertaEstoque> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<AlertaEstoque> findByEmpresaIdAndStatus(Long empresaId, StatusAlerta status, Pageable pageable);

    Page<AlertaEstoque> findByEmpresaIdAndTipoAlerta(Long empresaId, TipoAlerta tipo, Pageable pageable);
}

