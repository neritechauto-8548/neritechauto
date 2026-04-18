package com.neritech.saas.agendamento.service;

import com.neritech.saas.agendamento.domain.ListaEspera;
import com.neritech.saas.agendamento.domain.TipoAgendamento;
import com.neritech.saas.agendamento.dto.ListaEsperaRequest;
import com.neritech.saas.agendamento.dto.ListaEsperaResponse;
import com.neritech.saas.agendamento.mapper.ListaEsperaMapper;
import com.neritech.saas.agendamento.repository.ListaEsperaRepository;
import com.neritech.saas.agendamento.repository.TipoAgendamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListaEsperaService {

    private final ListaEsperaRepository repository;
    private final TipoAgendamentoRepository tipoAgendamentoRepository;
    private final ListaEsperaMapper mapper;

    @Transactional
    public ListaEsperaResponse criar(ListaEsperaRequest request) {
        ListaEspera listaEspera = mapper.toEntity(request);

        if (request.tipoAgendamentoId() != null) {
            TipoAgendamento tipoAgendamento = tipoAgendamentoRepository.findById(request.tipoAgendamentoId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de agendamento nÃƒÂ£o encontrado"));
            listaEspera.setTipoAgendamento(tipoAgendamento);
        }

        listaEspera.setCadastradoPor(1L); // TODO: Obter do contexto de seguranÃƒÂ§a

        ListaEspera salvo = repository.save(listaEspera);
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public ListaEsperaResponse buscarPorId(Long id) {
        ListaEspera listaEspera = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro na lista de espera nÃƒÂ£o encontrado"));
        return mapper.toResponse(listaEspera);
    }

    @Transactional(readOnly = true)
    public List<ListaEsperaResponse> listarPorEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Registro na lista de espera nÃƒÂ£o encontrado");
        }
        repository.deleteById(id);
    }
}
