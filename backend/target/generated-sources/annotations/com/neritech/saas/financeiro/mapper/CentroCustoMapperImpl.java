package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.CentroCusto;
import com.neritech.saas.financeiro.domain.enums.TipoCentroCusto;
import com.neritech.saas.financeiro.dto.CentroCustoRequest;
import com.neritech.saas.financeiro.dto.CentroCustoResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:01-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CentroCustoMapperImpl implements CentroCustoMapper {

    @Override
    public CentroCusto toEntity(CentroCustoRequest request) {
        if ( request == null ) {
            return null;
        }

        CentroCusto centroCusto = new CentroCusto();

        centroCusto.setAtivo( request.ativo() );
        centroCusto.setCodigo( request.codigo() );
        centroCusto.setNome( request.nome() );
        centroCusto.setDescricao( request.descricao() );
        centroCusto.setTipo( request.tipo() );
        centroCusto.setResponsavelId( request.responsavelId() );
        centroCusto.setOrcamentoMensal( request.orcamentoMensal() );
        centroCusto.setOrcamentoAnual( request.orcamentoAnual() );

        return centroCusto;
    }

    @Override
    public CentroCustoResponse toResponse(CentroCusto entity) {
        if ( entity == null ) {
            return null;
        }

        String centroCustoPaiNome = null;
        Long id = null;
        Long empresaId = null;
        String codigo = null;
        String nome = null;
        String descricao = null;
        TipoCentroCusto tipo = null;
        Long responsavelId = null;
        BigDecimal orcamentoMensal = null;
        BigDecimal orcamentoAnual = null;
        Boolean ativo = null;

        centroCustoPaiNome = entityCentroCustoPaiNome( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        codigo = entity.getCodigo();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        tipo = entity.getTipo();
        responsavelId = entity.getResponsavelId();
        orcamentoMensal = entity.getOrcamentoMensal();
        orcamentoAnual = entity.getOrcamentoAnual();
        ativo = entity.getAtivo();

        Long centroCustoPaiId = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        CentroCustoResponse centroCustoResponse = new CentroCustoResponse( id, empresaId, codigo, nome, descricao, centroCustoPaiId, centroCustoPaiNome, tipo, responsavelId, orcamentoMensal, orcamentoAnual, ativo, createdAt, updatedAt );

        return centroCustoResponse;
    }

    @Override
    public void updateEntityFromDTO(CentroCustoRequest request, CentroCusto entity) {
        if ( request == null ) {
            return;
        }

        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
        if ( request.codigo() != null ) {
            entity.setCodigo( request.codigo() );
        }
        if ( request.nome() != null ) {
            entity.setNome( request.nome() );
        }
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.tipo() != null ) {
            entity.setTipo( request.tipo() );
        }
        if ( request.responsavelId() != null ) {
            entity.setResponsavelId( request.responsavelId() );
        }
        if ( request.orcamentoMensal() != null ) {
            entity.setOrcamentoMensal( request.orcamentoMensal() );
        }
        if ( request.orcamentoAnual() != null ) {
            entity.setOrcamentoAnual( request.orcamentoAnual() );
        }
    }

    private String entityCentroCustoPaiNome(CentroCusto centroCusto) {
        if ( centroCusto == null ) {
            return null;
        }
        CentroCusto centroCustoPai = centroCusto.getCentroCustoPai();
        if ( centroCustoPai == null ) {
            return null;
        }
        String nome = centroCustoPai.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
