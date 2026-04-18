package com.neritech.saas.relatorios.repository;

import com.neritech.saas.relatorios.domain.Exportacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExportacaoRepository extends JpaRepository<Exportacao, Long> {
    List<Exportacao> findByEmpresaId(Long empresaId);

    List<Exportacao> findByEmpresaIdAndUsuarioSolicitante(Long empresaId, Long usuarioSolicitante);
}

