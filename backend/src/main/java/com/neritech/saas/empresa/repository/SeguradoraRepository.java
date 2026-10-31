package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.Seguradora;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface SeguradoraRepository extends JpaRepository<Seguradora, Long> {
    Optional<Seguradora> findByIdAndEmpresaId(Long id, Long empresaId);

    @Query("""
        SELECT s FROM Seguradora s WHERE s.empresaId = :empresa
        AND (:ativo IS NULL OR s.ativo = :ativo)
        AND (:cnpj = '' OR s.cnpj LIKE CONCAT('%', :cnpj, '%'))
        AND (:nome = '' OR LOWER(s.razaoSocial) LIKE LOWER(CONCAT('%', :nome, '%')))
        AND (:fantasia = '' OR LOWER(COALESCE(s.nomeFantasia, '')) LIKE LOWER(CONCAT('%', :fantasia, '%')))
        AND (:busca = '' OR
            LOWER(s.razaoSocial) LIKE LOWER(CONCAT('%', :busca, '%')) OR
            LOWER(COALESCE(s.nomeFantasia, '')) LIKE LOWER(CONCAT('%', :busca, '%')) OR
            s.cnpj LIKE CONCAT('%', :busca, '%'))
        """)
    Page<Seguradora> buscar(@Param("empresa") Long empresa,
        @Param("ativo") Boolean ativo, @Param("busca") String busca,
        @Param("cnpj") String cnpj, @Param("nome") String nome,
        @Param("fantasia") String fantasia, Pageable page);

    @Query("""
        SELECT COUNT(s) > 0 FROM Seguradora s
         WHERE s.empresaId = :empresa AND s.cnpj = :cnpj
           AND (:id IS NULL OR s.id <> :id)
        """)
    boolean duplicado(@Param("empresa") Long empresa, @Param("cnpj") String cnpj,
                      @Param("id") Long id);
}
