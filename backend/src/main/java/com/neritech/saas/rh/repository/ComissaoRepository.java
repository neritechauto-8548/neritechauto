package com.neritech.saas.rh.repository;

import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import com.neritech.saas.rh.domain.Comissao;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComissaoRepository extends JpaRepository<Comissao, Long> {

    List<Comissao> findByEmpresaId(Long empresaId);

    Page<Comissao> findByEmpresaId(Long empresaId, Pageable pageable);

    List<Comissao> findByFuncionarioId(Long funcionarioId);

    List<Comissao> findByPeriodoReferencia(String periodoReferencia);

    List<Comissao> findByStatusPagamento(StatusPagamento statusPagamento);

    List<Comissao> findByEmpresaIdAndPeriodoReferenciaAndStatusPagamento(
            Long empresaId, String periodoReferencia, StatusPagamento statusPagamento);
}

