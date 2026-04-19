package com.neritech.saas.garantia.mapper;

import com.neritech.saas.garantia.domain.CanalAberturaReclamacao;
import com.neritech.saas.garantia.domain.PrioridadeReclamacao;
import com.neritech.saas.garantia.domain.ReclamacaoGarantia;
import com.neritech.saas.garantia.domain.StatusReclamacao;
import com.neritech.saas.garantia.domain.TipoReclamacao;
import com.neritech.saas.garantia.dto.ReclamacaoGarantiaRequest;
import com.neritech.saas.garantia.dto.ReclamacaoGarantiaResponse;
import com.neritech.saas.rh.domain.Funcionario;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:46-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ReclamacaoGarantiaMapperImpl implements ReclamacaoGarantiaMapper {

    @Override
    public ReclamacaoGarantia toEntity(ReclamacaoGarantiaRequest request) {
        if ( request == null ) {
            return null;
        }

        ReclamacaoGarantia reclamacaoGarantia = new ReclamacaoGarantia();

        reclamacaoGarantia.setNumeroReclamacao( request.numeroReclamacao() );
        reclamacaoGarantia.setTipoReclamacao( request.tipoReclamacao() );
        reclamacaoGarantia.setProblemaRelatado( request.problemaRelatado() );
        reclamacaoGarantia.setSintomasObservados( request.sintomasObservados() );
        reclamacaoGarantia.setCondicoesUso( request.condicoesUso() );
        reclamacaoGarantia.setKilometragemAtual( request.kilometragemAtual() );
        reclamacaoGarantia.setTempoUsoDesdeServico( request.tempoUsoDesdeServico() );
        reclamacaoGarantia.setEvidenciasFornecidas( request.evidenciasFornecidas() );
        reclamacaoGarantia.setFotosProblema( request.fotosProblema() );
        reclamacaoGarantia.setVideosProblema( request.videosProblema() );
        reclamacaoGarantia.setDocumentosAnexos( request.documentosAnexos() );
        reclamacaoGarantia.setPrioridade( request.prioridade() );
        reclamacaoGarantia.setCanalAbertura( request.canalAbertura() );
        reclamacaoGarantia.setClienteSolicitante( request.clienteSolicitante() );
        reclamacaoGarantia.setContatoSolicitante( request.contatoSolicitante() );
        reclamacaoGarantia.setEnderecoAtendimento( request.enderecoAtendimento() );
        reclamacaoGarantia.setDataAgendamentoAnalise( request.dataAgendamentoAnalise() );
        reclamacaoGarantia.setStatus( request.status() );
        reclamacaoGarantia.setObservacoesInternas( request.observacoesInternas() );

        return reclamacaoGarantia;
    }

    @Override
    public ReclamacaoGarantiaResponse toResponse(ReclamacaoGarantia entity) {
        if ( entity == null ) {
            return null;
        }

        String tecnicoResponsavelNome = null;
        Long id = null;
        String numeroReclamacao = null;
        LocalDateTime dataAbertura = null;
        TipoReclamacao tipoReclamacao = null;
        String problemaRelatado = null;
        String sintomasObservados = null;
        String condicoesUso = null;
        Integer kilometragemAtual = null;
        Integer tempoUsoDesdeServico = null;
        String evidenciasFornecidas = null;
        String fotosProblema = null;
        String videosProblema = null;
        String documentosAnexos = null;
        PrioridadeReclamacao prioridade = null;
        CanalAberturaReclamacao canalAbertura = null;
        String clienteSolicitante = null;
        String contatoSolicitante = null;
        String enderecoAtendimento = null;
        LocalDateTime dataAgendamentoAnalise = null;
        LocalDateTime dataInicioAnalise = null;
        LocalDateTime dataFimAnalise = null;
        Integer tempoAnaliseHoras = null;
        String diagnosticoTecnico = null;
        String causaIdentificada = null;
        String procedimentoRealizado = null;
        Boolean aprovada = null;
        String motivoReprovacao = null;
        BigDecimal valorAprovado = null;
        String itensSubstituidos = null;
        String servicosRefeitos = null;
        BigDecimal custosAdicionais = null;
        String justificativaCustos = null;
        Integer satisfacaoCliente = null;
        String comentarioSatisfacao = null;
        StatusReclamacao status = null;
        LocalDateTime dataResolucao = null;
        String observacoesInternas = null;
        Long abertaPor = null;

        tecnicoResponsavelNome = entityTecnicoResponsavelNomeCompleto( entity );
        id = entity.getId();
        numeroReclamacao = entity.getNumeroReclamacao();
        dataAbertura = entity.getDataAbertura();
        tipoReclamacao = entity.getTipoReclamacao();
        problemaRelatado = entity.getProblemaRelatado();
        sintomasObservados = entity.getSintomasObservados();
        condicoesUso = entity.getCondicoesUso();
        kilometragemAtual = entity.getKilometragemAtual();
        tempoUsoDesdeServico = entity.getTempoUsoDesdeServico();
        evidenciasFornecidas = entity.getEvidenciasFornecidas();
        fotosProblema = entity.getFotosProblema();
        videosProblema = entity.getVideosProblema();
        documentosAnexos = entity.getDocumentosAnexos();
        prioridade = entity.getPrioridade();
        canalAbertura = entity.getCanalAbertura();
        clienteSolicitante = entity.getClienteSolicitante();
        contatoSolicitante = entity.getContatoSolicitante();
        enderecoAtendimento = entity.getEnderecoAtendimento();
        dataAgendamentoAnalise = entity.getDataAgendamentoAnalise();
        dataInicioAnalise = entity.getDataInicioAnalise();
        dataFimAnalise = entity.getDataFimAnalise();
        tempoAnaliseHoras = entity.getTempoAnaliseHoras();
        diagnosticoTecnico = entity.getDiagnosticoTecnico();
        causaIdentificada = entity.getCausaIdentificada();
        procedimentoRealizado = entity.getProcedimentoRealizado();
        aprovada = entity.getAprovada();
        motivoReprovacao = entity.getMotivoReprovacao();
        valorAprovado = entity.getValorAprovado();
        itensSubstituidos = entity.getItensSubstituidos();
        servicosRefeitos = entity.getServicosRefeitos();
        custosAdicionais = entity.getCustosAdicionais();
        justificativaCustos = entity.getJustificativaCustos();
        satisfacaoCliente = entity.getSatisfacaoCliente();
        comentarioSatisfacao = entity.getComentarioSatisfacao();
        status = entity.getStatus();
        dataResolucao = entity.getDataResolucao();
        observacoesInternas = entity.getObservacoesInternas();
        abertaPor = entity.getAbertaPor();

        Long garantiaId = null;
        Long tecnicoResponsavelId = null;

        ReclamacaoGarantiaResponse reclamacaoGarantiaResponse = new ReclamacaoGarantiaResponse( id, garantiaId, numeroReclamacao, dataAbertura, tipoReclamacao, problemaRelatado, sintomasObservados, condicoesUso, kilometragemAtual, tempoUsoDesdeServico, evidenciasFornecidas, fotosProblema, videosProblema, documentosAnexos, prioridade, canalAbertura, clienteSolicitante, contatoSolicitante, enderecoAtendimento, dataAgendamentoAnalise, tecnicoResponsavelId, tecnicoResponsavelNome, dataInicioAnalise, dataFimAnalise, tempoAnaliseHoras, diagnosticoTecnico, causaIdentificada, procedimentoRealizado, aprovada, motivoReprovacao, valorAprovado, itensSubstituidos, servicosRefeitos, custosAdicionais, justificativaCustos, satisfacaoCliente, comentarioSatisfacao, status, dataResolucao, observacoesInternas, abertaPor );

        return reclamacaoGarantiaResponse;
    }

    @Override
    public void updateEntityFromDTO(ReclamacaoGarantiaRequest request, ReclamacaoGarantia entity) {
        if ( request == null ) {
            return;
        }

        if ( request.numeroReclamacao() != null ) {
            entity.setNumeroReclamacao( request.numeroReclamacao() );
        }
        if ( request.tipoReclamacao() != null ) {
            entity.setTipoReclamacao( request.tipoReclamacao() );
        }
        if ( request.problemaRelatado() != null ) {
            entity.setProblemaRelatado( request.problemaRelatado() );
        }
        if ( request.sintomasObservados() != null ) {
            entity.setSintomasObservados( request.sintomasObservados() );
        }
        if ( request.condicoesUso() != null ) {
            entity.setCondicoesUso( request.condicoesUso() );
        }
        if ( request.kilometragemAtual() != null ) {
            entity.setKilometragemAtual( request.kilometragemAtual() );
        }
        if ( request.tempoUsoDesdeServico() != null ) {
            entity.setTempoUsoDesdeServico( request.tempoUsoDesdeServico() );
        }
        if ( request.evidenciasFornecidas() != null ) {
            entity.setEvidenciasFornecidas( request.evidenciasFornecidas() );
        }
        if ( request.fotosProblema() != null ) {
            entity.setFotosProblema( request.fotosProblema() );
        }
        if ( request.videosProblema() != null ) {
            entity.setVideosProblema( request.videosProblema() );
        }
        if ( request.documentosAnexos() != null ) {
            entity.setDocumentosAnexos( request.documentosAnexos() );
        }
        if ( request.prioridade() != null ) {
            entity.setPrioridade( request.prioridade() );
        }
        if ( request.canalAbertura() != null ) {
            entity.setCanalAbertura( request.canalAbertura() );
        }
        if ( request.clienteSolicitante() != null ) {
            entity.setClienteSolicitante( request.clienteSolicitante() );
        }
        if ( request.contatoSolicitante() != null ) {
            entity.setContatoSolicitante( request.contatoSolicitante() );
        }
        if ( request.enderecoAtendimento() != null ) {
            entity.setEnderecoAtendimento( request.enderecoAtendimento() );
        }
        if ( request.dataAgendamentoAnalise() != null ) {
            entity.setDataAgendamentoAnalise( request.dataAgendamentoAnalise() );
        }
        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
        if ( request.observacoesInternas() != null ) {
            entity.setObservacoesInternas( request.observacoesInternas() );
        }
    }

    private String entityTecnicoResponsavelNomeCompleto(ReclamacaoGarantia reclamacaoGarantia) {
        if ( reclamacaoGarantia == null ) {
            return null;
        }
        Funcionario tecnicoResponsavel = reclamacaoGarantia.getTecnicoResponsavel();
        if ( tecnicoResponsavel == null ) {
            return null;
        }
        String nomeCompleto = tecnicoResponsavel.getNomeCompleto();
        if ( nomeCompleto == null ) {
            return null;
        }
        return nomeCompleto;
    }
}
