package com.neritech.saas.rh.repository;


import com.neritech.saas.rh.domain.DocumentoFuncionario;
import com.neritech.saas.rh.domain.enums.TipoDocumento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoFuncionarioRepository extends JpaRepository<DocumentoFuncionario, Long> {

    List<DocumentoFuncionario> findByFuncionarioId(Long funcionarioId);

    List<DocumentoFuncionario> findByTipoDocumento(TipoDocumento tipoDocumento);

    List<DocumentoFuncionario> findByFuncionarioIdAndTipoDocumento(Long funcionarioId, TipoDocumento tipoDocumento);

    List<DocumentoFuncionario> findByFuncionarioIdAndVerificado(Long funcionarioId, Boolean verificado);
}
