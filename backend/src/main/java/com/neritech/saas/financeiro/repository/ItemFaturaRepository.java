package com.neritech.saas.financeiro.repository;

import com.neritech.saas.financeiro.domain.ItemFatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemFaturaRepository extends JpaRepository<ItemFatura, Long>, JpaSpecificationExecutor<ItemFatura> {
    List<ItemFatura> findByFaturaId(Long faturaId);
}
