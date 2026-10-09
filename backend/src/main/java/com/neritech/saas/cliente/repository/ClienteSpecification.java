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

    public static Specification<Cliente> buildSpecification(String busca, String nomeCompleto, String razaoSocial, String cpf, String cnpj, TipoCliente tipoCliente, StatusCliente status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Enforce Multi-tenant isolation
            predicates.add(criteriaBuilder.equal(root.get("empresaId"), TenantContext.getCurrentTenant()));

            // Adiciona fetch opcional para os contatos e endereços somente em queries de listagem
            if (Long.class != query.getResultType() && long.class != query.getResultType()) {
                // Aqui não precisamos necessariamente fazer fetch de contatos/enderecos, a não ser que a DTO demande.
                // Como não sabemos exatamente, deixamos apenas os joins normais ou nada se forem lazy.
            }

            if (busca != null && !busca.isBlank()) {
                String term = "%" + busca.trim().toLowerCase() + "%";
                String termDoc = busca.replaceAll("[^a-zA-Z0-9]", "");

                List<Predicate> searchPredicates = new ArrayList<>();
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeCompleto")), term));
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("razaoSocial")), term));
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeFantasia")), term));
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), term));

                if (!termDoc.isBlank()) {
                    searchPredicates.add(criteriaBuilder.equal(root.get("cpf"), termDoc));
                    searchPredicates.add(criteriaBuilder.equal(root.get("cnpj"), termDoc));
                }

                predicates.add(criteriaBuilder.or(searchPredicates.toArray(new Predicate[0])));
            } else {
                if (nomeCompleto != null && !nomeCompleto.isBlank()) {
                    String term = "%" + nomeCompleto.toLowerCase() + "%";
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeCompleto")), term),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("razaoSocial")), term),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeFantasia")), term)
                    ));
                }

                if (cpf != null && !cpf.isBlank()) {
                    String termDoc = cpf.replaceAll("[^a-zA-Z0-9]", "");
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("cpf"), termDoc),
                            criteriaBuilder.equal(root.get("cnpj"), termDoc)
                    ));
                }
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
