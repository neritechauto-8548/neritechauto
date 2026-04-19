package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.AvaliacaoFuncionario;
import com.neritech.saas.rh.domain.enums.RecomendacaoAvaliacao;
import com.neritech.saas.rh.domain.enums.TipoAvaliacao;
import com.neritech.saas.rh.dto.AvaliacaoFuncionarioRequest;
import com.neritech.saas.rh.dto.AvaliacaoFuncionarioResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:46-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class AvaliacaoFuncionarioMapperImpl implements AvaliacaoFuncionarioMapper {

    @Override
    public AvaliacaoFuncionario toEntity(AvaliacaoFuncionarioRequest request) {
        if ( request == null ) {
            return null;
        }

        AvaliacaoFuncionario avaliacaoFuncionario = new AvaliacaoFuncionario();

        avaliacaoFuncionario.setFuncionarioId( request.funcionarioId() );
        avaliacaoFuncionario.setAvaliadorId( request.avaliadorId() );
        avaliacaoFuncionario.setTipoAvaliacao( request.tipoAvaliacao() );
        avaliacaoFuncionario.setPeriodoAvaliado( request.periodoAvaliado() );
        avaliacaoFuncionario.setDataAvaliacao( request.dataAvaliacao() );
        avaliacaoFuncionario.setNotaProdutividade( request.notaProdutividade() );
        avaliacaoFuncionario.setNotaQualidade( request.notaQualidade() );
        avaliacaoFuncionario.setNotaPontualidade( request.notaPontualidade() );
        avaliacaoFuncionario.setNotaTrabalhoEquipe( request.notaTrabalhoEquipe() );
        avaliacaoFuncionario.setNotaIniciativa( request.notaIniciativa() );
        avaliacaoFuncionario.setNotaConhecimentoTecnico( request.notaConhecimentoTecnico() );
        avaliacaoFuncionario.setNotaAtendimentoCliente( request.notaAtendimentoCliente() );
        avaliacaoFuncionario.setNotaOrganizacao( request.notaOrganizacao() );
        avaliacaoFuncionario.setNotaLideranca( request.notaLideranca() );
        avaliacaoFuncionario.setNotaFinal( request.notaFinal() );
        avaliacaoFuncionario.setPontosFortes( request.pontosFortes() );
        avaliacaoFuncionario.setPontosMelhoria( request.pontosMelhoria() );
        avaliacaoFuncionario.setMetasEstabelecidas( request.metasEstabelecidas() );
        avaliacaoFuncionario.setPlanoDesenvolvimento( request.planoDesenvolvimento() );
        avaliacaoFuncionario.setTreinamentosRecomendados( request.treinamentosRecomendados() );
        avaliacaoFuncionario.setRecomendacao( request.recomendacao() );
        avaliacaoFuncionario.setAumentoSalarialSugerido( request.aumentoSalarialSugerido() );
        avaliacaoFuncionario.setBonusSugerido( request.bonusSugerido() );
        avaliacaoFuncionario.setObservacoesAvaliador( request.observacoesAvaliador() );
        avaliacaoFuncionario.setComentariosFuncionario( request.comentariosFuncionario() );
        avaliacaoFuncionario.setDataCienciaFuncionario( request.dataCienciaFuncionario() );

        return avaliacaoFuncionario;
    }

    @Override
    public AvaliacaoFuncionarioResponse toResponse(AvaliacaoFuncionario entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long funcionarioId = null;
        Long avaliadorId = null;
        TipoAvaliacao tipoAvaliacao = null;
        String periodoAvaliado = null;
        LocalDate dataAvaliacao = null;
        BigDecimal notaProdutividade = null;
        BigDecimal notaQualidade = null;
        BigDecimal notaPontualidade = null;
        BigDecimal notaTrabalhoEquipe = null;
        BigDecimal notaIniciativa = null;
        BigDecimal notaConhecimentoTecnico = null;
        BigDecimal notaAtendimentoCliente = null;
        BigDecimal notaOrganizacao = null;
        BigDecimal notaLideranca = null;
        BigDecimal notaFinal = null;
        String pontosFortes = null;
        String pontosMelhoria = null;
        String metasEstabelecidas = null;
        String planoDesenvolvimento = null;
        String treinamentosRecomendados = null;
        RecomendacaoAvaliacao recomendacao = null;
        BigDecimal aumentoSalarialSugerido = null;
        BigDecimal bonusSugerido = null;
        String observacoesAvaliador = null;
        String comentariosFuncionario = null;
        LocalDate dataCienciaFuncionario = null;
        Long aprovadaPor = null;
        LocalDate dataAprovacao = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        funcionarioId = entity.getFuncionarioId();
        avaliadorId = entity.getAvaliadorId();
        tipoAvaliacao = entity.getTipoAvaliacao();
        periodoAvaliado = entity.getPeriodoAvaliado();
        dataAvaliacao = entity.getDataAvaliacao();
        notaProdutividade = entity.getNotaProdutividade();
        notaQualidade = entity.getNotaQualidade();
        notaPontualidade = entity.getNotaPontualidade();
        notaTrabalhoEquipe = entity.getNotaTrabalhoEquipe();
        notaIniciativa = entity.getNotaIniciativa();
        notaConhecimentoTecnico = entity.getNotaConhecimentoTecnico();
        notaAtendimentoCliente = entity.getNotaAtendimentoCliente();
        notaOrganizacao = entity.getNotaOrganizacao();
        notaLideranca = entity.getNotaLideranca();
        notaFinal = entity.getNotaFinal();
        pontosFortes = entity.getPontosFortes();
        pontosMelhoria = entity.getPontosMelhoria();
        metasEstabelecidas = entity.getMetasEstabelecidas();
        planoDesenvolvimento = entity.getPlanoDesenvolvimento();
        treinamentosRecomendados = entity.getTreinamentosRecomendados();
        recomendacao = entity.getRecomendacao();
        aumentoSalarialSugerido = entity.getAumentoSalarialSugerido();
        bonusSugerido = entity.getBonusSugerido();
        observacoesAvaliador = entity.getObservacoesAvaliador();
        comentariosFuncionario = entity.getComentariosFuncionario();
        dataCienciaFuncionario = entity.getDataCienciaFuncionario();
        aprovadaPor = entity.getAprovadaPor();
        dataAprovacao = entity.getDataAprovacao();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        AvaliacaoFuncionarioResponse avaliacaoFuncionarioResponse = new AvaliacaoFuncionarioResponse( id, funcionarioId, avaliadorId, tipoAvaliacao, periodoAvaliado, dataAvaliacao, notaProdutividade, notaQualidade, notaPontualidade, notaTrabalhoEquipe, notaIniciativa, notaConhecimentoTecnico, notaAtendimentoCliente, notaOrganizacao, notaLideranca, notaFinal, pontosFortes, pontosMelhoria, metasEstabelecidas, planoDesenvolvimento, treinamentosRecomendados, recomendacao, aumentoSalarialSugerido, bonusSugerido, observacoesAvaliador, comentariosFuncionario, dataCienciaFuncionario, aprovadaPor, dataAprovacao, dataCadastro, dataAtualizacao );

        return avaliacaoFuncionarioResponse;
    }

    @Override
    public void updateEntity(AvaliacaoFuncionarioRequest request, AvaliacaoFuncionario entity) {
        if ( request == null ) {
            return;
        }

        if ( request.funcionarioId() != null ) {
            entity.setFuncionarioId( request.funcionarioId() );
        }
        if ( request.avaliadorId() != null ) {
            entity.setAvaliadorId( request.avaliadorId() );
        }
        if ( request.tipoAvaliacao() != null ) {
            entity.setTipoAvaliacao( request.tipoAvaliacao() );
        }
        if ( request.periodoAvaliado() != null ) {
            entity.setPeriodoAvaliado( request.periodoAvaliado() );
        }
        if ( request.dataAvaliacao() != null ) {
            entity.setDataAvaliacao( request.dataAvaliacao() );
        }
        if ( request.notaProdutividade() != null ) {
            entity.setNotaProdutividade( request.notaProdutividade() );
        }
        if ( request.notaQualidade() != null ) {
            entity.setNotaQualidade( request.notaQualidade() );
        }
        if ( request.notaPontualidade() != null ) {
            entity.setNotaPontualidade( request.notaPontualidade() );
        }
        if ( request.notaTrabalhoEquipe() != null ) {
            entity.setNotaTrabalhoEquipe( request.notaTrabalhoEquipe() );
        }
        if ( request.notaIniciativa() != null ) {
            entity.setNotaIniciativa( request.notaIniciativa() );
        }
        if ( request.notaConhecimentoTecnico() != null ) {
            entity.setNotaConhecimentoTecnico( request.notaConhecimentoTecnico() );
        }
        if ( request.notaAtendimentoCliente() != null ) {
            entity.setNotaAtendimentoCliente( request.notaAtendimentoCliente() );
        }
        if ( request.notaOrganizacao() != null ) {
            entity.setNotaOrganizacao( request.notaOrganizacao() );
        }
        if ( request.notaLideranca() != null ) {
            entity.setNotaLideranca( request.notaLideranca() );
        }
        if ( request.notaFinal() != null ) {
            entity.setNotaFinal( request.notaFinal() );
        }
        if ( request.pontosFortes() != null ) {
            entity.setPontosFortes( request.pontosFortes() );
        }
        if ( request.pontosMelhoria() != null ) {
            entity.setPontosMelhoria( request.pontosMelhoria() );
        }
        if ( request.metasEstabelecidas() != null ) {
            entity.setMetasEstabelecidas( request.metasEstabelecidas() );
        }
        if ( request.planoDesenvolvimento() != null ) {
            entity.setPlanoDesenvolvimento( request.planoDesenvolvimento() );
        }
        if ( request.treinamentosRecomendados() != null ) {
            entity.setTreinamentosRecomendados( request.treinamentosRecomendados() );
        }
        if ( request.recomendacao() != null ) {
            entity.setRecomendacao( request.recomendacao() );
        }
        if ( request.aumentoSalarialSugerido() != null ) {
            entity.setAumentoSalarialSugerido( request.aumentoSalarialSugerido() );
        }
        if ( request.bonusSugerido() != null ) {
            entity.setBonusSugerido( request.bonusSugerido() );
        }
        if ( request.observacoesAvaliador() != null ) {
            entity.setObservacoesAvaliador( request.observacoesAvaliador() );
        }
        if ( request.comentariosFuncionario() != null ) {
            entity.setComentariosFuncionario( request.comentariosFuncionario() );
        }
        if ( request.dataCienciaFuncionario() != null ) {
            entity.setDataCienciaFuncionario( request.dataCienciaFuncionario() );
        }
    }
}
