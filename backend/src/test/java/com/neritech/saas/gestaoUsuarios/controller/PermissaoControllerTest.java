package com.neritech.saas.gestaoUsuarios.controller;

import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.Permissao;
import com.neritech.saas.gestaoUsuarios.service.PermissaoService;
import com.neritech.saas.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PermissaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PermissaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PermissaoService permissaoService;

    @MockBean
    private JwtService jwtService;

    private Permissao permissao;

    @BeforeEach
    void setUp() {
        permissao = TestDataBuilder.umaPermissao().build();
    }

    @Test
    @DisplayName("GET /permissoes - Deve listar permissões")
    void deveListarPermissoes() throws Exception {
        // Arrange
        when(permissaoService.findAll()).thenReturn(List.of(permissao));

        // Act & Assert
        mockMvc.perform(get("/api/permissoes")
                .header("X-Tenant-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("USUARIO.READ"));
    }
}
