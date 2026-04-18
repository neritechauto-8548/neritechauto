package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ComunicacaoEnviada;
import com.neritech.saas.comunicacao.dto.ComunicacaoEnviadaRequest;
import com.neritech.saas.comunicacao.dto.ComunicacaoEnviadaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ComunicacaoEnviadaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "dataEnvio", ignore = true) // Gerenciado pelo sistema
    @Mapping(target = "dataEntrega", ignore = true)
    @Mapping(target = "dataLeitura", ignore = true)
    @Mapping(target = "dataClique", ignore = true)
    @Mapping(target = "tentativasEnvio", ignore = true)
    @Mapping(target = "erroEnvio", ignore = true)
    @Mapping(target = "custoEnvio", ignore = true)
    @Mapping(target = "respostaDestinatario", ignore = true)
    @Mapping(target = "dataResposta", ignore = true)
    @Mapping(target = "avaliacaoConteudo", ignore = true)
    @Mapping(target = "motivoAvaliacao", ignore = true)
    ComunicacaoEnviada toEntity(ComunicacaoEnviadaRequest request);

    ComunicacaoEnviadaResponse toResponse(ComunicacaoEnviada entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntity(ComunicacaoEnviadaRequest request, @MappingTarget ComunicacaoEnviada entity);
}
