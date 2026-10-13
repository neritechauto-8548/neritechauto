package com.neritech.saas.cliente.repository;

import com.neritech.saas.cliente.domain.DocumentoCliente;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentoClienteRepository extends JpaRepository<DocumentoCliente, Long> {
    @Query("select d from DocumentoCliente d where d.cliente.id = :clienteId and d.empresaId = :empresaId")
    Page<DocumentoCliente> findScoped(@Param("clienteId") Long clienteId,
                                       @Param("empresaId") Long empresaId, Pageable pageable);

    @Query("select d from DocumentoCliente d where d.id = :id and d.cliente.id = :clienteId and d.empresaId = :empresaId")
    Optional<DocumentoCliente> findOneScoped(@Param("id") Long id, @Param("clienteId") Long clienteId,
                                            @Param("empresaId") Long empresaId);
}
