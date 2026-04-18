package com.neritech.saas.produtoServico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.neritech.saas.produtoServico.domain.Servico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findByEmpresaId(Long empresaId);

    boolean existsByNomeAndEmpresaId(String nome, Long empresaId);

    Page<Servico> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<Servico> findByEmpresaIdAndNomeContainingIgnoreCase(Long empresaId, String nome, Pageable pageable);

    Page<Servico> findByEmpresaIdAndAtivoTrue(Long empresaId, Pageable pageable);

    Page<Servico> findByEmpresaIdAndAtivoFalse(Long empresaId, Pageable pageable);

    @Query("SELECT s FROM Servico s WHERE s.empresaId = :empresaId AND LOWER(s.nome) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Servico> search(@Param("empresaId") Long empresaId, @Param("search") String search, Pageable pageable);
}
