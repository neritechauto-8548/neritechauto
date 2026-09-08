package com.neritech.saas.cliente.repository;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.common.tenancy.TenantContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class ClienteSpecification {

    private ClienteSpecification() {
    }

    public static Specification<Cliente> buildSpecification(
            String nomeCompleto,
            String razaoSocial,
            String cpf,
            String cnpj,
            TipoCliente tipoCliente,
            StatusCliente status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Long tenantId = TenantContext.getCurrentTenant();

            // Tenant is mandatory and always participates in the query predicate.
            predicates.add(criteriaBuilder.equal(root.get("empresaId"), tenantId));

            if (nomeCompleto != null && !nomeCompleto.isBlank()) {
                String term = "%" + nomeCompleto.trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeCompleto")), term),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("razaoSocial")), term),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeFantasia")), term)));
            }

            if (razaoSocial != null && !razaoSocial.isBlank()) {
                String term = "%" + razaoSocial.trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("razaoSocial")), term));
            }

            if (cpf != null && !cpf.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("cpf"), normalizeDocument(cpf)));
            }

            if (cnpj != null && !cnpj.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("cnpj"), normalizeDocument(cnpj)));
            }

            if (tipoCliente != null) {
                predicates.add(criteriaBuilder.equal(root.get("tipoCliente"), tipoCliente));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String normalizeDocument(String value) {
        return value.replaceAll("[^a-zA-Z0-9]", "");
    }
}
