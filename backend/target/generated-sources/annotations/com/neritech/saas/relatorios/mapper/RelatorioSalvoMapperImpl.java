package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.RelatorioSalvo;
import com.neritech.saas.relatorios.dto.RelatorioSalvoRequest;
import com.neritech.saas.relatorios.dto.RelatorioSalvoResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:24-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class RelatorioSalvoMapperImpl implements RelatorioSalvoMapper {

    @Override
    public RelatorioSalvo toEntity(RelatorioSalvoRequest request) {
        if ( request == null ) {
            return null;
        }

        RelatorioSalvo.RelatorioSalvoBuilder<?, ?> relatorioSalvo = RelatorioSalvo.builder();

        relatorioSalvo.empresaId( request.getEmpresaId() );
        relatorioSalvo.agendamentoAtivo( request.getAgendamentoAtivo() );
        relatorioSalvo.agrupamentos( request.getAgrupamentos() );
        relatorioSalvo.ativo( request.getAtivo() );
        relatorioSalvo.camposExibidos( request.getCamposExibidos() );
        relatorioSalvo.categoria( request.getCategoria() );
        relatorioSalvo.compartilhadoCom( request.getCompartilhadoCom() );
        relatorioSalvo.consultaSql( request.getConsultaSql() );
        relatorioSalvo.descricao( request.getDescricao() );
        relatorioSalvo.emailsEnvio( request.getEmailsEnvio() );
        relatorioSalvo.filtrosPadrao( request.getFiltrosPadrao() );
        relatorioSalvo.formatoSaida( request.getFormatoSaida() );
        relatorioSalvo.frequenciaAgendamento( request.getFrequenciaAgendamento() );
        relatorioSalvo.graficosInclusos( request.getGraficosInclusos() );
        relatorioSalvo.nomeRelatorio( request.getNomeRelatorio() );
        relatorioSalvo.ordenacaoPadrao( request.getOrdenacaoPadrao() );
        relatorioSalvo.orientacaoPagina( request.getOrientacaoPagina() );
        relatorioSalvo.parametrosRelatorio( request.getParametrosRelatorio() );
        relatorioSalvo.pastaDestino( request.getPastaDestino() );
        relatorioSalvo.proximoEnvio( request.getProximoEnvio() );
        relatorioSalvo.publico( request.getPublico() );
        relatorioSalvo.templateLayout( request.getTemplateLayout() );
        relatorioSalvo.tipoRelatorio( request.getTipoRelatorio() );
        relatorioSalvo.totalizadores( request.getTotalizadores() );

        return relatorioSalvo.build();
    }

    @Override
    public RelatorioSalvoResponse toResponse(RelatorioSalvo entity) {
        if ( entity == null ) {
            return null;
        }

        RelatorioSalvoResponse relatorioSalvoResponse = new RelatorioSalvoResponse();

        relatorioSalvoResponse.setId( entity.getId() );
        relatorioSalvoResponse.setEmpresaId( entity.getEmpresaId() );
        relatorioSalvoResponse.setNomeRelatorio( entity.getNomeRelatorio() );
        relatorioSalvoResponse.setDescricao( entity.getDescricao() );
        relatorioSalvoResponse.setTipoRelatorio( entity.getTipoRelatorio() );
        relatorioSalvoResponse.setCategoria( entity.getCategoria() );
        relatorioSalvoResponse.setConsultaSql( entity.getConsultaSql() );
        relatorioSalvoResponse.setParametrosRelatorio( entity.getParametrosRelatorio() );
        relatorioSalvoResponse.setCamposExibidos( entity.getCamposExibidos() );
        relatorioSalvoResponse.setOrdenacaoPadrao( entity.getOrdenacaoPadrao() );
        relatorioSalvoResponse.setFiltrosPadrao( entity.getFiltrosPadrao() );
        relatorioSalvoResponse.setFormatoSaida( entity.getFormatoSaida() );
        relatorioSalvoResponse.setOrientacaoPagina( entity.getOrientacaoPagina() );
        relatorioSalvoResponse.setTemplateLayout( entity.getTemplateLayout() );
        relatorioSalvoResponse.setAgrupamentos( entity.getAgrupamentos() );
        relatorioSalvoResponse.setTotalizadores( entity.getTotalizadores() );
        relatorioSalvoResponse.setGraficosInclusos( entity.getGraficosInclusos() );
        relatorioSalvoResponse.setPublico( entity.getPublico() );
        relatorioSalvoResponse.setCompartilhadoCom( entity.getCompartilhadoCom() );
        relatorioSalvoResponse.setAgendamentoAtivo( entity.getAgendamentoAtivo() );
        relatorioSalvoResponse.setFrequenciaAgendamento( entity.getFrequenciaAgendamento() );
        relatorioSalvoResponse.setProximoEnvio( entity.getProximoEnvio() );
        relatorioSalvoResponse.setEmailsEnvio( entity.getEmailsEnvio() );
        relatorioSalvoResponse.setPastaDestino( entity.getPastaDestino() );
        relatorioSalvoResponse.setAtivo( entity.getAtivo() );
        relatorioSalvoResponse.setTotalExecucoes( entity.getTotalExecucoes() );
        relatorioSalvoResponse.setDataUltimaExecucao( entity.getDataUltimaExecucao() );
        relatorioSalvoResponse.setTempoMedioExecucao( entity.getTempoMedioExecucao() );
        relatorioSalvoResponse.setTamanhoMedioArquivo( entity.getTamanhoMedioArquivo() );
        relatorioSalvoResponse.setCriadoPor( entity.getCriadoPor() );
        relatorioSalvoResponse.setDataCadastro( entity.getDataCadastro() );
        relatorioSalvoResponse.setDataAtualizacao( entity.getDataAtualizacao() );

        return relatorioSalvoResponse;
    }

    @Override
    public void updateEntity(RelatorioSalvo entity, RelatorioSalvoRequest request) {
        if ( request == null ) {
            return;
        }

        entity.setAgendamentoAtivo( request.getAgendamentoAtivo() );
        entity.setAgrupamentos( request.getAgrupamentos() );
        entity.setAtivo( request.getAtivo() );
        entity.setCamposExibidos( request.getCamposExibidos() );
        entity.setCategoria( request.getCategoria() );
        entity.setCompartilhadoCom( request.getCompartilhadoCom() );
        entity.setConsultaSql( request.getConsultaSql() );
        entity.setDescricao( request.getDescricao() );
        entity.setFiltrosPadrao( request.getFiltrosPadrao() );
        entity.setFormatoSaida( request.getFormatoSaida() );
        entity.setGraficosInclusos( request.getGraficosInclusos() );
        entity.setNomeRelatorio( request.getNomeRelatorio() );
        entity.setOrdenacaoPadrao( request.getOrdenacaoPadrao() );
        entity.setOrientacaoPagina( request.getOrientacaoPagina() );
        entity.setParametrosRelatorio( request.getParametrosRelatorio() );
        entity.setPublico( request.getPublico() );
        entity.setTemplateLayout( request.getTemplateLayout() );
        entity.setTipoRelatorio( request.getTipoRelatorio() );
        entity.setTotalizadores( request.getTotalizadores() );
        entity.setFrequenciaAgendamento( request.getFrequenciaAgendamento() );
        entity.setProximoEnvio( request.getProximoEnvio() );
        entity.setEmailsEnvio( request.getEmailsEnvio() );
        entity.setPastaDestino( request.getPastaDestino() );
    }
}
