package com.neritech.saas.veiculo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalVehicleDTO(
    @JsonProperty("placa") String placa,
    @JsonProperty("marca") String marca,
    @JsonProperty("modelo") String modelo,
    @JsonProperty("ano") String ano,
    @JsonProperty("anoModelo") String anoModelo,
    @JsonProperty("cor") String cor,
    @JsonProperty("chassi") String chassi,
    @JsonProperty("renavam") String renavam,
    @JsonProperty("motor") String motor,
    @JsonProperty("combustivel") String combustivel,
    @JsonProperty("municipio") String municipio,
    @JsonProperty("uf") String uf
) {}
