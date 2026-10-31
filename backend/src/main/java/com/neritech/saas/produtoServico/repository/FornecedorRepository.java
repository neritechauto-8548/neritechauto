package com.neritech.saas.produtoServico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.neritech.saas.produtoServico.domain.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Page<Fornecedor> findByEmpresaId(Long empresaId, Pageable pageable);
    @org.springframework.data.jpa.repository.Query("""
      SELECT (COUNT(f) > 0) FROM Fornecedor f
      WHERE f.empresaId = :tenant AND f.cpf = :cpf
        AND (:excluding IS NULL OR f.id <> :excluding)
      """)
    boolean existsOtherCpf(@org.springframework.data.repository.query.Param("tenant") Long tenant,
            @org.springframework.data.repository.query.Param("cpf") String cpf,
            @org.springframework.data.repository.query.Param("excluding") Long excluding);

    @org.springframework.data.jpa.repository.Query("""
      SELECT (COUNT(f) > 0) FROM Fornecedor f
      WHERE f.empresaId = :tenant AND f.cnpj = :cnpj
        AND (:excluding IS NULL OR f.id <> :excluding)
      """)
    boolean existsOtherCnpj(@org.springframework.data.repository.query.Param("tenant") Long tenant,
            @org.springframework.data.repository.query.Param("cnpj") String cnpj,
            @org.springframework.data.repository.query.Param("excluding") Long excluding);


    java.util.Optional<Fornecedor> findByEmpresaIdAndCpf(Long empresaId, String cpf);
    java.util.Optional<Fornecedor> findByEmpresaIdAndCnpj(Long empresaId, String cnpj);

    boolean existsByEmpresaIdAndCpf(Long empresaId, String cpf);

    boolean existsByEmpresaIdAndCnpj(Long empresaId, String cnpj);

    Page<Fornecedor> findByEmpresaIdAndAtivoTrue(Long empresaId, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT f FROM Fornecedor f WHERE f.empresaId = :empresaId AND " +
            "(LOWER(f.nomeFantasia) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(f.razaoSocial) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(f.cpf) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(f.cnpj) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Fornecedor> search(Long empresaId, String search, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("""
        SELECT f FROM Fornecedor f
         WHERE f.empresaId = :empresaId
           AND (:search IS NULL OR :search = '' OR
                LOWER(f.nomeFantasia) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(f.razaoSocial) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(f.cpf) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(f.cnpj) LIKE LOWER(CONCAT('%', :search, '%')))
           AND (:ativo IS NULL OR f.ativo = :ativo)
           AND (:cidade IS NULL OR :cidade = '' OR
                LOWER(f.cidade) LIKE LOWER(CONCAT('%', :cidade, '%')))
        """)
    Page<Fornecedor> filter(@org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("search") String search,
            @org.springframework.data.repository.query.Param("ativo") Boolean ativo,
            @org.springframework.data.repository.query.Param("cidade") String cidade,
            Pageable pageable);

    @org.springframework.data.jpa.repository.Query("""
        SELECT f FROM Fornecedor f
         WHERE f.empresaId=:empresaId
           AND (:ativo IS NULL OR f.ativo=:ativo)
           AND (:cidade='' OR LOWER(f.cidade) LIKE LOWER(CONCAT('%',:cidade,'%')))
           AND (:documento='' OR f.cnpj LIKE CONCAT('%',:documento,'%')
                              OR f.cpf LIKE CONCAT('%',:documento,'%'))
           AND (:nome='' OR LOWER(f.razaoSocial) LIKE LOWER(CONCAT('%',:nome,'%'))
                       OR LOWER(f.nomeFantasia) LIKE LOWER(CONCAT('%',:nome,'%')))
           AND (:search='' OR LOWER(f.razaoSocial) LIKE LOWER(CONCAT('%',:search,'%'))
                        OR LOWER(f.nomeFantasia) LIKE LOWER(CONCAT('%',:search,'%'))
                        OR f.cnpj LIKE CONCAT('%',:search,'%')
                        OR f.cpf LIKE CONCAT('%',:search,'%'))
        """)
    Page<Fornecedor> filterDetalhado(
        @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
        @org.springframework.data.repository.query.Param("search") String search,
        @org.springframework.data.repository.query.Param("ativo") Boolean ativo,
        @org.springframework.data.repository.query.Param("cidade") String cidade,
        @org.springframework.data.repository.query.Param("documento") String documento,
        @org.springframework.data.repository.query.Param("nome") String nome, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("""
        SELECT f FROM Fornecedor f WHERE f.empresaId = :tenant
        AND f.transportadora = true
        AND (:ativo IS NULL OR
          (:ativo = true AND f.ativo = true AND f.transportadoraAtiva = true) OR
          (:ativo = false AND (f.ativo = false OR f.transportadoraAtiva = false)))
        AND (:regiao IS NULL OR :regiao = '' OR
             LOWER(f.cidade) LIKE LOWER(CONCAT('%', :regiao, '%')) OR
             LOWER(f.estado) = LOWER(:regiao))
        AND (:busca IS NULL OR :busca = '' OR
             LOWER(f.nomeFantasia) LIKE LOWER(CONCAT('%', :busca, '%')) OR
             LOWER(f.razaoSocial) LIKE LOWER(CONCAT('%', :busca, '%')) OR
             LOWER(f.cpf) LIKE LOWER(CONCAT('%', :busca, '%')) OR
             LOWER(f.cnpj) LIKE LOWER(CONCAT('%', :busca, '%')))
        """)
    Page<Fornecedor> listarTransportadoras(
        @org.springframework.data.repository.query.Param("tenant") Long tenant,
        @org.springframework.data.repository.query.Param("ativo") Boolean ativo,
        @org.springframework.data.repository.query.Param("regiao") String regiao,
        @org.springframework.data.repository.query.Param("busca") String busca,
        Pageable page);
}

