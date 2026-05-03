package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long>, JpaSpecificationExecutor<Localizacao> {
    List<Localizacao> findByEmpresaId(Long empresaId);
    Page<Localizacao> findByEmpresaId(Long empresaId, Pageable pageable);
}

