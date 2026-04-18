package com.neritech.saas.gestaoUsuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import com.neritech.saas.gestaoUsuarios.service.UsuarioService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtService jwtService;

    private UsuarioRequest usuarioRequest;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
        usuarioRequest = TestDataBuilder.umUsuarioRequest().build();
        usuarioResponse = new UsuarioResponse();
        usuarioResponse.setId(1L);
        usuarioResponse.setNomeCompleto(usuarioRequest.getNomeCompleto());
        usuarioResponse.setEmail(usuarioRequest.getEmail());
    }

    @Test
    @DisplayName("POST /usuarios - Deve criar usuário com sucesso")
    void deveCriarUsuario() throws Exception {
        // Arrange
        when(usuarioService.create(any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        // Act & Assert
        mockMvc.perform(post("/api/usuarios")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value(usuarioRequest.getEmail()));
    }

    @Test
    @DisplayName("GET /usuarios/{id} - Deve retornar usuário")
    void deveRetornarUsuarioPorId() throws Exception {
        // Arrange
        when(usuarioService.findById(1L)).thenReturn(usuarioResponse);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/1")
                .header("X-Tenant-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - Deve atualizar usuário")
    void deveAtualizarUsuario() throws Exception {
        // Arrange
        when(usuarioService.update(eq(1L), any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        // Act & Assert
        mockMvc.perform(put("/api/usuarios/1")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(usuarioRequest.getEmail()));
    }
}
