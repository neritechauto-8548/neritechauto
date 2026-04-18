package com.neritech.saas.produtoServico.mapper;

import com.neritech.saas.produtoServico.domain.TabelaPreco;
import com.neritech.saas.produtoServico.domain.enums.TipoTabelaPreco;
import com.neritech.saas.produtoServico.dto.TabelaPrecoRequest;
import com.neritech.saas.produtoServico.dto.TabelaPrecoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:59-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class TabelaPrecoMapperImpl implements TabelaPrecoMapper {

    @Override
    public TabelaPreco toEntity(TabelaPrecoRequest request) {
        if ( request == null ) {
            return null;
        }

        TabelaPreco tabelaPreco = new TabelaPreco();

        tabelaPreco.setNome( request.nome() );
        tabelaPreco.setDescricao( request.descricao() );
        tabelaPreco.setTipoTabela( request.tipoTabela() );
        tabelaPreco.setGrupoClienteId( request.grupoClienteId() );
        tabelaPreco.setMargemLucroPadrao( request.margemLucroPadrao() );
        tabelaPreco.setDescontoMaximoPermitido( request.descontoMaximoPermitido() );
        tabelaPreco.setDataInicio( request.dataInicio() );
        tabelaPreco.setDataFim( request.dataFim() );
        tabelaPreco.setAtivo( request.ativo() );
        tabelaPreco.setPadrao( request.padrao() );
        tabelaPreco.setObservacoes( request.observacoes() );

        return tabelaPreco;
    }

    @Override
    public TabelaPrecoResponse toResponse(TabelaPreco entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        Long id = null;
        String nome = null;
        String descricao = null;
        TipoTabelaPreco tipoTabela = null;
        Long grupoClienteId = null;
        BigDecimal margemLucroPadrao = null;
        BigDecimal descontoMaximoPermitido = null;
        LocalDate dataInicio = null;
        LocalDate dataFim = null;
        Boolean ativo = null;
        Boolean padrao = null;
        String observacoes = null;

        empresaId = entity.getEmpresaId();
        id = entity.getId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        tipoTabela = entity.getTipoTabela();
        grupoClienteId = entity.getGrupoClienteId();
        margemLucroPadrao = entity.getMargemLucroPadrao();
        descontoMaximoPermitido = entity.getDescontoMaximoPermitido();
        dataInicio = entity.getDataInicio();
        dataFim = entity.getDataFim();
        ativo = entity.getAtivo();
        padrao = entity.getPadrao();
        observacoes = entity.getObservacoes();

        TabelaPrecoResponse tabelaPrecoResponse = new TabelaPrecoResponse( id, empresaId, nome, descricao, tipoTabela, grupoClienteId, margemLucroPadrao, descontoMaximoPermitido, dataInicio, dataFim, ativo, padrao, observacoes );

        return tabelaPrecoResponse;
    }

    @Override
    public void updateEntityFromRequest(TabelaPrecoRequest request, TabelaPreco entity) {
        if ( request == null ) {
            return;
        }

        entity.setNome( request.nome() );
        entity.setDescricao( request.descricao() );
        entity.setTipoTabela( request.tipoTabela() );
        entity.setGrupoClienteId( request.grupoClienteId() );
        entity.setMargemLucroPadrao( request.margemLucroPadrao() );
        entity.setDescontoMaximoPermitido( request.descontoMaximoPermitido() );
        entity.setDataInicio( request.dataInicio() );
        entity.setDataFim( request.dataFim() );
        entity.setAtivo( request.ativo() );
        entity.setPadrao( request.padrao() );
        entity.setObservacoes( request.observacoes() );
    }
}
