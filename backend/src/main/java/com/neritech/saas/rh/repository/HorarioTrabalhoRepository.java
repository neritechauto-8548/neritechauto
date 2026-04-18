package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.HorarioTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioTrabalhoRepository extends JpaRepository<HorarioTrabalho, Long> {

    List<HorarioTrabalho> findByEmpresaId(Long empresaId);

    List<HorarioTrabalho> findByEmpresaIdAndAtivo(Long empresaId, Boolean ativo);
}

