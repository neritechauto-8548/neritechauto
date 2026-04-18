package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.ModeloDocumento;
import com.neritech.saas.empresa.domain.enums.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ModeloDocumentoRepository
        extends JpaRepository<ModeloDocumento, Long>, JpaSpecificationExecutor<ModeloDocumento> {
    List<ModeloDocumento> findByEmpresaId(Long empresaId);

    List<ModeloDocumento> findByEmpresaIdAndTipoDocumento(Long empresaId, TipoDocumento tipoDocumento);

    List<ModeloDocumento> findByEmpresaIdAndAtivoTrue(Long empresaId);
}

