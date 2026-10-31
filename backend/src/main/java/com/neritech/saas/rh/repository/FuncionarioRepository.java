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
    Optional<Funcionario> findByIdAndEmpresaId(Long id, Long empresaId);

    Page<Funcionario> findByEmpresaId(Long empresaId, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("select f from Funcionario f left join f.cargo c left join f.departamento d " +
            "where f.empresaId=:empresaId and (:q = '' or lower(f.nomeCompleto) like lower(concat('%',:q,'%')) " +
            "or lower(f.matricula) like lower(concat('%',:q,'%'))) " +
            "and (:status is null or f.status=:status) and (:cargoId is null or f.cargoId=:cargoId) " +
            "and (:departamentoId is null or f.departamentoId=:departamentoId)")
    Page<Funcionario> search(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("q") String q,
            @org.springframework.data.repository.query.Param("status") StatusFuncionario status,
            @org.springframework.data.repository.query.Param("cargoId") Long cargoId,
            @org.springframework.data.repository.query.Param("departamentoId") Long departamentoId,
            Pageable pageable);

    @org.springframework.data.jpa.repository.Query("select f from Funcionario f left join f.cargo c " +
            "where f.empresaId=:empresaId " +
            "and (:q is null or lower(f.nomeCompleto) like lower(concat('%',:q,'%')) escape '!' " +
            "or lower(f.matricula) like lower(concat('%',:q,'%')) escape '!') " +
            "and (:nome = '' or lower(f.nomeCompleto) like lower(concat('%',:nome,'%')) escape '!') " +
            "and (:funcao = '' or lower(c.nome) like lower(concat('%',:funcao,'%')) escape '!') " +
            "and (:registro = '' or lower(f.matricula) like lower(concat('%',:registro,'%')) escape '!') " +
            "and (:status is null or f.status=:status) " +
            "and (:cargoId is null or f.cargoId=:cargoId) " +
            "and (:departamentoId is null or f.departamentoId=:departamentoId)")
    Page<Funcionario> searchAdvanced(
            @org.springframework.data.repository.query.Param("empresaId") Long empresaId,
            @org.springframework.data.repository.query.Param("q") String q,
            @org.springframework.data.repository.query.Param("status") StatusFuncionario status,
            @org.springframework.data.repository.query.Param("cargoId") Long cargoId,
            @org.springframework.data.repository.query.Param("departamentoId") Long departamentoId,
            @org.springframework.data.repository.query.Param("nome") String nome,
            @org.springframework.data.repository.query.Param("funcao") String funcao,
            @org.springframework.data.repository.query.Param("registro") String registro,
            Pageable pageable);

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
    boolean existsByEmpresaIdAndUsuarioIdAndIdNot(Long empresaId, Long usuarioId, Long id);
    long countByEmpresaIdAndCargoId(Long empresaId, Long cargoId);
    long countByEmpresaIdAndDepartamentoId(Long empresaId, Long departamentoId);
}

