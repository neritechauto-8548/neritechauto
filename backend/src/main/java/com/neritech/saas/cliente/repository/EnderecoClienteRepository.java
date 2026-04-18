package com.neritech.saas.cliente.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.neritech.saas.cliente.domain.EnderecoCliente;

import java.util.Optional;

@Repository
public interface EnderecoClienteRepository extends JpaRepository<EnderecoCliente, Long> {

    @Query("select e from EnderecoCliente e where e.id = :id and e.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Optional<EnderecoCliente> findByIdScoped(@Param("id") Long id);

    @Query("select e from EnderecoCliente e where e.cliente.id = :clienteId and e.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Page<EnderecoCliente> findByClienteId(@Param("clienteId") Long clienteId, Pageable pageable);

    @Query("select case when (count(e) > 0) then true else false end from EnderecoCliente e where e.id = :id and e.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    boolean existsByIdScoped(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from EnderecoCliente e where e.id = :id and e.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    int deleteByIdScoped(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from EnderecoCliente e where e.cliente.id = :clienteId and e.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    int deleteByClienteIdScoped(@Param("clienteId") Long clienteId);

}
