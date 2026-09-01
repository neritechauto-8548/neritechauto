package com.neritech.saas.empresa.repository;

import com.neritech.saas.empresa.domain.Empresa;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByCnpj(String cnpj);

    Page<Empresa> findByRazaoSocialContainingIgnoreCase(String razaoSocial, Pageable pageable);

    Optional<Empresa> findByStripeCustomerId(String stripeCustomerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Empresa e where e.id = :id")
    Optional<Empresa> findByIdForUpdate(@Param("id") Long id);
}
