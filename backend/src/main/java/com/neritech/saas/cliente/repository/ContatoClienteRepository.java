package com.neritech.saas.cliente.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.neritech.saas.cliente.domain.ContatoCliente;

import java.util.Optional;

@Repository
public interface ContatoClienteRepository extends JpaRepository<ContatoCliente, Long> {

    @Query("select c from ContatoCliente c where c.id = :id and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Optional<ContatoCliente> findByIdScoped(@Param("id") Long id);

    @Query("select c from ContatoCliente c where c.cliente.id = :clienteId and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Page<ContatoCliente> findByClienteId(@Param("clienteId") Long clienteId, Pageable pageable);

    @Query("select case when (count(c) > 0) then true else false end from ContatoCliente c where c.id = :id and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    boolean existsByIdScoped(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ContatoCliente c where c.id = :id and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    int deleteByIdScoped(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ContatoCliente c where c.cliente.id = :clienteId and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    int deleteByClienteIdScoped(@Param("clienteId") Long clienteId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ContatoCliente c set c.principal = false where c.cliente.id = :clienteId and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    int clearPrincipal(@Param("clienteId") Long clienteId);
}
