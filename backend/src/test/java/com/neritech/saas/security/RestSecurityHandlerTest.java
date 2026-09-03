package com.neritech.saas.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;

class RestSecurityHandlerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Requisição não autenticada deve retornar 401 JSON")
    void deveRetornar401Json() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/clientes");
        MockHttpServletResponse response = new MockHttpServletResponse();
        RestAuthenticationEntryPoint entryPoint = new RestAuthenticationEntryPoint(objectMapper);

        entryPoint.commence(request, response, new BadCredentialsException("token inválido"));

        JsonNode body = objectMapper.readTree(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).startsWith("application/json");
        assertThat(body.get("status").asInt()).isEqualTo(401);
        assertThat(body.get("error").asText()).isEqualTo("Unauthorized");
        assertThat(body.get("path").asText()).isEqualTo("/v1/clientes");
    }

    @Test
    @DisplayName("Usuário autenticado sem permissão deve retornar 403 JSON")
    void deveRetornar403Json() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/v1/clientes/10");
        MockHttpServletResponse response = new MockHttpServletResponse();
        RestAccessDeniedHandler handler = new RestAccessDeniedHandler(objectMapper);

        handler.handle(request, response, new AccessDeniedException("sem permissão"));

        JsonNode body = objectMapper.readTree(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentType()).startsWith("application/json");
        assertThat(body.get("status").asInt()).isEqualTo(403);
        assertThat(body.get("error").asText()).isEqualTo("Forbidden");
        assertThat(body.get("path").asText()).isEqualTo("/v1/clientes/10");
    }
}
