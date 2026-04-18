package com.neritech.saas.fiscal.repository;

import com.neritech.saas.fiscal.domain.NotaFiscalInterna;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NotaFiscalInternaRepository extends JpaRepository<NotaFiscalInterna, Long> {
    Optional<NotaFiscalInterna> findTopByEmpresaIdOrderByNumeroDesc(Long empresaId);
    Optional<NotaFiscalInterna> findTopByOrdemServicoIdOrderByIdDesc(Long osId);
}
