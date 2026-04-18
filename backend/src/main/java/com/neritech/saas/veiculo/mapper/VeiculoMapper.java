package com.neritech.saas.veiculo.mapper;

import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.veiculo.dto.VeiculoRequest;
import com.neritech.saas.veiculo.dto.VeiculoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VeiculoMapper {

    @Mapping(target = "cliente.id", source = "clienteId")
    @Mapping(target = "marca.id", source = "marcaId")
    @Mapping(target = "modelo.id", source = "modeloId")
    @Mapping(target = "anoModelo.id", source = "anoModeloId")
    @Mapping(target = "tipoCombustivel", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    Veiculo toEntity(VeiculoRequest request);

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "clienteNome", source = "cliente.nomeCompleto")
    @Mapping(target = "marcaId", source = "marca.id")
    @Mapping(target = "marcaNome", source = "marca.nome")
    @Mapping(target = "modeloId", source = "modelo.id")
    @Mapping(target = "modeloNome", source = "modelo.nome")
    @Mapping(target = "anoModeloId", source = "anoModelo.id")
    @Mapping(target = "anoFabricacao", source = "anoModelo.anoFabricacao")
    @Mapping(target = "anoModelo", source = "anoModelo.anoModelo")
    @Mapping(target = "combustivelId", source = "tipoCombustivel.id")
    @Mapping(target = "combustivelNome", source = "tipoCombustivel.nome")
    VeiculoResponse toResponse(Veiculo entity);

    @Mapping(target = "cliente.id", source = "clienteId")
    @Mapping(target = "marca.id", source = "marcaId")
    @Mapping(target = "modelo.id", source = "modeloId")
    @Mapping(target = "anoModelo.id", source = "anoModeloId")
    @Mapping(target = "tipoCombustivel", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    void updateEntityFromRequest(VeiculoRequest request, @MappingTarget Veiculo entity);
}
