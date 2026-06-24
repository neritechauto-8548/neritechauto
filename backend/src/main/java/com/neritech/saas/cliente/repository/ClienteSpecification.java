package com.neritech.saas.cliente.repository;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import com.neritech.saas.common.tenancy.TenantContext;

public class ClienteSpecification {

    public static Specification<Cliente> buildSpecification(String nomeCompleto, String razaoSocial, String cpf, String cnpj, TipoCliente tipoCliente, StatusCliente status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Enforce Multi-tenant isolation
            predicates.add(criteriaBuilder.equal(root.get("empresaId"), TenantContext.getCurrentTenant()));

            // Adiciona fetch opcional para os contatos e endereços somente em queries de listagem
            if (Long.class != query.getResultType() && long.class != query.getResultType()) {
                // Aqui não precisamos necessariamente fazer fetch de contatos/enderecos, a não ser que a DTO demande.
                // Como não sabemos exatamente, deixamos apenas os joins normais ou nada se forem lazy.
            }

            if (nomeCompleto != null && !nomeCompleto.isBlank()) {
                String term = "%" + nomeCompleto.toLowerCase() + "%";
                Predicate orName = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeCompleto")), term),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("razaoSocial")), term),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeFantasia")), term)
                );
                predicates.add(orName);
            }

            if (cpf != null && !cpf.isBlank()) {
                String termDoc = cpf.replaceAll("[^a-zA-Z0-9]", "");
                Predicate orDoc = criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("cpf"), termDoc),
                        criteriaBuilder.equal(root.get("cnpj"), termDoc)
                );
                predicates.add(orDoc);
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
}
