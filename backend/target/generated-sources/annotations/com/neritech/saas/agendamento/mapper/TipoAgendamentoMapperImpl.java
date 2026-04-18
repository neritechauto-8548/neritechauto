package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.TipoAgendamento;
import com.neritech.saas.agendamento.dto.TipoAgendamentoRequest;
import com.neritech.saas.agendamento.dto.TipoAgendamentoResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:04-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TipoAgendamentoMapperImpl implements TipoAgendamentoMapper {

    @Override
    public TipoAgendamento toEntity(TipoAgendamentoRequest request) {
        if ( request == null ) {
            return null;
        }

        TipoAgendamento tipoAgendamento = new TipoAgendamento();

        tipoAgendamento.setEmpresaId( request.empresaId() );
        tipoAgendamento.setNome( request.nome() );
        tipoAgendamento.setDescricao( request.descricao() );
        tipoAgendamento.setCorIdentificacao( request.corIdentificacao() );
        tipoAgendamento.setIcone( request.icone() );
        tipoAgendamento.setDuracaoPadraoMinutos( request.duracaoPadraoMinutos() );
        tipoAgendamento.setServicosInclusos( request.servicosInclusos() );
        tipoAgendamento.setObservacoesPadrao( request.observacoesPadrao() );
        tipoAgendamento.setPermiteEncaixe( request.permiteEncaixe() );
        tipoAgendamento.setRequerOrcamento( request.requerOrcamento() );
        tipoAgendamento.setAtivo( request.ativo() );
        tipoAgendamento.setOrdemExibicao( request.ordemExibicao() );

        return tipoAgendamento;
    }

    @Override
    public TipoAgendamentoResponse toResponse(TipoAgendamento entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nome = null;
        String descricao = null;
        String corIdentificacao = null;
        String icone = null;
        Integer duracaoPadraoMinutos = null;
        Boolean permiteEncaixe = null;
        Boolean requerOrcamento = null;
        String servicosInclusos = null;
        String observacoesPadrao = null;
        Boolean ativo = null;
        Integer ordemExibicao = null;
        LocalDateTime dataCadastro = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        corIdentificacao = entity.getCorIdentificacao();
        icone = entity.getIcone();
        duracaoPadraoMinutos = entity.getDuracaoPadraoMinutos();
        permiteEncaixe = entity.getPermiteEncaixe();
        requerOrcamento = entity.getRequerOrcamento();
        servicosInclusos = entity.getServicosInclusos();
        observacoesPadrao = entity.getObservacoesPadrao();
        ativo = entity.getAtivo();
        ordemExibicao = entity.getOrdemExibicao();
        dataCadastro = entity.getDataCadastro();

        TipoAgendamentoResponse tipoAgendamentoResponse = new TipoAgendamentoResponse( id, empresaId, nome, descricao, corIdentificacao, icone, duracaoPadraoMinutos, permiteEncaixe, requerOrcamento, servicosInclusos, observacoesPadrao, ativo, ordemExibicao, dataCadastro );

        return tipoAgendamentoResponse;
    }
}
