package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.PlanoConta;
import com.neritech.saas.financeiro.domain.enums.NaturezaSaldo;
import com.neritech.saas.financeiro.domain.enums.TipoContaContabil;
import com.neritech.saas.financeiro.dto.PlanoContaRequest;
import com.neritech.saas.financeiro.dto.PlanoContaResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:04-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class PlanoContaMapperImpl implements PlanoContaMapper {

    @Override
    public PlanoConta toEntity(PlanoContaRequest request) {
        if ( request == null ) {
            return null;
        }

        PlanoConta planoConta = new PlanoConta();

        planoConta.setAceitaLancamento( request.aceitaLancamento() );
        planoConta.setAtivo( request.ativo() );
        planoConta.setCodigo( request.codigo() );
        planoConta.setNome( request.nome() );
        planoConta.setNivel( request.nivel() );
        planoConta.setTipoConta( request.tipoConta() );
        planoConta.setNaturezaSaldo( request.naturezaSaldo() );

        return planoConta;
    }

    @Override
    public PlanoContaResponse toResponse(PlanoConta entity) {
        if ( entity == null ) {
            return null;
        }

        String contaPaiNome = null;
        Long id = null;
        Long empresaId = null;
        String codigo = null;
        String nome = null;
        Integer nivel = null;
        TipoContaContabil tipoConta = null;
        NaturezaSaldo naturezaSaldo = null;
        Boolean aceitaLancamento = null;
        Boolean ativo = null;

        contaPaiNome = entityContaPaiNome( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        codigo = entity.getCodigo();
        nome = entity.getNome();
        nivel = entity.getNivel();
        tipoConta = entity.getTipoConta();
        naturezaSaldo = entity.getNaturezaSaldo();
        aceitaLancamento = entity.getAceitaLancamento();
        ativo = entity.getAtivo();

        Long contaPaiId = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        PlanoContaResponse planoContaResponse = new PlanoContaResponse( id, empresaId, codigo, nome, contaPaiId, contaPaiNome, nivel, tipoConta, naturezaSaldo, aceitaLancamento, ativo, createdAt, updatedAt );

        return planoContaResponse;
    }

    @Override
    public void updateEntityFromDTO(PlanoContaRequest request, PlanoConta entity) {
        if ( request == null ) {
            return;
        }

        if ( request.aceitaLancamento() != null ) {
            entity.setAceitaLancamento( request.aceitaLancamento() );
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
        if ( request.nivel() != null ) {
            entity.setNivel( request.nivel() );
        }
        if ( request.tipoConta() != null ) {
            entity.setTipoConta( request.tipoConta() );
        }
        if ( request.naturezaSaldo() != null ) {
            entity.setNaturezaSaldo( request.naturezaSaldo() );
        }
    }

    private String entityContaPaiNome(PlanoConta planoConta) {
        if ( planoConta == null ) {
            return null;
        }
        PlanoConta contaPai = planoConta.getContaPai();
        if ( contaPai == null ) {
            return null;
        }
        String nome = contaPai.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
