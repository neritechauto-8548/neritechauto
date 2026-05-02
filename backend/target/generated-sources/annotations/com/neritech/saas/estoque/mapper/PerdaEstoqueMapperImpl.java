package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.LocalizacaoEstoque;
import com.neritech.saas.estoque.domain.PerdaEstoque;
import com.neritech.saas.estoque.domain.enums.TipoPerda;
import com.neritech.saas.estoque.dto.PerdaEstoqueRequest;
import com.neritech.saas.estoque.dto.PerdaEstoqueResponse;
import com.neritech.saas.produtoServico.domain.Produto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:31-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class PerdaEstoqueMapperImpl implements PerdaEstoqueMapper {

    @Override
    public PerdaEstoque toEntity(PerdaEstoqueRequest request) {
        if ( request == null ) {
            return null;
        }

        PerdaEstoque perdaEstoque = new PerdaEstoque();

        perdaEstoque.setLoteNumero( request.loteNumero() );
        perdaEstoque.setQuantidadePerdida( request.quantidadePerdida() );
        perdaEstoque.setTipoPerda( request.tipoPerda() );
        perdaEstoque.setDescricao( request.descricao() );
        perdaEstoque.setValorPerda( request.valorPerda() );
        perdaEstoque.setResponsavelPerda( request.responsavelPerda() );
        perdaEstoque.setFoiSegurado( request.foiSegurado() );
        perdaEstoque.setNumeroSinistro( request.numeroSinistro() );
        perdaEstoque.setValorIndenizado( request.valorIndenizado() );
        perdaEstoque.setDataOcorrencia( request.dataOcorrencia() );
        perdaEstoque.setDataDescoberta( request.dataDescoberta() );
        perdaEstoque.setBoletimOcorrencia( request.boletimOcorrencia() );
        perdaEstoque.setFotosComprovantesUrl( request.fotosComprovantesUrl() );
        perdaEstoque.setObservacoes( request.observacoes() );
        perdaEstoque.setAprovadoPor( request.aprovadoPor() );
        perdaEstoque.setUsuarioCadastro( request.usuarioCadastro() );

        return perdaEstoque;
    }

    @Override
    public PerdaEstoqueResponse toResponse(PerdaEstoque entity) {
        if ( entity == null ) {
            return null;
        }

        Long produtoId = null;
        String produtoNome = null;
        Long localizacaoId = null;
        String localizacaoNome = null;
        Long id = null;
        String loteNumero = null;
        BigDecimal quantidadePerdida = null;
        TipoPerda tipoPerda = null;
        String descricao = null;
        BigDecimal valorPerda = null;
        String responsavelPerda = null;
        Boolean foiSegurado = null;
        String numeroSinistro = null;
        BigDecimal valorIndenizado = null;
        LocalDate dataOcorrencia = null;
        LocalDate dataDescoberta = null;
        String boletimOcorrencia = null;
        String fotosComprovantesUrl = null;
        String observacoes = null;
        Long aprovadoPor = null;
        LocalDateTime dataAprovacao = null;
        Long usuarioCadastro = null;

        produtoId = entityProdutoId( entity );
        produtoNome = entityProdutoNome( entity );
        localizacaoId = entityLocalizacaoId( entity );
        localizacaoNome = entityLocalizacaoNome( entity );
        id = entity.getId();
        loteNumero = entity.getLoteNumero();
        quantidadePerdida = entity.getQuantidadePerdida();
        tipoPerda = entity.getTipoPerda();
        descricao = entity.getDescricao();
        valorPerda = entity.getValorPerda();
        responsavelPerda = entity.getResponsavelPerda();
        foiSegurado = entity.getFoiSegurado();
        numeroSinistro = entity.getNumeroSinistro();
        valorIndenizado = entity.getValorIndenizado();
        dataOcorrencia = entity.getDataOcorrencia();
        dataDescoberta = entity.getDataDescoberta();
        boletimOcorrencia = entity.getBoletimOcorrencia();
        fotosComprovantesUrl = entity.getFotosComprovantesUrl();
        observacoes = entity.getObservacoes();
        aprovadoPor = entity.getAprovadoPor();
        dataAprovacao = entity.getDataAprovacao();
        usuarioCadastro = entity.getUsuarioCadastro();

        PerdaEstoqueResponse perdaEstoqueResponse = new PerdaEstoqueResponse( id, produtoId, produtoNome, loteNumero, quantidadePerdida, tipoPerda, descricao, valorPerda, responsavelPerda, localizacaoId, localizacaoNome, foiSegurado, numeroSinistro, valorIndenizado, dataOcorrencia, dataDescoberta, boletimOcorrencia, fotosComprovantesUrl, observacoes, aprovadoPor, dataAprovacao, usuarioCadastro );

        return perdaEstoqueResponse;
    }

    @Override
    public void updateEntityFromRequest(PerdaEstoqueRequest request, PerdaEstoque entity) {
        if ( request == null ) {
            return;
        }

        entity.setLoteNumero( request.loteNumero() );
        entity.setQuantidadePerdida( request.quantidadePerdida() );
        entity.setTipoPerda( request.tipoPerda() );
        entity.setDescricao( request.descricao() );
        entity.setValorPerda( request.valorPerda() );
        entity.setResponsavelPerda( request.responsavelPerda() );
        entity.setFoiSegurado( request.foiSegurado() );
        entity.setNumeroSinistro( request.numeroSinistro() );
        entity.setValorIndenizado( request.valorIndenizado() );
        entity.setDataOcorrencia( request.dataOcorrencia() );
        entity.setDataDescoberta( request.dataDescoberta() );
        entity.setBoletimOcorrencia( request.boletimOcorrencia() );
        entity.setFotosComprovantesUrl( request.fotosComprovantesUrl() );
        entity.setObservacoes( request.observacoes() );
        entity.setAprovadoPor( request.aprovadoPor() );
        entity.setUsuarioCadastro( request.usuarioCadastro() );
    }

    private Long entityProdutoId(PerdaEstoque perdaEstoque) {
        if ( perdaEstoque == null ) {
            return null;
        }
        Produto produto = perdaEstoque.getProduto();
        if ( produto == null ) {
            return null;
        }
        Long id = produto.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityProdutoNome(PerdaEstoque perdaEstoque) {
        if ( perdaEstoque == null ) {
            return null;
        }
        Produto produto = perdaEstoque.getProduto();
        if ( produto == null ) {
            return null;
        }
        String nome = produto.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private Long entityLocalizacaoId(PerdaEstoque perdaEstoque) {
        if ( perdaEstoque == null ) {
            return null;
        }
        LocalizacaoEstoque localizacao = perdaEstoque.getLocalizacao();
        if ( localizacao == null ) {
            return null;
        }
        Long id = localizacao.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityLocalizacaoNome(PerdaEstoque perdaEstoque) {
        if ( perdaEstoque == null ) {
            return null;
        }
        LocalizacaoEstoque localizacao = perdaEstoque.getLocalizacao();
        if ( localizacao == null ) {
            return null;
        }
        String nome = localizacao.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
