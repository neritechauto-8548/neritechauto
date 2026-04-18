package com.neritech.saas.gestaoUsuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.service.FuncaoService;
import com.neritech.saas.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FuncaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class FuncaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FuncaoService funcaoService;

    @MockBean
    private JwtService jwtService;

    private Funcao funcao;

    @BeforeEach
    void setUp() {
        funcao = TestDataBuilder.umaFuncao().build();
    }

    @Test
    @DisplayName("GET /funcoes - Deve listar funções")
    void deveListarFuncoes() throws Exception {
        // Arrange
        when(funcaoService.findAll()).thenReturn(List.of(funcao));

        // Act & Assert
        mockMvc.perform(get("/api/funcoes")
                .header("X-Tenant-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("ADMIN"));
    }

    @Test
    @DisplayName("POST /funcoes - Deve criar função")
    void deveCriarFuncao() throws Exception {
        // Arrange
        when(funcaoService.save(any(Funcao.class))).thenReturn(funcao);

        // Act & Assert
        mockMvc.perform(post("/api/funcoes")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(funcao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("ADMIN"));
    }
}
