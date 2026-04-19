package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.EscalaTrabalho;
import com.neritech.saas.rh.domain.enums.TipoEscala;
import com.neritech.saas.rh.dto.EscalaTrabalhoRequest;
import com.neritech.saas.rh.dto.EscalaTrabalhoResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T16:16:34-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class EscalaTrabalhoMapperImpl implements EscalaTrabalhoMapper {

    @Override
    public EscalaTrabalho toEntity(EscalaTrabalhoRequest request) {
        if ( request == null ) {
            return null;
        }

        EscalaTrabalho escalaTrabalho = new EscalaTrabalho();

        escalaTrabalho.setEmpresaId( request.empresaId() );
        escalaTrabalho.setFuncionarioId( request.funcionarioId() );
        escalaTrabalho.setHorarioTrabalhoId( request.horarioTrabalhoId() );
        escalaTrabalho.setTipoEscala( request.tipoEscala() );
        escalaTrabalho.setDataInicio( request.dataInicio() );
        escalaTrabalho.setDataFim( request.dataFim() );
        escalaTrabalho.setDiasTrabalho( request.diasTrabalho() );
        escalaTrabalho.setDiasFolga( request.diasFolga() );
        escalaTrabalho.setFuncionariosIncluidos( request.funcionariosIncluidos() );
        escalaTrabalho.setObservacoes( request.observacoes() );
        escalaTrabalho.setAtivo( request.ativo() );

        return escalaTrabalho;
    }

    @Override
    public EscalaTrabalhoResponse toResponse(EscalaTrabalho entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        Long funcionarioId = null;
        Long horarioTrabalhoId = null;
        TipoEscala tipoEscala = null;
        LocalDate dataInicio = null;
        LocalDate dataFim = null;
        Integer diasTrabalho = null;
        Integer diasFolga = null;
        String funcionariosIncluidos = null;
        String observacoes = null;
        Boolean ativo = null;
        Long criadoPor = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        funcionarioId = entity.getFuncionarioId();
        horarioTrabalhoId = entity.getHorarioTrabalhoId();
        tipoEscala = entity.getTipoEscala();
        dataInicio = entity.getDataInicio();
        dataFim = entity.getDataFim();
        diasTrabalho = entity.getDiasTrabalho();
        diasFolga = entity.getDiasFolga();
        funcionariosIncluidos = entity.getFuncionariosIncluidos();
        observacoes = entity.getObservacoes();
        ativo = entity.getAtivo();
        criadoPor = entity.getCriadoPor();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        EscalaTrabalhoResponse escalaTrabalhoResponse = new EscalaTrabalhoResponse( id, empresaId, funcionarioId, horarioTrabalhoId, tipoEscala, dataInicio, dataFim, diasTrabalho, diasFolga, funcionariosIncluidos, observacoes, ativo, criadoPor, dataCadastro, dataAtualizacao );

        return escalaTrabalhoResponse;
    }

    @Override
    public void updateEntity(EscalaTrabalhoRequest request, EscalaTrabalho entity) {
        if ( request == null ) {
            return;
        }

        if ( request.empresaId() != null ) {
            entity.setEmpresaId( request.empresaId() );
        }
        if ( request.funcionarioId() != null ) {
            entity.setFuncionarioId( request.funcionarioId() );
        }
        if ( request.horarioTrabalhoId() != null ) {
            entity.setHorarioTrabalhoId( request.horarioTrabalhoId() );
        }
        if ( request.tipoEscala() != null ) {
            entity.setTipoEscala( request.tipoEscala() );
        }
        if ( request.dataInicio() != null ) {
            entity.setDataInicio( request.dataInicio() );
        }
        if ( request.dataFim() != null ) {
            entity.setDataFim( request.dataFim() );
        }
        if ( request.diasTrabalho() != null ) {
            entity.setDiasTrabalho( request.diasTrabalho() );
        }
        if ( request.diasFolga() != null ) {
            entity.setDiasFolga( request.diasFolga() );
        }
        if ( request.funcionariosIncluidos() != null ) {
            entity.setFuncionariosIncluidos( request.funcionariosIncluidos() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
    }
}
