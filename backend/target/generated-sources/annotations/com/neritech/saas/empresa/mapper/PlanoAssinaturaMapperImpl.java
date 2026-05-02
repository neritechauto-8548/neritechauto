package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.domain.enums.SuporteTecnico;
import com.neritech.saas.empresa.dto.PlanoAssinaturaRequest;
import com.neritech.saas.empresa.dto.PlanoAssinaturaResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:27-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class PlanoAssinaturaMapperImpl implements PlanoAssinaturaMapper {

    @Override
    public PlanoAssinatura toEntity(PlanoAssinaturaRequest request) {
        if ( request == null ) {
            return null;
        }

        PlanoAssinatura planoAssinatura = new PlanoAssinatura();

        planoAssinatura.setNome( request.nome() );
        planoAssinatura.setDescricao( request.descricao() );
        planoAssinatura.setPrecoMensal( request.precoMensal() );
        planoAssinatura.setPrecoAnual( request.precoAnual() );
        planoAssinatura.setMaxUsuarios( request.maxUsuarios() );
        planoAssinatura.setMaxClientes( request.maxClientes() );
        planoAssinatura.setMaxVeiculos( request.maxVeiculos() );
        planoAssinatura.setMaxOsMes( request.maxOsMes() );
        planoAssinatura.setMaxProdutos( request.maxProdutos() );
        planoAssinatura.setMaxFornecedores( request.maxFornecedores() );
        planoAssinatura.setPossuiAppMobile( request.possuiAppMobile() );
        planoAssinatura.setPossuiApi( request.possuiApi() );
        planoAssinatura.setPossuiIntegracaoNfe( request.possuiIntegracaoNfe() );
        planoAssinatura.setPossuiBackupAutomatico( request.possuiBackupAutomatico() );
        planoAssinatura.setStorageGb( request.storageGb() );
        planoAssinatura.setSuporteTecnico( request.suporteTecnico() );
        planoAssinatura.setPeriodoTesteDias( request.periodoTesteDias() );
        planoAssinatura.setAtivo( request.ativo() );

        return planoAssinatura;
    }

    @Override
    public PlanoAssinaturaResponse toResponse(PlanoAssinatura entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        String descricao = null;
        BigDecimal precoMensal = null;
        BigDecimal precoAnual = null;
        Integer maxUsuarios = null;
        Integer maxClientes = null;
        Integer maxVeiculos = null;
        Integer maxOsMes = null;
        Integer maxProdutos = null;
        Integer maxFornecedores = null;
        Boolean possuiAppMobile = null;
        Boolean possuiApi = null;
        Boolean possuiIntegracaoNfe = null;
        Boolean possuiBackupAutomatico = null;
        Integer storageGb = null;
        SuporteTecnico suporteTecnico = null;
        Integer periodoTesteDias = null;
        Boolean ativo = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        precoMensal = entity.getPrecoMensal();
        precoAnual = entity.getPrecoAnual();
        maxUsuarios = entity.getMaxUsuarios();
        maxClientes = entity.getMaxClientes();
        maxVeiculos = entity.getMaxVeiculos();
        maxOsMes = entity.getMaxOsMes();
        maxProdutos = entity.getMaxProdutos();
        maxFornecedores = entity.getMaxFornecedores();
        possuiAppMobile = entity.getPossuiAppMobile();
        possuiApi = entity.getPossuiApi();
        possuiIntegracaoNfe = entity.getPossuiIntegracaoNfe();
        possuiBackupAutomatico = entity.getPossuiBackupAutomatico();
        storageGb = entity.getStorageGb();
        suporteTecnico = entity.getSuporteTecnico();
        periodoTesteDias = entity.getPeriodoTesteDias();
        ativo = entity.getAtivo();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        PlanoAssinaturaResponse planoAssinaturaResponse = new PlanoAssinaturaResponse( id, nome, descricao, precoMensal, precoAnual, maxUsuarios, maxClientes, maxVeiculos, maxOsMes, maxProdutos, maxFornecedores, possuiAppMobile, possuiApi, possuiIntegracaoNfe, possuiBackupAutomatico, storageGb, suporteTecnico, periodoTesteDias, ativo, dataCadastro, dataAtualizacao );

        return planoAssinaturaResponse;
    }

    @Override
    public void updateEntityFromRequest(PlanoAssinaturaRequest request, PlanoAssinatura entity) {
        if ( request == null ) {
            return;
        }

        entity.setNome( request.nome() );
        entity.setDescricao( request.descricao() );
        entity.setPrecoMensal( request.precoMensal() );
        entity.setPrecoAnual( request.precoAnual() );
        entity.setMaxUsuarios( request.maxUsuarios() );
        entity.setMaxClientes( request.maxClientes() );
        entity.setMaxVeiculos( request.maxVeiculos() );
        entity.setMaxOsMes( request.maxOsMes() );
        entity.setMaxProdutos( request.maxProdutos() );
        entity.setMaxFornecedores( request.maxFornecedores() );
        entity.setPossuiAppMobile( request.possuiAppMobile() );
        entity.setPossuiApi( request.possuiApi() );
        entity.setPossuiIntegracaoNfe( request.possuiIntegracaoNfe() );
        entity.setPossuiBackupAutomatico( request.possuiBackupAutomatico() );
        entity.setStorageGb( request.storageGb() );
        entity.setSuporteTecnico( request.suporteTecnico() );
        entity.setPeriodoTesteDias( request.periodoTesteDias() );
        entity.setAtivo( request.ativo() );
    }
}
