package com.neritech.saas.cliente.repository;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import com.neritech.saas.common.tenancy.TenantContext;

public class ClienteSpecification {

    public static Specification<Cliente> buildSpecification(String busca, String nomeCompleto, String razaoSocial, String cpf, String cnpj, TipoCliente tipoCliente, StatusCliente status, LocalDate dataInicio, LocalDate dataFim,
            String nomeFantasia, String contato, com.neritech.saas.rh.domain.enums.Sexo sexo, String placa) {
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
            }
            // Independent advanced filters must be applied WITH the global search,
            // not silently discarded when busca is present.
            {
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

            // Filtros avançados independentes com isolamento da empresa em subconsultas.
            if (nomeFantasia != null && !nomeFantasia.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeFantasia")),
                    "%" + nomeFantasia.trim().toLowerCase(java.util.Locale.ROOT) + "%"));
            }
            if (sexo != null) {
                predicates.add(criteriaBuilder.equal(root.get("sexo"), sexo));
            }
            if (contato != null && !contato.isBlank()) {
                var contactQuery = query.subquery(Long.class);
                var contact = contactQuery.from(com.neritech.saas.cliente.domain.ContatoCliente.class);
                String term = "%" + contato.trim().toLowerCase(java.util.Locale.ROOT) + "%";
                contactQuery.select(contact.get("cliente").get("id")).where(
                    criteriaBuilder.equal(contact.get("empresaId"), TenantContext.getCurrentTenant()),
                    criteriaBuilder.equal(contact.get("cliente").get("id"), root.get("id")),
                    criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(contact.get("contato")), term),
                        criteriaBuilder.like(criteriaBuilder.lower(contact.get("nome")), term)));
                predicates.add(criteriaBuilder.exists(contactQuery));
            }
            if (placa != null && !placa.isBlank()) {
                var plateQuery = query.subquery(Long.class);
                var vehicle = plateQuery.from(com.neritech.saas.veiculo.domain.Veiculo.class);
                String plate = "%" + placa.trim().toLowerCase(java.util.Locale.ROOT) + "%";
                plateQuery.select(vehicle.get("cliente").get("id")).where(
                    criteriaBuilder.equal(vehicle.get("empresaId"), TenantContext.getCurrentTenant()),
                    criteriaBuilder.equal(vehicle.get("cliente").get("id"), root.get("id")),
                    criteriaBuilder.like(criteriaBuilder.lower(vehicle.get("placa")), plate));
                predicates.add(criteriaBuilder.exists(plateQuery));
            }

            if (tipoCliente != null) {
                predicates.add(criteriaBuilder.equal(root.get("tipoCliente"), tipoCliente));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (dataInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataCadastro"), dataInicio.atStartOfDay()));
            }
            if (dataFim != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("dataCadastro"), dataFim.plusDays(1).atStartOfDay()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    /** API de filtros anterior, mantida para compatibilidade. */
    public static Specification<Cliente> buildSpecification(String busca, String nomeCompleto, String razaoSocial,
        String cpf, String cnpj, TipoCliente tipoCliente, StatusCliente status, LocalDate dataInicio, LocalDate dataFim) {
        return buildSpecification(busca, nomeCompleto, razaoSocial, cpf, cnpj, tipoCliente, status, dataInicio, dataFim,
                null, null, null, null);
    }
}
