package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.FechamentoCaixa;
import com.neritech.saas.financeiro.dto.FechamentoCaixaRequest;
import com.neritech.saas.financeiro.dto.FechamentoCaixaResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:22-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FechamentoCaixaMapperImpl implements FechamentoCaixaMapper {

    @Override
    public FechamentoCaixa toEntity(FechamentoCaixaRequest request) {
        if ( request == null ) {
            return null;
        }

        FechamentoCaixa fechamentoCaixa = new FechamentoCaixa();

        fechamentoCaixa.setDataAbertura( request.getDataAbertura() );
        fechamentoCaixa.setDataFechamento( request.getDataFechamento() );
        fechamentoCaixa.setObservacoes( request.getObservacoes() );
        fechamentoCaixa.setSaldoFinal( request.getSaldoFinal() );
        fechamentoCaixa.setSaldoInicial( request.getSaldoInicial() );
        fechamentoCaixa.setSituacao( request.getSituacao() );
        fechamentoCaixa.setTotalEntradas( request.getTotalEntradas() );
        fechamentoCaixa.setTotalSaidas( request.getTotalSaidas() );
        fechamentoCaixa.setUsuarioResponsavel( request.getUsuarioResponsavel() );

        return fechamentoCaixa;
    }

    @Override
    public FechamentoCaixaResponse toResponse(FechamentoCaixa entity) {
        if ( entity == null ) {
            return null;
        }

        FechamentoCaixaResponse fechamentoCaixaResponse = new FechamentoCaixaResponse();

        fechamentoCaixaResponse.setDataAbertura( entity.getDataAbertura() );
        fechamentoCaixaResponse.setDataFechamento( entity.getDataFechamento() );
        fechamentoCaixaResponse.setId( entity.getId() );
        fechamentoCaixaResponse.setObservacoes( entity.getObservacoes() );
        fechamentoCaixaResponse.setSaldoFinal( entity.getSaldoFinal() );
        fechamentoCaixaResponse.setSaldoInicial( entity.getSaldoInicial() );
        fechamentoCaixaResponse.setSituacao( entity.getSituacao() );
        fechamentoCaixaResponse.setTotalEntradas( entity.getTotalEntradas() );
        fechamentoCaixaResponse.setTotalSaidas( entity.getTotalSaidas() );
        fechamentoCaixaResponse.setUsuarioResponsavel( entity.getUsuarioResponsavel() );

        return fechamentoCaixaResponse;
    }

    @Override
    public void updateEntityFromDTO(FechamentoCaixaRequest request, FechamentoCaixa entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getDataAbertura() != null ) {
            entity.setDataAbertura( request.getDataAbertura() );
        }
        if ( request.getDataFechamento() != null ) {
            entity.setDataFechamento( request.getDataFechamento() );
        }
        if ( request.getObservacoes() != null ) {
            entity.setObservacoes( request.getObservacoes() );
        }
        if ( request.getSaldoFinal() != null ) {
            entity.setSaldoFinal( request.getSaldoFinal() );
        }
        if ( request.getSaldoInicial() != null ) {
            entity.setSaldoInicial( request.getSaldoInicial() );
        }
        if ( request.getSituacao() != null ) {
            entity.setSituacao( request.getSituacao() );
        }
        if ( request.getTotalEntradas() != null ) {
            entity.setTotalEntradas( request.getTotalEntradas() );
        }
        if ( request.getTotalSaidas() != null ) {
            entity.setTotalSaidas( request.getTotalSaidas() );
        }
        if ( request.getUsuarioResponsavel() != null ) {
            entity.setUsuarioResponsavel( request.getUsuarioResponsavel() );
        }
    }
}
