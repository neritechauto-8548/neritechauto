package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    java.util.Optional<OrdemServico> findByIdAndEmpresaId(Long id, Long empresaId);

    Page<OrdemServico> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<OrdemServico> findByEmpresaIdAndStatusId(Long empresaId, Long statusId, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT o FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.tipoOS IN :tipos AND (" +
            "LOWER(o.numeroOS) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "o.clienteId IN (SELECT c.id FROM Cliente c WHERE LOWER(c.nomeCompleto) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
            "o.veiculoId IN (SELECT v.id FROM Veiculo v WHERE LOWER(v.placa) LIKE LOWER(CONCAT('%', :search, '%')))" +
            ")")
    Page<OrdemServico> search(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("tipos") java.util.List<TipoOS> tipos,
            @org.springframework.data.repository.query.Param("search") String search,
            Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT o FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.tipoOS IN :tipos")
    Page<OrdemServico> findByEmpresaIdAndTiposIn(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("tipos") java.util.List<TipoOS> tipos,
            Pageable pageable);

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

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.codigo IN :codigos AND COALESCE(o.dataAbertura, o.dataCadastro) BETWEEN :inicio AND :fim")
    long countByStatusCodesAndPeriod(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("codigos") java.util.List<String> codigos,
            @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
            @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.codigo IN :codigos")
    long countByStatusCodes(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("codigos") java.util.List<String> codigos);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = :finalizaOS AND COALESCE(o.dataAbertura, o.dataCadastro) BETWEEN :inicio AND :fim")
    long countByFinalizaOSAndPeriod(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("finalizaOS") boolean finalizaOS,
            @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
            @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = :finalizaOS")
    long countByFinalizaOS(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("finalizaOS") boolean finalizaOS);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.cancelaOS = :cancelaOS AND COALESCE(o.dataAbertura, o.dataCadastro) BETWEEN :inicio AND :fim")
    long countByCancelaOSAndPeriod(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("cancelaOS") boolean cancelaOS,
            @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
            @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.cancelaOS = :cancelaOS")
    long countByCancelaOS(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("cancelaOS") boolean cancelaOS);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND COALESCE(o.dataAbertura, o.dataCadastro) BETWEEN :inicio AND :fim")
    long countByPeriod(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
            @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = true AND COALESCE(o.dataEntrega, o.dataAtualizacao, o.dataCadastro) BETWEEN :inicio AND :fim")
    long countSaidasByPeriod(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
            @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);
}
