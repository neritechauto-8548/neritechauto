package com.neritech.saas.fiscal.repository;

import com.neritech.saas.fiscal.domain.AliquotaImposto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AliquotaImpostoRepository extends JpaRepository<AliquotaImposto, Long> {
    List<AliquotaImposto> findByUf(String uf);

    List<AliquotaImposto> findByPadraoTrue();
}
