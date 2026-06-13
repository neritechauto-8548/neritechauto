package com.neritech.saas.produtoServico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.neritech.saas.produtoServico.domain.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Page<Fornecedor> findByEmpresaId(Long empresaId, Pageable pageable);

    boolean existsByEmpresaIdAndCpf(Long empresaId, String cpf);

    boolean existsByEmpresaIdAndCnpj(Long empresaId, String cnpj);

    Page<Fornecedor> findByEmpresaIdAndAtivoTrue(Long empresaId, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT f FROM Fornecedor f WHERE f.empresaId = :empresaId AND " +
            "(LOWER(f.nomeFantasia) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(f.razaoSocial) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(f.cpf) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(f.cnpj) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Fornecedor> search(Long empresaId, String search, Pageable pageable);
}

