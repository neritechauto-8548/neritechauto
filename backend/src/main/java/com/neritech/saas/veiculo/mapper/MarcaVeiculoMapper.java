package com.neritech.saas.veiculo.mapper;

import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.dto.MarcaVeiculoRequest;
import com.neritech.saas.veiculo.dto.MarcaVeiculoResponse;

public class MarcaVeiculoMapper {

    public static MarcaVeiculo toEntity(MarcaVeiculoRequest request) {
        if (request == null) {
            return null;
        }
        MarcaVeiculo marcaVeiculo = new MarcaVeiculo();
        marcaVeiculo.setNome(request.getNome());
        return marcaVeiculo;
    }

    public static MarcaVeiculoResponse toResponse(MarcaVeiculo marcaVeiculo) {
        if (marcaVeiculo == null) {
            return null;
        }
        MarcaVeiculoResponse response = new MarcaVeiculoResponse();
        response.setId(marcaVeiculo.getId());
        response.setEmpresaId(marcaVeiculo.getEmpresaId());
        response.setNome(marcaVeiculo.getNome());
        response.setCreatedDate(marcaVeiculo.getDataCadastro());
        response.setLastModifiedDate(marcaVeiculo.getDataAtualizacao());
        response.setCreatedBy(marcaVeiculo.getCriadoPor());
        response.setLastModifiedBy(marcaVeiculo.getAtualizadoPor());
        response.setVersion(marcaVeiculo.getVersao());
        return response;
    }

    public static void updateEntityFromRequest(MarcaVeiculoRequest request, MarcaVeiculo entity) {
        if (request == null || entity == null) {
            return;
        }
        entity.setNome(request.getNome());
    }
}
