package com.neritech.saas.gestaoUsuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.service.FuncaoService;
import com.neritech.saas.security.JwtService;
import com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import com.neritech.saas.gestaoUsuarios.dto.FuncaoResponse;
import org.springframework.data.domain.PageImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "neritech.tenancy.web.enabled=false")
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
    private SessaoUsuarioRepository sessaoUsuarioRepository;

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
        var response = new FuncaoResponse(funcao.getId(), funcao.getEmpresaId(), funcao.getNome(), funcao.getDescricao(),
                funcao.getSistema(), funcao.getAtivo(), 0, Set.of(), funcao.getDataAtualizacao(), funcao.getVersao());
        when(funcaoService.search(any(), any(), any())).thenReturn(new PageImpl<>(List.of(response)));

        // Act & Assert
        mockMvc.perform(get("/v1/funcoes")
                .header("X-Tenant-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].nome").value("ADMIN"));
    }

    @Test
    @DisplayName("POST /funcoes - Deve criar função")
    void deveCriarFuncao() throws Exception {
        // Arrange
        var response = new FuncaoResponse(funcao.getId(), funcao.getEmpresaId(), funcao.getNome(), funcao.getDescricao(),
                funcao.getSistema(), funcao.getAtivo(), 0, Set.of(), funcao.getDataAtualizacao(), funcao.getVersao());
        when(funcaoService.create(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/v1/funcoes")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"ADMINISTRATIVO\",\"permissaoIds\":[]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nome").value("ADMIN"));
    }
}
