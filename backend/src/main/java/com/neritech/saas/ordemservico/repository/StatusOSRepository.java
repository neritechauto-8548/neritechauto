package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.StatusOS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatusOSRepository extends JpaRepository<StatusOS, Long> {
    Page<StatusOS> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<StatusOS> findByEmpresaIdAndAtivo(Long empresaId, Boolean ativo, Pageable pageable);

    Optional<StatusOS> findByEmpresaIdAndCodigo(Long empresaId, String codigo);

    boolean existsByEmpresaIdAndCodigo(Long empresaId, String codigo);
}

