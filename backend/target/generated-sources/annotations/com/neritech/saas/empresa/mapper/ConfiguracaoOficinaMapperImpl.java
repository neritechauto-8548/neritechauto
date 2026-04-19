package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.ConfiguracaoOficina;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.dto.ConfiguracaoOficinaRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoOficinaResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:27-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ConfiguracaoOficinaMapperImpl implements ConfiguracaoOficinaMapper {

    @Override
    public ConfiguracaoOficina toEntity(ConfiguracaoOficinaRequest request) {
        if ( request == null ) {
            return null;
        }

        ConfiguracaoOficina configuracaoOficina = new ConfiguracaoOficina();

        configuracaoOficina.setHorarioAberturaSegSex( request.horarioAberturaSegSex() );
        configuracaoOficina.setHorarioFechamentoSegSex( request.horarioFechamentoSegSex() );
        configuracaoOficina.setHorarioAberturaSabado( request.horarioAberturaSabado() );
        configuracaoOficina.setHorarioFechamentoSabado( request.horarioFechamentoSabado() );
        configuracaoOficina.setFuncionaDomingo( request.funcionaDomingo() );
        configuracaoOficina.setHorarioAberturaDomingo( request.horarioAberturaDomingo() );
        configuracaoOficina.setHorarioFechamentoDomingo( request.horarioFechamentoDomingo() );
        configuracaoOficina.setTempoAgendamentoPadrao( request.tempoAgendamentoPadrao() );
        configuracaoOficina.setAntecedenciaMinimaAgendamento( request.antecedenciaMinimaAgendamento() );
        configuracaoOficina.setPermiteAgendamentoOnline( request.permiteAgendamentoOnline() );
        configuracaoOficina.setEnviaLembreteAgendamento( request.enviaLembreteAgendamento() );
        configuracaoOficina.setTempoLembreteHoras( request.tempoLembreteHoras() );
        configuracaoOficina.setMargemLucroPadrao( request.margemLucroPadrao() );
        configuracaoOficina.setDiasGarantiaPadrao( request.diasGarantiaPadrao() );
        configuracaoOficina.setMoeda( request.moeda() );
        configuracaoOficina.setFormatoData( request.formatoData() );
        configuracaoOficina.setFormatoHora( request.formatoHora() );
        configuracaoOficina.setTimezone( request.timezone() );

        return configuracaoOficina;
    }

    @Override
    public ConfiguracaoOficinaResponse toResponse(ConfiguracaoOficina entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        String empresaNome = null;
        Long id = null;
        LocalTime horarioAberturaSegSex = null;
        LocalTime horarioFechamentoSegSex = null;
        LocalTime horarioAberturaSabado = null;
        LocalTime horarioFechamentoSabado = null;
        Boolean funcionaDomingo = null;
        LocalTime horarioAberturaDomingo = null;
        LocalTime horarioFechamentoDomingo = null;
        Integer tempoAgendamentoPadrao = null;
        Integer antecedenciaMinimaAgendamento = null;
        Boolean permiteAgendamentoOnline = null;
        Boolean enviaLembreteAgendamento = null;
        Integer tempoLembreteHoras = null;
        BigDecimal margemLucroPadrao = null;
        Integer diasGarantiaPadrao = null;
        String moeda = null;
        String formatoData = null;
        String formatoHora = null;
        String timezone = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        empresaId = entityEmpresaId( entity );
        empresaNome = entityEmpresaNomeFantasia( entity );
        id = entity.getId();
        horarioAberturaSegSex = entity.getHorarioAberturaSegSex();
        horarioFechamentoSegSex = entity.getHorarioFechamentoSegSex();
        horarioAberturaSabado = entity.getHorarioAberturaSabado();
        horarioFechamentoSabado = entity.getHorarioFechamentoSabado();
        funcionaDomingo = entity.getFuncionaDomingo();
        horarioAberturaDomingo = entity.getHorarioAberturaDomingo();
        horarioFechamentoDomingo = entity.getHorarioFechamentoDomingo();
        tempoAgendamentoPadrao = entity.getTempoAgendamentoPadrao();
        antecedenciaMinimaAgendamento = entity.getAntecedenciaMinimaAgendamento();
        permiteAgendamentoOnline = entity.getPermiteAgendamentoOnline();
        enviaLembreteAgendamento = entity.getEnviaLembreteAgendamento();
        tempoLembreteHoras = entity.getTempoLembreteHoras();
        margemLucroPadrao = entity.getMargemLucroPadrao();
        diasGarantiaPadrao = entity.getDiasGarantiaPadrao();
        moeda = entity.getMoeda();
        formatoData = entity.getFormatoData();
        formatoHora = entity.getFormatoHora();
        timezone = entity.getTimezone();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        ConfiguracaoOficinaResponse configuracaoOficinaResponse = new ConfiguracaoOficinaResponse( id, empresaId, empresaNome, horarioAberturaSegSex, horarioFechamentoSegSex, horarioAberturaSabado, horarioFechamentoSabado, funcionaDomingo, horarioAberturaDomingo, horarioFechamentoDomingo, tempoAgendamentoPadrao, antecedenciaMinimaAgendamento, permiteAgendamentoOnline, enviaLembreteAgendamento, tempoLembreteHoras, margemLucroPadrao, diasGarantiaPadrao, moeda, formatoData, formatoHora, timezone, dataCadastro, dataAtualizacao );

        return configuracaoOficinaResponse;
    }

    @Override
    public void updateEntityFromRequest(ConfiguracaoOficinaRequest request, ConfiguracaoOficina entity) {
        if ( request == null ) {
            return;
        }

        entity.setHorarioAberturaSegSex( request.horarioAberturaSegSex() );
        entity.setHorarioFechamentoSegSex( request.horarioFechamentoSegSex() );
        entity.setHorarioAberturaSabado( request.horarioAberturaSabado() );
        entity.setHorarioFechamentoSabado( request.horarioFechamentoSabado() );
        entity.setFuncionaDomingo( request.funcionaDomingo() );
        entity.setHorarioAberturaDomingo( request.horarioAberturaDomingo() );
        entity.setHorarioFechamentoDomingo( request.horarioFechamentoDomingo() );
        entity.setTempoAgendamentoPadrao( request.tempoAgendamentoPadrao() );
        entity.setAntecedenciaMinimaAgendamento( request.antecedenciaMinimaAgendamento() );
        entity.setPermiteAgendamentoOnline( request.permiteAgendamentoOnline() );
        entity.setEnviaLembreteAgendamento( request.enviaLembreteAgendamento() );
        entity.setTempoLembreteHoras( request.tempoLembreteHoras() );
        entity.setMargemLucroPadrao( request.margemLucroPadrao() );
        entity.setDiasGarantiaPadrao( request.diasGarantiaPadrao() );
        entity.setMoeda( request.moeda() );
        entity.setFormatoData( request.formatoData() );
        entity.setFormatoHora( request.formatoHora() );
        entity.setTimezone( request.timezone() );
    }

    private Long entityEmpresaId(ConfiguracaoOficina configuracaoOficina) {
        if ( configuracaoOficina == null ) {
            return null;
        }
        Empresa empresa = configuracaoOficina.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        Long id = empresa.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityEmpresaNomeFantasia(ConfiguracaoOficina configuracaoOficina) {
        if ( configuracaoOficina == null ) {
            return null;
        }
        Empresa empresa = configuracaoOficina.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        String nomeFantasia = empresa.getNomeFantasia();
        if ( nomeFantasia == null ) {
            return null;
        }
        return nomeFantasia;
    }
}
