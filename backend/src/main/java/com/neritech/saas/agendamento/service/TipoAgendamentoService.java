package com.neritech.saas.agendamento.service;

import com.neritech.saas.agendamento.domain.TipoAgendamento;
import com.neritech.saas.agendamento.dto.TipoAgendamentoRequest;
import com.neritech.saas.agendamento.dto.TipoAgendamentoResponse;
import com.neritech.saas.agendamento.mapper.TipoAgendamentoMapper;
import com.neritech.saas.agendamento.repository.TipoAgendamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoAgendamentoService {

    private final TipoAgendamentoRepository repository;
    private final TipoAgendamentoMapper mapper;

    @Transactional
    public TipoAgendamentoResponse criar(TipoAgendamentoRequest request) {
        TipoAgendamento tipoAgendamento = mapper.toEntity(request);
        tipoAgendamento.setCriadoPor(1L); // TODO: Obter do contexto de seguranÃƒÂ§a
        TipoAgendamento salvo = repository.save(tipoAgendamento);
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public TipoAgendamentoResponse buscarPorId(Long id) {
        TipoAgendamento tipoAgendamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de agendamento nÃƒÂ£o encontrado"));
        return mapper.toResponse(tipoAgendamento);
    }

    @Transactional(readOnly = true)
    public List<TipoAgendamentoResponse> listarPorEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public TipoAgendamentoResponse atualizar(Long id, TipoAgendamentoRequest request) {
        TipoAgendamento tipoAgendamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de agendamento nÃƒÂ£o encontrado"));

        tipoAgendamento.setNome(request.nome());
        tipoAgendamento.setDescricao(request.descricao());
        tipoAgendamento.setDuracaoPadraoMinutos(request.duracaoPadraoMinutos());
        tipoAgendamento.setAtivo(request.ativo());

        TipoAgendamento atualizado = repository.save(tipoAgendamento);
        return mapper.toResponse(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de agendamento nÃƒÂ£o encontrado");
        }
        repository.deleteById(id);
    }
}
