package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.LocalizacaoEstoque;
import com.neritech.saas.estoque.domain.enums.TipoLocalizacao;
import com.neritech.saas.estoque.dto.LocalizacaoEstoqueRequest;
import com.neritech.saas.estoque.dto.LocalizacaoEstoqueResponse;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:26-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class LocalizacaoEstoqueMapperImpl implements LocalizacaoEstoqueMapper {

    @Override
    public LocalizacaoEstoque toEntity(LocalizacaoEstoqueRequest request) {
        if ( request == null ) {
            return null;
        }

        LocalizacaoEstoque localizacaoEstoque = new LocalizacaoEstoque();

        localizacaoEstoque.setEmpresaId( request.empresaId() );
        localizacaoEstoque.setCodigo( request.codigo() );
        localizacaoEstoque.setNome( request.nome() );
        localizacaoEstoque.setDescricao( request.descricao() );
        localizacaoEstoque.setTipoLocalizacao( request.tipoLocalizacao() );
        localizacaoEstoque.setSetor( request.setor() );
        localizacaoEstoque.setCorredor( request.corredor() );
        localizacaoEstoque.setPrateleira( request.prateleira() );
        localizacaoEstoque.setPosicao( request.posicao() );
        localizacaoEstoque.setCapacidadeMaxima( request.capacidadeMaxima() );
        localizacaoEstoque.setTemperaturaControlada( request.temperaturaControlada() );
        localizacaoEstoque.setTemperaturaMin( request.temperaturaMin() );
        localizacaoEstoque.setTemperaturaMax( request.temperaturaMax() );
        localizacaoEstoque.setUmidadeControlada( request.umidadeControlada() );
        localizacaoEstoque.setUmidadeMin( request.umidadeMin() );
        localizacaoEstoque.setUmidadeMax( request.umidadeMax() );
        localizacaoEstoque.setAcessoRestrito( request.acessoRestrito() );
        localizacaoEstoque.setUsuariosAcesso( request.usuariosAcesso() );
        localizacaoEstoque.setObservacoes( request.observacoes() );
        localizacaoEstoque.setAtivo( request.ativo() );

        return localizacaoEstoque;
    }

    @Override
    public LocalizacaoEstoqueResponse toResponse(LocalizacaoEstoque entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String codigo = null;
        String nome = null;
        String descricao = null;
        TipoLocalizacao tipoLocalizacao = null;
        String setor = null;
        String corredor = null;
        String prateleira = null;
        String posicao = null;
        BigDecimal capacidadeMaxima = null;
        Boolean temperaturaControlada = null;
        BigDecimal temperaturaMin = null;
        BigDecimal temperaturaMax = null;
        Boolean umidadeControlada = null;
        BigDecimal umidadeMin = null;
        BigDecimal umidadeMax = null;
        Boolean acessoRestrito = null;
        String usuariosAcesso = null;
        String observacoes = null;
        Boolean ativo = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        codigo = entity.getCodigo();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        tipoLocalizacao = entity.getTipoLocalizacao();
        setor = entity.getSetor();
        corredor = entity.getCorredor();
        prateleira = entity.getPrateleira();
        posicao = entity.getPosicao();
        capacidadeMaxima = entity.getCapacidadeMaxima();
        temperaturaControlada = entity.getTemperaturaControlada();
        temperaturaMin = entity.getTemperaturaMin();
        temperaturaMax = entity.getTemperaturaMax();
        umidadeControlada = entity.getUmidadeControlada();
        umidadeMin = entity.getUmidadeMin();
        umidadeMax = entity.getUmidadeMax();
        acessoRestrito = entity.getAcessoRestrito();
        usuariosAcesso = entity.getUsuariosAcesso();
        observacoes = entity.getObservacoes();
        ativo = entity.getAtivo();

        LocalizacaoEstoqueResponse localizacaoEstoqueResponse = new LocalizacaoEstoqueResponse( id, empresaId, codigo, nome, descricao, tipoLocalizacao, setor, corredor, prateleira, posicao, capacidadeMaxima, temperaturaControlada, temperaturaMin, temperaturaMax, umidadeControlada, umidadeMin, umidadeMax, acessoRestrito, usuariosAcesso, observacoes, ativo );

        return localizacaoEstoqueResponse;
    }

    @Override
    public void updateEntityFromRequest(LocalizacaoEstoqueRequest request, LocalizacaoEstoque entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setCodigo( request.codigo() );
        entity.setNome( request.nome() );
        entity.setDescricao( request.descricao() );
        entity.setTipoLocalizacao( request.tipoLocalizacao() );
        entity.setSetor( request.setor() );
        entity.setCorredor( request.corredor() );
        entity.setPrateleira( request.prateleira() );
        entity.setPosicao( request.posicao() );
        entity.setCapacidadeMaxima( request.capacidadeMaxima() );
        entity.setTemperaturaControlada( request.temperaturaControlada() );
        entity.setTemperaturaMin( request.temperaturaMin() );
        entity.setTemperaturaMax( request.temperaturaMax() );
        entity.setUmidadeControlada( request.umidadeControlada() );
        entity.setUmidadeMin( request.umidadeMin() );
        entity.setUmidadeMax( request.umidadeMax() );
        entity.setAcessoRestrito( request.acessoRestrito() );
        entity.setUsuariosAcesso( request.usuariosAcesso() );
        entity.setObservacoes( request.observacoes() );
        entity.setAtivo( request.ativo() );
    }
}
