package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Treinamento;
import com.neritech.saas.rh.domain.enums.StatusTreinamento;
import com.neritech.saas.rh.domain.enums.TipoTreinamento;
import com.neritech.saas.rh.dto.TreinamentoRequest;
import com.neritech.saas.rh.dto.TreinamentoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:51-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TreinamentoMapperImpl implements TreinamentoMapper {

    @Override
    public Treinamento toEntity(TreinamentoRequest request) {
        if ( request == null ) {
            return null;
        }

        Treinamento treinamento = new Treinamento();

        treinamento.setEmpresaId( request.empresaId() );
        treinamento.setNomeTreinamento( request.nomeTreinamento() );
        treinamento.setDescricao( request.descricao() );
        treinamento.setCategoria( request.categoria() );
        treinamento.setTipoTreinamento( request.tipoTreinamento() );
        treinamento.setInstrutorInternoId( request.instrutorInternoId() );
        treinamento.setInstrutorExterno( request.instrutorExterno() );
        treinamento.setEmpresaTreinamento( request.empresaTreinamento() );
        treinamento.setCargaHoraria( request.cargaHoraria() );
        treinamento.setDataInicio( request.dataInicio() );
        treinamento.setDataFim( request.dataFim() );
        treinamento.setLocalRealizacao( request.localRealizacao() );
        treinamento.setCapacidadeMaxima( request.capacidadeMaxima() );
        treinamento.setCustoTotal( request.custoTotal() );
        treinamento.setCustoPorParticipante( request.custoPorParticipante() );
        treinamento.setObjetivoTreinamento( request.objetivoTreinamento() );
        treinamento.setConteudoProgramatico( request.conteudoProgramatico() );
        treinamento.setMaterialNecessario( request.materialNecessario() );
        treinamento.setCertificacaoEmitida( request.certificacaoEmitida() );
        treinamento.setNomeCertificacao( request.nomeCertificacao() );
        treinamento.setValidadeCertificacaoMeses( request.validadeCertificacaoMeses() );
        treinamento.setObrigatorio( request.obrigatorio() );
        treinamento.setCargosObrigatorios( request.cargosObrigatorios() );
        treinamento.setDepartamentosObrigatorios( request.departamentosObrigatorios() );
        treinamento.setStatus( request.status() );
        treinamento.setAvaliacaoMedia( request.avaliacaoMedia() );
        treinamento.setObservacoes( request.observacoes() );

        return treinamento;
    }

    @Override
    public TreinamentoResponse toResponse(Treinamento entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nomeTreinamento = null;
        String descricao = null;
        String categoria = null;
        TipoTreinamento tipoTreinamento = null;
        Long instrutorInternoId = null;
        String instrutorExterno = null;
        String empresaTreinamento = null;
        Integer cargaHoraria = null;
        LocalDate dataInicio = null;
        LocalDate dataFim = null;
        String localRealizacao = null;
        Integer capacidadeMaxima = null;
        BigDecimal custoTotal = null;
        BigDecimal custoPorParticipante = null;
        String objetivoTreinamento = null;
        String conteudoProgramatico = null;
        String materialNecessario = null;
        Boolean certificacaoEmitida = null;
        String nomeCertificacao = null;
        Integer validadeCertificacaoMeses = null;
        Boolean obrigatorio = null;
        String cargosObrigatorios = null;
        String departamentosObrigatorios = null;
        StatusTreinamento status = null;
        BigDecimal avaliacaoMedia = null;
        String observacoes = null;
        Long criadoPor = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nomeTreinamento = entity.getNomeTreinamento();
        descricao = entity.getDescricao();
        categoria = entity.getCategoria();
        tipoTreinamento = entity.getTipoTreinamento();
        instrutorInternoId = entity.getInstrutorInternoId();
        instrutorExterno = entity.getInstrutorExterno();
        empresaTreinamento = entity.getEmpresaTreinamento();
        cargaHoraria = entity.getCargaHoraria();
        dataInicio = entity.getDataInicio();
        dataFim = entity.getDataFim();
        localRealizacao = entity.getLocalRealizacao();
        capacidadeMaxima = entity.getCapacidadeMaxima();
        custoTotal = entity.getCustoTotal();
        custoPorParticipante = entity.getCustoPorParticipante();
        objetivoTreinamento = entity.getObjetivoTreinamento();
        conteudoProgramatico = entity.getConteudoProgramatico();
        materialNecessario = entity.getMaterialNecessario();
        certificacaoEmitida = entity.getCertificacaoEmitida();
        nomeCertificacao = entity.getNomeCertificacao();
        validadeCertificacaoMeses = entity.getValidadeCertificacaoMeses();
        obrigatorio = entity.getObrigatorio();
        cargosObrigatorios = entity.getCargosObrigatorios();
        departamentosObrigatorios = entity.getDepartamentosObrigatorios();
        status = entity.getStatus();
        avaliacaoMedia = entity.getAvaliacaoMedia();
        observacoes = entity.getObservacoes();
        criadoPor = entity.getCriadoPor();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        TreinamentoResponse treinamentoResponse = new TreinamentoResponse( id, empresaId, nomeTreinamento, descricao, categoria, tipoTreinamento, instrutorInternoId, instrutorExterno, empresaTreinamento, cargaHoraria, dataInicio, dataFim, localRealizacao, capacidadeMaxima, custoTotal, custoPorParticipante, objetivoTreinamento, conteudoProgramatico, materialNecessario, certificacaoEmitida, nomeCertificacao, validadeCertificacaoMeses, obrigatorio, cargosObrigatorios, departamentosObrigatorios, status, avaliacaoMedia, observacoes, criadoPor, dataCadastro, dataAtualizacao );

        return treinamentoResponse;
    }

    @Override
    public void updateEntity(TreinamentoRequest request, Treinamento entity) {
        if ( request == null ) {
            return;
        }

        if ( request.empresaId() != null ) {
            entity.setEmpresaId( request.empresaId() );
        }
        if ( request.nomeTreinamento() != null ) {
            entity.setNomeTreinamento( request.nomeTreinamento() );
        }
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.categoria() != null ) {
            entity.setCategoria( request.categoria() );
        }
        if ( request.tipoTreinamento() != null ) {
            entity.setTipoTreinamento( request.tipoTreinamento() );
        }
        if ( request.instrutorInternoId() != null ) {
            entity.setInstrutorInternoId( request.instrutorInternoId() );
        }
        if ( request.instrutorExterno() != null ) {
            entity.setInstrutorExterno( request.instrutorExterno() );
        }
        if ( request.empresaTreinamento() != null ) {
            entity.setEmpresaTreinamento( request.empresaTreinamento() );
        }
        if ( request.cargaHoraria() != null ) {
            entity.setCargaHoraria( request.cargaHoraria() );
        }
        if ( request.dataInicio() != null ) {
            entity.setDataInicio( request.dataInicio() );
        }
        if ( request.dataFim() != null ) {
            entity.setDataFim( request.dataFim() );
        }
        if ( request.localRealizacao() != null ) {
            entity.setLocalRealizacao( request.localRealizacao() );
        }
        if ( request.capacidadeMaxima() != null ) {
            entity.setCapacidadeMaxima( request.capacidadeMaxima() );
        }
        if ( request.custoTotal() != null ) {
            entity.setCustoTotal( request.custoTotal() );
        }
        if ( request.custoPorParticipante() != null ) {
            entity.setCustoPorParticipante( request.custoPorParticipante() );
        }
        if ( request.objetivoTreinamento() != null ) {
            entity.setObjetivoTreinamento( request.objetivoTreinamento() );
        }
        if ( request.conteudoProgramatico() != null ) {
            entity.setConteudoProgramatico( request.conteudoProgramatico() );
        }
        if ( request.materialNecessario() != null ) {
            entity.setMaterialNecessario( request.materialNecessario() );
        }
        if ( request.certificacaoEmitida() != null ) {
            entity.setCertificacaoEmitida( request.certificacaoEmitida() );
        }
        if ( request.nomeCertificacao() != null ) {
            entity.setNomeCertificacao( request.nomeCertificacao() );
        }
        if ( request.validadeCertificacaoMeses() != null ) {
            entity.setValidadeCertificacaoMeses( request.validadeCertificacaoMeses() );
        }
        if ( request.obrigatorio() != null ) {
            entity.setObrigatorio( request.obrigatorio() );
        }
        if ( request.cargosObrigatorios() != null ) {
            entity.setCargosObrigatorios( request.cargosObrigatorios() );
        }
        if ( request.departamentosObrigatorios() != null ) {
            entity.setDepartamentosObrigatorios( request.departamentosObrigatorios() );
        }
        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
        if ( request.avaliacaoMedia() != null ) {
            entity.setAvaliacaoMedia( request.avaliacaoMedia() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }
}
