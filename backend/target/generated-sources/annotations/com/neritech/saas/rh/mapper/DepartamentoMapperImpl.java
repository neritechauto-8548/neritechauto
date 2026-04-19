package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Departamento;
import com.neritech.saas.rh.dto.DepartamentoRequest;
import com.neritech.saas.rh.dto.DepartamentoResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:23-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class DepartamentoMapperImpl implements DepartamentoMapper {

    @Override
    public Departamento toEntity(DepartamentoRequest request) {
        if ( request == null ) {
            return null;
        }

        Departamento departamento = new Departamento();

        departamento.setEmpresaId( request.empresaId() );
        departamento.setNome( request.nome() );
        departamento.setDescricao( request.descricao() );
        departamento.setCodigo( request.codigo() );
        departamento.setDepartamentoPaiId( request.departamentoPaiId() );
        departamento.setGerenteId( request.gerenteId() );
        departamento.setCentroCusto( request.centroCusto() );
        departamento.setAtivo( request.ativo() );

        return departamento;
    }

    @Override
    public DepartamentoResponse toResponse(Departamento entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nome = null;
        String descricao = null;
        String codigo = null;
        Long departamentoPaiId = null;
        Long gerenteId = null;
        String centroCusto = null;
        Boolean ativo = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        codigo = entity.getCodigo();
        departamentoPaiId = entity.getDepartamentoPaiId();
        gerenteId = entity.getGerenteId();
        centroCusto = entity.getCentroCusto();
        ativo = entity.getAtivo();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        DepartamentoResponse departamentoResponse = new DepartamentoResponse( id, empresaId, nome, descricao, codigo, departamentoPaiId, gerenteId, centroCusto, ativo, dataCadastro, dataAtualizacao );

        return departamentoResponse;
    }

    @Override
    public void updateEntity(DepartamentoRequest request, Departamento entity) {
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
        if ( request.codigo() != null ) {
            entity.setCodigo( request.codigo() );
        }
        if ( request.departamentoPaiId() != null ) {
            entity.setDepartamentoPaiId( request.departamentoPaiId() );
        }
        if ( request.gerenteId() != null ) {
            entity.setGerenteId( request.gerenteId() );
        }
        if ( request.centroCusto() != null ) {
            entity.setCentroCusto( request.centroCusto() );
        }
        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
    }
}
