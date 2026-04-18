package com.neritech.saas.agendamento.repository;

import com.neritech.saas.agendamento.domain.ListaEspera;
import com.neritech.saas.agendamento.domain.enums.StatusListaEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListaEsperaRepository extends JpaRepository<ListaEspera, Long> {
    List<ListaEspera> findByEmpresaId(Long empresaId);

    List<ListaEspera> findByClienteId(Long clienteId);

    List<ListaEspera> findByStatus(StatusListaEspera status);

    List<ListaEspera> findByEmpresaIdAndStatusOrderByPosicaoLista(Long empresaId, StatusListaEspera status);
}

