package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.PesquisaResposta;
import java.time.LocalDateTime;
import java.util.List;

public record PesquisaRespostaResponse(
    Long id, Long questionarioId, Long clienteId, LocalDateTime dataCadastro, List<Item> respostas
) {
    public record Item(Long perguntaId, String perguntaTexto, String tipo, String valor) {}

    public static PesquisaRespostaResponse from(PesquisaResposta item) {
        return new PesquisaRespostaResponse(item.getId(), item.getQuestionario().getId(),
            item.getCliente().getId(), item.getDataCadastro(),
            item.getItens().stream().map(i -> new Item(i.getPergunta().getId(),
                i.getPerguntaTexto(), i.getPerguntaTipo().name(), i.getRespostaTexto())).toList());
    }
}
