package com.neritech.saas.cliente.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.OrigemCliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {
    @Query("select c from Cliente c where c.id = :id and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Optional<Cliente> findByIdScoped(@Param("id") Long id);

    @Query("select c from Cliente c where c.cpf = :cpf and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Optional<Cliente> findByCpf(@Param("cpf") String cpf);

    @Query("select c from Cliente c where c.cnpj = :cnpj and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Optional<Cliente> findByCnpj(@Param("cnpj") String cnpj);

    @Query("select c from Cliente c where lower(c.nomeCompleto) like lower(concat('%', :nomeCompleto, '%')) and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Page<Cliente> findByNomeCompletoContainingIgnoreCase(@Param("nomeCompleto") String nomeCompleto, Pageable pageable);

    @Query("select c from Cliente c where lower(c.razaoSocial) like lower(concat('%', :razaoSocial, '%')) and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Page<Cliente> findByRazaoSocialContainingIgnoreCase(@Param("razaoSocial") String razaoSocial, Pageable pageable);

    @Query("select c from Cliente c where c.tipoCliente = :tipoCliente and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Page<Cliente> findByTipoCliente(@Param("tipoCliente") TipoCliente tipoCliente, Pageable pageable);

    @Query("select c from Cliente c where c.status = :status and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Page<Cliente> findByStatus(@Param("status") StatusCliente status, Pageable pageable);

    @Query("select c from Cliente c where c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    Page<Cliente> findAll(Pageable pageable);

    @Query("select case when (count(c) > 0) then true else false end from Cliente c where c.id = :id and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    boolean existsByIdScoped(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Cliente c where c.id = :id and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    int deleteByIdScoped(@Param("id") Long id);

    @Query("select c from Cliente c where MONTH(c.dataNascimento) = :mes and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    java.util.List<Cliente> findByMesAniversario(@Param("mes") Integer mes);

    @Query("select count(c) from Cliente c where c.status = :status and c.empresaId = ?#{T(com.neritech.saas.common.tenancy.TenantContext).getCurrentTenant()}")
    long countByStatus(@Param("status") StatusCliente status);

    @org.springframework.data.jpa.repository.Query(nativeQuery = true, value =
           "SELECT * FROM cliente c " +
           "WHERE c.empresa_id = :empresaId " +
           "AND (CAST(:status AS TEXT) IS NULL OR c.status = CAST(:status AS TEXT)) " +
           "AND (CAST(:tipoCliente AS TEXT) IS NULL OR c.tipo_cliente = CAST(:tipoCliente AS TEXT)) " +
           "AND (CAST(:origemCliente AS TEXT) IS NULL OR c.origem_cliente = CAST(:origemCliente AS TEXT)) " +
           "AND (CAST(:busca AS TEXT) IS NULL " +
           "     OR c.nome_completo ILIKE CONCAT('%', CAST(:busca AS TEXT), '%') " +
           "     OR c.razao_social ILIKE CONCAT('%', CAST(:busca AS TEXT), '%')) " +
           "AND (CAST(:dataInicio AS TIMESTAMP) IS NULL OR c.data_cadastro >= CAST(:dataInicio AS TIMESTAMP)) " +
           "AND (CAST(:dataFim AS TIMESTAMP) IS NULL OR c.data_cadastro <= CAST(:dataFim AS TIMESTAMP)) " +
           "ORDER BY COALESCE(c.nome_completo, c.razao_social) ASC")
    java.util.List<Cliente> findForRelatorio(
            @Param("empresaId") Long empresaId,
            @Param("status") String status,
            @Param("tipoCliente") String tipoCliente,
            @Param("origemCliente") String origemCliente,
            @Param("busca") String busca,
            @Param("dataInicio") java.time.LocalDateTime dataInicio,
            @Param("dataFim") java.time.LocalDateTime dataFim);
}
