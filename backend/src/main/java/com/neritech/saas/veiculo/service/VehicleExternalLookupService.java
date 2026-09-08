package com.neritech.saas.veiculo.service;

import com.neritech.saas.veiculo.dto.ExternalVehicleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class VehicleExternalLookupService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${neritech.vehicle.lookup.base-url:https://wdapi2.com.br/consulta}")
    private String baseUrl;

    @Value("${neritech.vehicle.lookup.token:${VEHICLE_LOOKUP_TOKEN:}}")
    private String token;

    /**
     * Enriquecimento opcional. Quando a credencial não estiver configurada, o
     * cadastro manual continua funcionando e nenhuma chamada externa é realizada.
     */
    public Optional<ExternalVehicleDTO> lookup(String placa) {
        String cleanPlaca = normalizePlate(placa);
        if (cleanPlaca == null || token == null || token.isBlank()) {
            return Optional.empty();
        }

        try {
            String url = normalizeBaseUrl(baseUrl) + "/" + cleanPlaca + "/" + token;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return Optional.empty();
            }

            Map<String, Object> body = unwrap(response.getBody());
            String resPlaca = firstNonBlank(getString(body, "placa"), getString(body, "license_plate"));
            if (resPlaca == null) {
                return Optional.empty();
            }

            return Optional.of(new ExternalVehicleDTO(
                    normalizePlate(resPlaca),
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
                    getString(body, "uf")));
        } catch (Exception ex) {
            // Não registrar URL, token, placa completa nem payload do provedor.
            log.warn("Consulta externa de veículo indisponível; cadastro manual permanece habilitado");
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> unwrap(Map rawBody) {
        Object data = rawBody.get("data");
        if (data instanceof Map<?, ?> dataMap) {
            return (Map<String, Object>) dataMap;
        }

        Object result = rawBody.get("result");
        if (result instanceof Map<?, ?> resultMap) {
            return (Map<String, Object>) resultMap;
        }

        return (Map<String, Object>) rawBody;
    }

    private String getString(Map<String, Object> map, String key) {
        if (map == null) {
            return null;
        }
        Object value = map.get(key);
        if (value == null) {
            value = map.get(toSnakeCase(key));
        }
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value).trim();
        return text.isEmpty() ? null : text;
    }

    private String firstNonBlank(String first, String second) {
        return first != null && !first.isBlank() ? first : second;
    }

    private String normalizePlate(String placa) {
        if (placa == null) {
            return null;
        }
        String normalized = placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        return normalized.isBlank() ? null : normalized;
    }

    private String normalizeBaseUrl(String value) {
        if (value == null || value.isBlank()) {
            return "https://wdapi2.com.br/consulta";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private String toSnakeCase(String value) {
        return value.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
