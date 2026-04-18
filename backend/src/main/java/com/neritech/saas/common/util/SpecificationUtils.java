package com.neritech.saas.common.util;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * UtilitÃ¡rios para construÃ§Ã£o de Specifications do Spring Data JPA
 */
public class SpecificationUtils {

    /**
     * Cria uma specification para busca com LIKE ignorando case em mÃºltiplos campos
     *
     * @param value Valor a ser buscado
     * @param fields Campos onde buscar
     * @param <T> Tipo da entidade
     * @return Specification para busca
     */
    public static <T> Specification<T> likeIgnoreCase(String value, String... fields) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            
            String searchValue = "%" + value.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            
            for (String field : fields) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(field)), 
                        searchValue
                    )
                );
            }
            
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Cria uma specification para busca com igualdade exata
     *
     * @param field Campo a ser comparado
     * @param value Valor a ser comparado
     * @param <T> Tipo da entidade
     * @return Specification para busca
     */
    public static <T> Specification<T> equals(String field, Object value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(field), value);
        };
    }

    /**
     * Cria uma specification para busca com LIKE ignorando case em um campo
     *
     * @param field Campo onde buscar
     * @param value Valor a ser buscado
     * @param <T> Tipo da entidade
     * @return Specification para busca
     */
    public static <T> Specification<T> likeIgnoreCase(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            
            String searchValue = "%" + value.toLowerCase() + "%";
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get(field)), 
                searchValue
            );
        };
    }
}
