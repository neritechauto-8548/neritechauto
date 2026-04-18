package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.TemplateComunicacao;
import com.neritech.saas.comunicacao.domain.enums.TipoTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateComunicacaoRepository extends JpaRepository<TemplateComunicacao, Long> {

    Page<TemplateComunicacao> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<TemplateComunicacao> findByEmpresaIdAndAtivo(Long empresaId, Boolean ativo, Pageable pageable);

    Page<TemplateComunicacao> findByEmpresaIdAndTipoTemplate(Long empresaId, TipoTemplate tipoTemplate,
            Pageable pageable);

    boolean existsByEmpresaIdAndNome(Long empresaId, String nome);
}

