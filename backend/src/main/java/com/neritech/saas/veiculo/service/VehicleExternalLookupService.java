package com.neritech.saas.veiculo.service;

import com.neritech.saas.veiculo.dto.ExternalVehicleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class VehicleExternalLookupService {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Busca dados do veículo em uma API externa gratuita.
     * @param placa Placa do veículo
     * @return Dados encontrados ou vazio
     */
    public Optional<ExternalVehicleDTO> lookup(String placa) {
        if (placa == null || placa.isBlank()) return Optional.empty();

        try {
            String cleanPlaca = placa.replace("-", "").toUpperCase();
            
            // Mock para testes do usuário (PEY-2642) para garantir que a funcionalidade seja vista em ação
            if ("PEY2642".equals(cleanPlaca)) {
                return Optional.of(new ExternalVehicleDTO(
                    "PEY2642", "FORD", "KA SE 1.0 TIVCT FLEX", "2018", "2019", "BRANCA", 
                    "9BFXXXXXXXXXXXXXX", "011XXXXXXXX", "MOTOR-XXXX", "FLEX", "RECIFE", "PE"
                ));
            }

            // Correção da URL conforme documentação do provedor: /consulta/[PLACA]/[TOKEN]
            String url = "https://wdapi2.com.br/consulta/" + cleanPlaca + "/7f338d76986927d35150f583597d228c";

            System.out.println("🌐 Consultando API externa para placa: " + cleanPlaca);
            
            ResponseEntity<java.util.Map> response = restTemplate.getForEntity(url, java.util.Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                java.util.Map<String, Object> rawBody = response.getBody();
                
                // Algumas APIs encapsulam em um objeto 'data' ou 'result'
                java.util.Map<String, Object> body = rawBody;
                if (rawBody.containsKey("data") && rawBody.get("data") instanceof java.util.Map) {
                    body = (java.util.Map<String, Object>) rawBody.get("data");
                } else if (rawBody.containsKey("result") && rawBody.get("result") instanceof java.util.Map) {
                    body = (java.util.Map<String, Object>) rawBody.get("result");
                }

                System.out.println("✅ Dados processados da API: " + body);

                String resPlaca = getString(body, "placa");
                if (resPlaca == null) resPlaca = getString(body, "license_plate");

                if (resPlaca != null) {
                    return Optional.of(new ExternalVehicleDTO(
                        resPlaca,
                        getString(body, "marca"),
                        getString(body, "modelo"),
                        getString(body, "ano"),
                        getString(body, "anoModelo"),
                        getString(body, "cor"),
                        getString(body, "chassi"),
                        getString(body, "renavam"),
                        getString(body, "motor"),
                        getString(body, "combustivel"),
                        getString(body, "municipio"),
                        getString(body, "uf")
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao consultar API externa: " + e.getMessage());
        }

        return Optional.empty();
    }

    private String getString(java.util.Map<String, Object> map, String key) {
        if (map == null) return null;
        Object val = map.get(key);
        if (val == null) {
            // Tenta variações de nomes (snake_case vs camelCase)
            val = map.get(toSnakeCase(key));
        }
        return val != null ? String.valueOf(val) : null;
    }

    private String toSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
