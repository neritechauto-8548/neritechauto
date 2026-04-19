package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.HorarioTrabalho;
import com.neritech.saas.rh.domain.enums.TipoHorario;
import com.neritech.saas.rh.dto.HorarioTrabalhoRequest;
import com.neritech.saas.rh.dto.HorarioTrabalhoResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T16:16:41-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class HorarioTrabalhoMapperImpl implements HorarioTrabalhoMapper {

    @Override
    public HorarioTrabalho toEntity(HorarioTrabalhoRequest request) {
        if ( request == null ) {
            return null;
        }

        HorarioTrabalho horarioTrabalho = new HorarioTrabalho();

        horarioTrabalho.setEmpresaId( request.empresaId() );
        horarioTrabalho.setNome( request.nome() );
        horarioTrabalho.setDescricao( request.descricao() );
        horarioTrabalho.setTipoHorario( request.tipoHorario() );
        horarioTrabalho.setHoraEntradaSeg( request.horaEntradaSeg() );
        horarioTrabalho.setHoraSaidaSeg( request.horaSaidaSeg() );
        horarioTrabalho.setHoraEntradaTer( request.horaEntradaTer() );
        horarioTrabalho.setHoraSaidaTer( request.horaSaidaTer() );
        horarioTrabalho.setHoraEntradaQua( request.horaEntradaQua() );
        horarioTrabalho.setHoraSaidaQua( request.horaSaidaQua() );
        horarioTrabalho.setHoraEntradaQui( request.horaEntradaQui() );
        horarioTrabalho.setHoraSaidaQui( request.horaSaidaQui() );
        horarioTrabalho.setHoraEntradaSex( request.horaEntradaSex() );
        horarioTrabalho.setHoraSaidaSex( request.horaSaidaSex() );
        horarioTrabalho.setHoraEntradaSab( request.horaEntradaSab() );
        horarioTrabalho.setHoraSaidaSab( request.horaSaidaSab() );
        horarioTrabalho.setHoraEntradaDom( request.horaEntradaDom() );
        horarioTrabalho.setHoraSaidaDom( request.horaSaidaDom() );
        horarioTrabalho.setIntervaloAlmocoMinutos( request.intervaloAlmocoMinutos() );
        horarioTrabalho.setCargaHorariaSemanal( request.cargaHorariaSemanal() );
        horarioTrabalho.setToleranciaAtrasoMinutos( request.toleranciaAtrasoMinutos() );
        horarioTrabalho.setAtivo( request.ativo() );

        return horarioTrabalho;
    }

    @Override
    public HorarioTrabalhoResponse toResponse(HorarioTrabalho entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nome = null;
        String descricao = null;
        TipoHorario tipoHorario = null;
        LocalTime horaEntradaSeg = null;
        LocalTime horaSaidaSeg = null;
        LocalTime horaEntradaTer = null;
        LocalTime horaSaidaTer = null;
        LocalTime horaEntradaQua = null;
        LocalTime horaSaidaQua = null;
        LocalTime horaEntradaQui = null;
        LocalTime horaSaidaQui = null;
        LocalTime horaEntradaSex = null;
        LocalTime horaSaidaSex = null;
        LocalTime horaEntradaSab = null;
        LocalTime horaSaidaSab = null;
        LocalTime horaEntradaDom = null;
        LocalTime horaSaidaDom = null;
        Integer intervaloAlmocoMinutos = null;
        Integer cargaHorariaSemanal = null;
        Integer toleranciaAtrasoMinutos = null;
        Boolean ativo = null;
        Long criadoPor = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        tipoHorario = entity.getTipoHorario();
        horaEntradaSeg = entity.getHoraEntradaSeg();
        horaSaidaSeg = entity.getHoraSaidaSeg();
        horaEntradaTer = entity.getHoraEntradaTer();
        horaSaidaTer = entity.getHoraSaidaTer();
        horaEntradaQua = entity.getHoraEntradaQua();
        horaSaidaQua = entity.getHoraSaidaQua();
        horaEntradaQui = entity.getHoraEntradaQui();
        horaSaidaQui = entity.getHoraSaidaQui();
        horaEntradaSex = entity.getHoraEntradaSex();
        horaSaidaSex = entity.getHoraSaidaSex();
        horaEntradaSab = entity.getHoraEntradaSab();
        horaSaidaSab = entity.getHoraSaidaSab();
        horaEntradaDom = entity.getHoraEntradaDom();
        horaSaidaDom = entity.getHoraSaidaDom();
        intervaloAlmocoMinutos = entity.getIntervaloAlmocoMinutos();
        cargaHorariaSemanal = entity.getCargaHorariaSemanal();
        toleranciaAtrasoMinutos = entity.getToleranciaAtrasoMinutos();
        ativo = entity.getAtivo();
        criadoPor = entity.getCriadoPor();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        HorarioTrabalhoResponse horarioTrabalhoResponse = new HorarioTrabalhoResponse( id, empresaId, nome, descricao, tipoHorario, horaEntradaSeg, horaSaidaSeg, horaEntradaTer, horaSaidaTer, horaEntradaQua, horaSaidaQua, horaEntradaQui, horaSaidaQui, horaEntradaSex, horaSaidaSex, horaEntradaSab, horaSaidaSab, horaEntradaDom, horaSaidaDom, intervaloAlmocoMinutos, cargaHorariaSemanal, toleranciaAtrasoMinutos, ativo, criadoPor, dataCadastro, dataAtualizacao );

        return horarioTrabalhoResponse;
    }

    @Override
    public void updateEntity(HorarioTrabalhoRequest request, HorarioTrabalho entity) {
        if ( request == null ) {
            return;
        }

        if ( request.empresaId() != null ) {
            entity.setEmpresaId( request.empresaId() );
        }
        if ( request.nome() != null ) {
            entity.setNome( request.nome() );
        }
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.tipoHorario() != null ) {
            entity.setTipoHorario( request.tipoHorario() );
        }
        if ( request.horaEntradaSeg() != null ) {
            entity.setHoraEntradaSeg( request.horaEntradaSeg() );
        }
        if ( request.horaSaidaSeg() != null ) {
            entity.setHoraSaidaSeg( request.horaSaidaSeg() );
        }
        if ( request.horaEntradaTer() != null ) {
            entity.setHoraEntradaTer( request.horaEntradaTer() );
        }
        if ( request.horaSaidaTer() != null ) {
            entity.setHoraSaidaTer( request.horaSaidaTer() );
        }
        if ( request.horaEntradaQua() != null ) {
            entity.setHoraEntradaQua( request.horaEntradaQua() );
        }
        if ( request.horaSaidaQua() != null ) {
            entity.setHoraSaidaQua( request.horaSaidaQua() );
        }
        if ( request.horaEntradaQui() != null ) {
            entity.setHoraEntradaQui( request.horaEntradaQui() );
        }
        if ( request.horaSaidaQui() != null ) {
            entity.setHoraSaidaQui( request.horaSaidaQui() );
        }
        if ( request.horaEntradaSex() != null ) {
            entity.setHoraEntradaSex( request.horaEntradaSex() );
        }
        if ( request.horaSaidaSex() != null ) {
            entity.setHoraSaidaSex( request.horaSaidaSex() );
        }
        if ( request.horaEntradaSab() != null ) {
            entity.setHoraEntradaSab( request.horaEntradaSab() );
        }
        if ( request.horaSaidaSab() != null ) {
            entity.setHoraSaidaSab( request.horaSaidaSab() );
        }
        if ( request.horaEntradaDom() != null ) {
            entity.setHoraEntradaDom( request.horaEntradaDom() );
        }
        if ( request.horaSaidaDom() != null ) {
            entity.setHoraSaidaDom( request.horaSaidaDom() );
        }
        if ( request.intervaloAlmocoMinutos() != null ) {
            entity.setIntervaloAlmocoMinutos( request.intervaloAlmocoMinutos() );
        }
        if ( request.cargaHorariaSemanal() != null ) {
            entity.setCargaHorariaSemanal( request.cargaHorariaSemanal() );
        }
        if ( request.toleranciaAtrasoMinutos() != null ) {
            entity.setToleranciaAtrasoMinutos( request.toleranciaAtrasoMinutos() );
        }
        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
    }
}
