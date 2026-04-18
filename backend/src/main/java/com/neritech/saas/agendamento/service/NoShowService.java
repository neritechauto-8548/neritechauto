package com.neritech.saas.agendamento.service;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.NoShow;
import com.neritech.saas.agendamento.dto.NoShowRequest;
import com.neritech.saas.agendamento.dto.NoShowResponse;
import com.neritech.saas.agendamento.mapper.NoShowMapper;
import com.neritech.saas.agendamento.repository.AgendamentoRepository;
import com.neritech.saas.agendamento.repository.NoShowRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoShowService {

    private final NoShowRepository repository;
    private final AgendamentoRepository agendamentoRepository;
    private final NoShowMapper mapper;

    @Transactional
    public NoShowResponse criar(NoShowRequest request) {
        NoShow noShow = mapper.toEntity(request);

        Agendamento agendamento = agendamentoRepository.findById(request.agendamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Agendamento nÃ£o encontrado"));
        noShow.setAgendamento(agendamento);

        if (request.novoAgendamentoId() != null) {
            Agendamento novoAgendamento = agendamentoRepository.findById(request.novoAgendamentoId())
                    .orElseThrow(() -> new EntityNotFoundException("Novo agendamento nÃ£o encontrado"));
            noShow.setNovoAgendamento(novoAgendamento);
        }

        NoShow salvo = repository.save(noShow);
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public NoShowResponse buscarPorId(Long id) {
        NoShow noShow = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de no-show nÃ£o encontrado"));
        return mapper.toResponse(noShow);
    }

    @Transactional(readOnly = true)
    public List<NoShowResponse> listarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Registro de no-show nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
