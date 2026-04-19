package com.neritech.saas.rh.repository;

import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import com.neritech.saas.rh.domain.Funcionario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Page<Funcionario> findByEmpresaId(Long empresaId, Pageable pageable);

    List<Funcionario> findByEmpresaIdAndStatus(Long empresaId, StatusFuncionario status);

    Optional<Funcionario> findByEmpresaIdAndMatricula(Long empresaId, String matricula);
    
    Optional<Funcionario> findByEmpresaIdAndCpf(Long empresaId, String cpf);

    Optional<Funcionario> findByCpf(String cpf);

    boolean existsByEmpresaIdAndMatricula(Long empresaId, String matricula);

    boolean existsByEmpresaIdAndCpf(Long empresaId, String cpf);

    boolean existsByCpf(String cpf);

    List<Funcionario> findByCargoId(Long cargoId);

    List<Funcionario> findByDepartamentoId(Long departamentoId);

    Optional<Funcionario> findByEmpresaIdAndUsuarioId(Long empresaId, Long usuarioId);
}

