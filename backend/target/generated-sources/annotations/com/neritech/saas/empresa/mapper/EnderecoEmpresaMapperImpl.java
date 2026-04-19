package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.EnderecoEmpresa;
import com.neritech.saas.empresa.domain.enums.TipoEndereco;
import com.neritech.saas.empresa.dto.EnderecoEmpresaRequest;
import com.neritech.saas.empresa.dto.EnderecoEmpresaResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:57-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class EnderecoEmpresaMapperImpl implements EnderecoEmpresaMapper {

    @Override
    public EnderecoEmpresa toEntity(EnderecoEmpresaRequest request) {
        if ( request == null ) {
            return null;
        }

        EnderecoEmpresa enderecoEmpresa = new EnderecoEmpresa();

        enderecoEmpresa.setTipoEndereco( request.tipoEndereco() );
        enderecoEmpresa.setCep( request.cep() );
        enderecoEmpresa.setLogradouro( request.logradouro() );
        enderecoEmpresa.setNumero( request.numero() );
        enderecoEmpresa.setComplemento( request.complemento() );
        enderecoEmpresa.setBairro( request.bairro() );
        enderecoEmpresa.setCidade( request.cidade() );
        enderecoEmpresa.setEstado( request.estado() );
        enderecoEmpresa.setPais( request.pais() );
        enderecoEmpresa.setPrincipal( request.principal() );
        enderecoEmpresa.setAtivo( request.ativo() );

        return enderecoEmpresa;
    }

    @Override
    public EnderecoEmpresaResponse toResponse(EnderecoEmpresa entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        String empresaNome = null;
        Long id = null;
        TipoEndereco tipoEndereco = null;
        String cep = null;
        String logradouro = null;
        String numero = null;
        String complemento = null;
        String bairro = null;
        String cidade = null;
        String estado = null;
        String pais = null;
        Boolean principal = null;
        Boolean ativo = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        empresaId = entityEmpresaId( entity );
        empresaNome = entityEmpresaNomeFantasia( entity );
        id = entity.getId();
        tipoEndereco = entity.getTipoEndereco();
        cep = entity.getCep();
        logradouro = entity.getLogradouro();
        numero = entity.getNumero();
        complemento = entity.getComplemento();
        bairro = entity.getBairro();
        cidade = entity.getCidade();
        estado = entity.getEstado();
        pais = entity.getPais();
        principal = entity.getPrincipal();
        ativo = entity.getAtivo();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        EnderecoEmpresaResponse enderecoEmpresaResponse = new EnderecoEmpresaResponse( id, empresaId, empresaNome, tipoEndereco, cep, logradouro, numero, complemento, bairro, cidade, estado, pais, principal, ativo, dataCadastro, dataAtualizacao );

        return enderecoEmpresaResponse;
    }

    @Override
    public void updateEntityFromRequest(EnderecoEmpresaRequest request, EnderecoEmpresa entity) {
        if ( request == null ) {
            return;
        }

        entity.setTipoEndereco( request.tipoEndereco() );
        entity.setCep( request.cep() );
        entity.setLogradouro( request.logradouro() );
        entity.setNumero( request.numero() );
        entity.setComplemento( request.complemento() );
        entity.setBairro( request.bairro() );
        entity.setCidade( request.cidade() );
        entity.setEstado( request.estado() );
        entity.setPais( request.pais() );
        entity.setPrincipal( request.principal() );
        entity.setAtivo( request.ativo() );
    }

    private Long entityEmpresaId(EnderecoEmpresa enderecoEmpresa) {
        if ( enderecoEmpresa == null ) {
            return null;
        }
        Empresa empresa = enderecoEmpresa.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        Long id = empresa.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityEmpresaNomeFantasia(EnderecoEmpresa enderecoEmpresa) {
        if ( enderecoEmpresa == null ) {
            return null;
        }
        Empresa empresa = enderecoEmpresa.getEmpresa();
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
