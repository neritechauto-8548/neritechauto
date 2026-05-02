package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Especialidade;
import com.neritech.saas.rh.domain.enums.NivelComplexidade;
import com.neritech.saas.rh.dto.EspecialidadeRequest;
import com.neritech.saas.rh.dto.EspecialidadeResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:22-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class EspecialidadeMapperImpl implements EspecialidadeMapper {

    @Override
    public Especialidade toEntity(EspecialidadeRequest request) {
        if ( request == null ) {
            return null;
        }

        Especialidade especialidade = new Especialidade();

        especialidade.setEmpresaId( request.empresaId() );
        especialidade.setNome( request.nome() );
        especialidade.setDescricao( request.descricao() );
        especialidade.setCategoria( request.categoria() );
        especialidade.setNivelComplexidade( request.nivelComplexidade() );
        especialidade.setCertificacaoObrigatoria( request.certificacaoObrigatoria() );
        especialidade.setNomeCertificacao( request.nomeCertificacao() );
        especialidade.setEntidadeCertificadora( request.entidadeCertificadora() );
        especialidade.setValidadeCertificacaoMeses( request.validadeCertificacaoMeses() );
        especialidade.setExperienciaMinimaAnos( request.experienciaMinimaAnos() );
        especialidade.setAdicionalSalarialPercentual( request.adicionalSalarialPercentual() );
        especialidade.setAtivo( request.ativo() );

        return especialidade;
    }

    @Override
    public EspecialidadeResponse toResponse(Especialidade entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nome = null;
        String descricao = null;
        String categoria = null;
        NivelComplexidade nivelComplexidade = null;
        Boolean certificacaoObrigatoria = null;
        String nomeCertificacao = null;
        String entidadeCertificadora = null;
        Integer validadeCertificacaoMeses = null;
        Integer experienciaMinimaAnos = null;
        BigDecimal adicionalSalarialPercentual = null;
        Boolean ativo = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        categoria = entity.getCategoria();
        nivelComplexidade = entity.getNivelComplexidade();
        certificacaoObrigatoria = entity.getCertificacaoObrigatoria();
        nomeCertificacao = entity.getNomeCertificacao();
        entidadeCertificadora = entity.getEntidadeCertificadora();
        validadeCertificacaoMeses = entity.getValidadeCertificacaoMeses();
        experienciaMinimaAnos = entity.getExperienciaMinimaAnos();
        adicionalSalarialPercentual = entity.getAdicionalSalarialPercentual();
        ativo = entity.getAtivo();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        EspecialidadeResponse especialidadeResponse = new EspecialidadeResponse( id, empresaId, nome, descricao, categoria, nivelComplexidade, certificacaoObrigatoria, nomeCertificacao, entidadeCertificadora, validadeCertificacaoMeses, experienciaMinimaAnos, adicionalSalarialPercentual, ativo, dataCadastro, dataAtualizacao );

        return especialidadeResponse;
    }

    @Override
    public void updateEntity(EspecialidadeRequest request, Especialidade entity) {
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
        if ( request.categoria() != null ) {
            entity.setCategoria( request.categoria() );
        }
        if ( request.nivelComplexidade() != null ) {
            entity.setNivelComplexidade( request.nivelComplexidade() );
        }
        if ( request.certificacaoObrigatoria() != null ) {
            entity.setCertificacaoObrigatoria( request.certificacaoObrigatoria() );
        }
        if ( request.nomeCertificacao() != null ) {
            entity.setNomeCertificacao( request.nomeCertificacao() );
        }
        if ( request.entidadeCertificadora() != null ) {
            entity.setEntidadeCertificadora( request.entidadeCertificadora() );
        }
        if ( request.validadeCertificacaoMeses() != null ) {
            entity.setValidadeCertificacaoMeses( request.validadeCertificacaoMeses() );
        }
        if ( request.experienciaMinimaAnos() != null ) {
            entity.setExperienciaMinimaAnos( request.experienciaMinimaAnos() );
        }
        if ( request.adicionalSalarialPercentual() != null ) {
            entity.setAdicionalSalarialPercentual( request.adicionalSalarialPercentual() );
        }
        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
    }
}
