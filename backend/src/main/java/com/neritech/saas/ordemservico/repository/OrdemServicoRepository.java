package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    Page<OrdemServico> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<OrdemServico> findByEmpresaIdAndStatusId(Long empresaId, Long statusId, Pageable pageable);

    Page<OrdemServico> findByEmpresaIdAndTipoOS(Long empresaId, TipoOS tipoOS, Pageable pageable);

    Page<OrdemServico> findByClienteId(Long clienteId, Pageable pageable);

    Page<OrdemServico> findByVeiculoId(Long veiculoId, Pageable pageable);
    
    @org.springframework.data.jpa.repository.Query("SELECT o FROM OrdemServico o WHERE o.numeroOS = :numeroOS AND o.clienteId IN (SELECT c.id FROM Cliente c WHERE c.cpf = :cpf)")
    java.util.Optional<OrdemServico> findByCpfAndNumeroOS(@org.springframework.data.repository.query.Param("cpf") String cpf, @org.springframework.data.repository.query.Param("numeroOS") String numeroOS);

    boolean existsByEmpresaIdAndNumeroOS(Long empresaId, String numeroOS);

    long countByEmpresaId(Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = true")
    long countConcluidas(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.cancelaOS = true")
    long countCanceladas(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = false AND o.status.cancelaOS = false")
    long countAtivas(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT AVG(o.valorTotal) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = true")
    java.math.BigDecimal calculateTicketMedio(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);
    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = false AND o.status.cancelaOS = false AND o.dataPromessa < CURRENT_TIMESTAMP")
    long countAtrasadas(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);
}
