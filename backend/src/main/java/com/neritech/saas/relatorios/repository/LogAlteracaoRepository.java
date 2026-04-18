package com.neritech.saas.relatorios.repository;

import com.neritech.saas.relatorios.domain.LogAlteracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogAlteracaoRepository extends JpaRepository<LogAlteracao, Long> {
    List<LogAlteracao> findByEmpresaId(Long empresaId);

    List<LogAlteracao> findByEmpresaIdAndTabelaAfetada(Long empresaId, String tabelaAfetada);

    List<LogAlteracao> findByEmpresaIdAndUsuarioResponsavel(Long empresaId, Long usuarioResponsavel);
}

