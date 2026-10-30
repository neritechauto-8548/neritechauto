package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    java.util.Optional<OrdemServico> findByIdAndEmpresaId(Long id, Long empresaId);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select o from OrdemServico o where o.id = :id and o.empresaId = :empresaId")
    java.util.Optional<OrdemServico> lockForPatio(@org.springframework.data.repository.query.Param("id") Long id,
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId);
    Page<OrdemServico> findByEmpresaId(Long empresaId, Pageable pageable);
    Page<OrdemServico> findByEmpresaIdAndStatusId(Long empresaId, Long statusId, Pageable pageable);
    Page<OrdemServico> findByEmpresaIdAndClienteId(Long empresaId, Long clienteId, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT o FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.tipoOS IN :tipos AND (" +
            "LOWER(o.numeroOS) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "o.clienteId IN (SELECT c.id FROM Cliente c WHERE c.empresaId = :empresaId AND LOWER(c.nomeCompleto) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
            "o.veiculoId IN (SELECT v.id FROM Veiculo v WHERE v.empresaId = :empresaId AND LOWER(v.placa) LIKE LOWER(CONCAT('%', :search, '%')))" +
            ")")
    Page<OrdemServico> search(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                              @org.springframework.data.repository.query.Param("tipos") java.util.List<TipoOS> tipos,
                              @org.springframework.data.repository.query.Param("search") String search,
                              Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT o FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.tipoOS IN :tipos")
    Page<OrdemServico> findByEmpresaIdAndTiposIn(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                                                 @org.springframework.data.repository.query.Param("tipos") java.util.List<TipoOS> tipos,
                                                 Pageable pageable);

    Page<OrdemServico> findByClienteId(Long clienteId, Pageable pageable);
    Page<OrdemServico> findByClienteIdAndEmpresaId(Long clienteId, Long empresaId, Pageable pageable);
    Page<OrdemServico> findByVeiculoId(Long veiculoId, Pageable pageable);

    /** Acesso à linha do tempo de um veículo restrito à empresa autenticada.
     *  Busca também descrições de serviços/produtos, sem consultar outras empresas.
     */
    @org.springframework.data.jpa.repository.Query(
        value = "select o from OrdemServico o where o.empresaId = :empresaId and o.veiculoId = :veiculoId " +
            "and (:soAutorizadas = false or o.aprovadoCliente = true) " +
            "and ((:incluirAutorizados = true and (o.status.finalizaOS = true or o.aprovadoCliente = true)) " +
            "or (:incluirAutorizados = false and o.status.finalizaOS = true)) " +
            "and (:busca = '' or lower(o.numeroOS) like lower(concat('%', :busca, '%')) " +
            "or lower(coalesce(o.problemaRelatado, '')) like lower(concat('%', :busca, '%')) " +
            "or exists (select i.id from ItemOSServico i where i.ordemServico = o " +
            "and lower(coalesce(i.descricao, '')) like lower(concat('%', :busca, '%'))) " +
            "or exists (select p.id from ItemOSProduto p where p.ordemServico = o " +
            "and lower(coalesce(p.descricao, '')) like lower(concat('%', :busca, '%'))))",
        countQuery = "select count(o) from OrdemServico o where o.empresaId = :empresaId and o.veiculoId = :veiculoId " +
            "and (:soAutorizadas = false or o.aprovadoCliente = true) " +
            "and ((:incluirAutorizados = true and (o.status.finalizaOS = true or o.aprovadoCliente = true)) " +
            "or (:incluirAutorizados = false and o.status.finalizaOS = true)) " +
            "and (:busca = '' or lower(o.numeroOS) like lower(concat('%', :busca, '%')) " +
            "or lower(coalesce(o.problemaRelatado, '')) like lower(concat('%', :busca, '%')) " +
            "or exists (select i.id from ItemOSServico i where i.ordemServico = o " +
            "and lower(coalesce(i.descricao, '')) like lower(concat('%', :busca, '%'))) " +
            "or exists (select p.id from ItemOSProduto p where p.ordemServico = o " +
            "and lower(coalesce(p.descricao, '')) like lower(concat('%', :busca, '%'))))"
    )
    Page<OrdemServico> historicoPorVeiculo(
        @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
        @org.springframework.data.repository.query.Param("veiculoId") Long veiculoId,
        @org.springframework.data.repository.query.Param("incluirAutorizados") boolean incluirAutorizados,
        @org.springframework.data.repository.query.Param("soAutorizadas") boolean soAutorizadas,
        @org.springframework.data.repository.query.Param("busca") String busca,
        Pageable pageable);


    @org.springframework.data.jpa.repository.Query("SELECT o FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.numeroOS = :numeroOS AND o.clienteId IN (SELECT c.id FROM Cliente c WHERE c.empresaId = :empresaId AND c.cpf = :cpf)")
    java.util.Optional<OrdemServico> findByCpfAndNumeroOS(@org.springframework.data.repository.query.Param("cpf") String cpf,
                                                           @org.springframework.data.repository.query.Param("numeroOS") String numeroOS,
                                                           @org.springframework.data.repository.query.Param("empresaId") Long empresaId);

    boolean existsByEmpresaIdAndNumeroOS(Long empresaId, String numeroOS);
    long countByEmpresaId(Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = true")
    long countConcluidas(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.cancelaOS = true")
    long countCanceladas(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = false AND o.status.cancelaOS = false")
    long countAtivas(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = false AND o.status.cancelaOS = false AND COALESCE(o.dataAbertura, o.dataCadastro) BETWEEN :inicio AND :fim")
    long countAtivasAndPeriod(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                              @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
                              @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT AVG(o.valorTotal) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = true")
    java.math.BigDecimal calculateTicketMedio(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT AVG(o.valorTotal) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = true AND COALESCE(o.dataFimExecucao, o.dataAtualizacao, o.dataCadastro) BETWEEN :inicio AND :fim")
    java.math.BigDecimal calculateTicketMedioPeriod(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                                                    @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
                                                    @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(o.valorServicos) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = true AND COALESCE(o.dataFimExecucao, o.dataAtualizacao, o.dataCadastro) BETWEEN :inicio AND :fim")
    java.math.BigDecimal calculateServicosPeriod(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                                                 @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
                                                 @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = false AND o.status.cancelaOS = false AND o.dataPromessa < CURRENT_TIMESTAMP")
    long countAtrasadas(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.codigo IN :codigos AND COALESCE(o.dataAbertura, o.dataCadastro) BETWEEN :inicio AND :fim")
    long countByStatusCodesAndPeriod(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                                      @org.springframework.data.repository.query.Param("codigos") java.util.List<String> codigos,
                                      @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
                                      @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.codigo IN :codigos")
    long countByStatusCodes(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                            @org.springframework.data.repository.query.Param("codigos") java.util.List<String> codigos);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = :finalizaOS AND COALESCE(o.dataEntrega, o.dataFimExecucao, o.dataAtualizacao, o.dataCadastro) BETWEEN :inicio AND :fim")
    long countByFinalizaOSAndPeriod(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                                    @org.springframework.data.repository.query.Param("finalizaOS") boolean finalizaOS,
                                    @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
                                    @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = :finalizaOS")
    long countByFinalizaOS(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                           @org.springframework.data.repository.query.Param("finalizaOS") boolean finalizaOS);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.cancelaOS = :cancelaOS AND COALESCE(o.dataAbertura, o.dataCadastro) BETWEEN :inicio AND :fim")
    long countByCancelaOSAndPeriod(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                                   @org.springframework.data.repository.query.Param("cancelaOS") boolean cancelaOS,
                                   @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
                                   @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.cancelaOS = :cancelaOS")
    long countByCancelaOS(@org.springframework.data.repository.query.Param("empresaId") Long empresaId, @org.springframework.data.repository.query.Param("cancelaOS") boolean cancelaOS);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND COALESCE(o.dataAbertura, o.dataCadastro) BETWEEN :inicio AND :fim")
    long countByPeriod(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                       @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
                       @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM OrdemServico o WHERE o.empresaId = :empresaId AND o.status.finalizaOS = true AND COALESCE(o.dataEntrega, o.dataAtualizacao, o.dataCadastro) BETWEEN :inicio AND :fim")
    long countSaidasByPeriod(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
                              @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio,
                              @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);
}
