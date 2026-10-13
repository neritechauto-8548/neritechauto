package com.neritech.saas.gestaoUsuarios.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.AbstractIntegrationTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.dto.LoginRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AuthenticationFlowIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FuncaoRepository funcaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Fluxo Completo: Registro -> Login -> Acesso Protegido")
    void fluxoCompletoAutenticacao() throws Exception {
        // 1. Criar usuário (via repository pois endpoint pode estar protegido ou não existir registro público)
        // O recurso /usuarios/{id} exige role ADMIN ou permissão GERAL_USUARIO.
        // O teste precisa autenticar um usuário com essa permissão de verdade.
        var funcaoAdmin = funcaoRepository.save(
                TestDataBuilder.umaFuncao().comNome("ADMIN").comEmpresaId(1L).build());
        var usuario = TestDataBuilder.umUsuario()
                .comEmail("integration@test.com")
                .comSenha(passwordEncoder.encode("senha123"))
                .comEmpresaId(1L)
                .comFuncao(funcaoAdmin)
                .build();
        usuarioRepository.save(usuario);

        // 2. Realizar Login
        LoginRequest loginRequest = TestDataBuilder.umLoginRequest()
                .comEmail("integration@test.com")
                .comSenha("senha123")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseBody).get("accessToken").asText();

        // 3. Acessar endpoint protegido
        mockMvc.perform(get("/usuarios/" + usuario.getId())
                .header("X-Tenant-Id", "1")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("integration@test.com"));

        // Usuário autenticado na empresa 1 não pode trocar de empresa pelo cabeçalho.
        mockMvc.perform(get("/usuarios/" + usuario.getId())
                .header("X-Tenant-Id", "2")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve falhar acesso sem token")
    void deveFalharAcessoSemToken() throws Exception {
        mockMvc.perform(get("/usuarios/1")
                .header("X-Tenant-Id", "1"))
                .andExpect(status().isForbidden()); // ou Unauthorized dependendo da config
    }
}
