package com.neritech.saas.veiculo.repository;

import com.neritech.saas.veiculo.domain.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, JpaSpecificationExecutor<Veiculo> {
    Optional<Veiculo> findByEmpresaIdAndPlaca(Long empresaId, String placa);

    List<Veiculo> findByEmpresaId(Long empresaId);

    List<Veiculo> findByClienteId(Long clienteId);

    @org.springframework.data.jpa.repository.Modifying(clearAutomatically = true, flushAutomatically = true)
    @org.springframework.data.jpa.repository.Query("delete from Veiculo v where v.cliente.id = :clienteId and v.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    int deleteByClienteIdScoped(@org.springframework.data.repository.query.Param("clienteId") Long clienteId);
}

