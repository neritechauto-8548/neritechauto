package com.neritech.saas.comunicacao.service;

import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.comunicacao.domain.*;
import com.neritech.saas.comunicacao.domain.enums.TipoItemQuestionario;
import com.neritech.saas.comunicacao.dto.PesquisaRespostaRequest;
import com.neritech.saas.comunicacao.dto.PesquisaRespostaResponse;
import com.neritech.saas.comunicacao.dto.PesquisaResumoResponse;
import com.neritech.saas.comunicacao.repository.*;
import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PesquisaRespostaService {
    private final QuestionarioRepository questionarios;
    private final ItQuestionarioRepository perguntas;
    private final ClienteRepository clientes;
    private final PesquisaRespostaRepository repository;

    public PesquisaRespostaService(QuestionarioRepository questionarios,
            ItQuestionarioRepository perguntas, ClienteRepository clientes,
            PesquisaRespostaRepository repository) {
        this.questionarios = questionarios;
        this.perguntas = perguntas;
        this.clientes = clientes;
        this.repository = repository;
    }

    private Questionario requireQuestionario(Long questionarioId) {
        Long empresaId = TenantAccess.requireCurrent();
        Questionario q = questionarios.findById(questionarioId)
            .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado"));
        TenantAccess.require(q.getEmpresaId());
        return q;
    }

    @Transactional(readOnly = true)
    public PesquisaResumoResponse resumo(Long questionarioId) {
        requireQuestionario(questionarioId);
        return new PesquisaResumoResponse(questionarioId,
            repository.countByQuestionarioIdAndEmpresaId(questionarioId, TenantAccess.requireCurrent()));
    }

    @Transactional(readOnly = true)
    public Page<PesquisaRespostaResponse> listar(Long questionarioId, Pageable pageable) {
        requireQuestionario(questionarioId);
        return repository.findByQuestionarioIdAndEmpresaId(questionarioId,
            TenantAccess.requireCurrent(), pageable).map(PesquisaRespostaResponse::from);
    }

    public PesquisaRespostaResponse registrar(Long questionarioId, PesquisaRespostaRequest input) {
        Questionario questionario = requireQuestionario(questionarioId);
        Long empresaId = TenantAccess.requireCurrent();
        UUID chave;
        try { chave = UUID.fromString(input.chaveIdempotencia()); }
        catch (IllegalArgumentException ex) { throw new BusinessException("Chave de idempotência inválida."); }

        String normalizedKey = chave.toString();
        Optional<PesquisaResposta> existing = repository.findByEmpresaIdAndChaveIdempotencia(empresaId, normalizedKey);
        if (existing.isPresent()) {
            PesquisaResposta r = existing.get();
            if (!r.getQuestionario().getId().equals(questionarioId)
                || !r.getCliente().getId().equals(input.clienteId())) {
                throw new BusinessException("A chave informada pertence a outra resposta.");
            }
            return PesquisaRespostaResponse.from(r);
        }
        if (!Boolean.TRUE.equals(questionario.getSnAtivo())) {
            throw new BusinessException("Questionário inativo não recebe respostas.");
        }
        var cliente = clientes.findByIdScoped(input.clienteId())
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado nesta empresa"));
        List<ItQuestionario> items = perguntas.findByQuestionarioId(questionarioId);
        if (items.isEmpty()) throw new BusinessException("Questionário sem perguntas cadastradas.");
        if (input.respostas().size() != items.size()) {
            throw new BusinessException("Todas as perguntas cadastradas precisam ser respondidas.");
        }
        Map<Long, String> values = new HashMap<>();
        for (PesquisaRespostaRequest.Item answer : input.respostas()) {
            if (values.putIfAbsent(answer.perguntaId(), answer.valor()) != null) {
                throw new BusinessException("Pergunta respondida mais de uma vez.");
            }
        }

        PesquisaResposta response = new PesquisaResposta();
        response.setEmpresaId(empresaId);
        response.setQuestionario(questionario);
        response.setCliente(cliente);
        response.setChaveIdempotencia(normalizedKey);
        for (ItQuestionario item : items) {
            String raw = values.get(item.getId());
            if (raw == null) throw new BusinessException("Uma pergunta obrigatória não foi respondida.");
            String normalized = normalize(raw, item.getTpItQuestionario());
            PesquisaRespostaItem answer = new PesquisaRespostaItem();
            answer.setPergunta(item);
            answer.setPerguntaTexto(item.getDsItQuestionario());
            answer.setPerguntaTipo(item.getTpItQuestionario());
            answer.setRespostaTexto(normalized);
            response.addItem(answer);
        }
        return PesquisaRespostaResponse.from(repository.saveAndFlush(response));
    }

    private String normalize(String raw, TipoItemQuestionario type) {
        String value = raw == null ? "" : raw.trim();
        if (value.isBlank() || value.length() > 1000) {
            throw new BusinessException("Resposta vazia ou acima de mil caracteres.");
        }
        return switch (type) {
            case SN -> {
                String answer = value.toUpperCase(Locale.ROOT);
                if (!answer.equals("SIM") && !answer.equals("NAO")) {
                    throw new BusinessException("Resposta sim/não inválida; use SIM ou NAO.");
                }
                yield answer;
            }
            case AV -> {
                try {
                    int valueNum = Integer.parseInt(value);
                    if (valueNum < 1 || valueNum > 10) throw new NumberFormatException();
                    yield Integer.toString(valueNum);
                } catch (NumberFormatException ex) {
                    throw new BusinessException("A avaliação deve estar entre 1 e 10.");
                }
            }
            case AB -> value;
        };
    }
}
