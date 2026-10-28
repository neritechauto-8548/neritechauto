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
        AND (:busca IS NULL OR :busca = '' OR
            LOWER(s.nomeFantasia) LIKE LOWER(CONCAT('%', :busca, '%')) OR
            LOWER(s.razaoSocial) LIKE LOWER(CONCAT('%', :busca, '%')) OR
            LOWER(s.cnpj) LIKE LOWER(CONCAT('%', :busca, '%')))
        """)
    Page<Seguradora> buscar(@Param("empresa") Long empresa, @Param("ativo") Boolean ativo,
                            @Param("busca") String busca, Pageable page);
    @Query("""
        SELECT COUNT(s) > 0 FROM Seguradora s
        WHERE s.empresaId = :empresa AND s.cnpj = :cnpj
            AND (:id IS NULL OR s.id <> :id)
        """)
    boolean duplicado(@Param("empresa") Long empresa, @Param("cnpj") String cnpj,
                      @Param("id") Long id);
}
