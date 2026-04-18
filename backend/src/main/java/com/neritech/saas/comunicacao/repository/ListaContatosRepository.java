package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.ListaContatos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaContatosRepository extends JpaRepository<ListaContatos, Long> {

    Page<ListaContatos> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<ListaContatos> findByEmpresaIdAndAtiva(Long empresaId, Boolean ativa, Pageable pageable);

    boolean existsByEmpresaIdAndNome(Long empresaId, String nome);
}

