package com.neritech.saas.veiculo.repository;

import com.neritech.saas.veiculo.domain.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, JpaSpecificationExecutor<Veiculo> {

    Optional<Veiculo> findByIdAndEmpresaId(Long id, Long empresaId);

    boolean existsByIdAndEmpresaId(Long id, Long empresaId);

    Optional<Veiculo> findByEmpresaIdAndPlaca(Long empresaId, String placa);

    List<Veiculo> findByEmpresaId(Long empresaId);

    List<Veiculo> findByClienteIdAndEmpresaId(Long clienteId, Long empresaId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Veiculo v where v.id = :id and v.empresaId = :empresaId")
    int deleteByIdAndEmpresaId(@Param("id") Long id, @Param("empresaId") Long empresaId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Veiculo v where v.cliente.id = :clienteId and v.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    int deleteByClienteIdScoped(@Param("clienteId") Long clienteId);
}
