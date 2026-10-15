package com.neritech.saas.gestaoUsuarios.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.AbstractIntegrationTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.domain.Permissao;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.gestaoUsuarios.repository.PermissaoRepository;
import com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class AuthSessionTokenLengthIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired UsuarioRepository usuarios;
    @Autowired FuncaoRepository funcoes;
    @Autowired PermissaoRepository permissoes;
    @Autowired SessaoUsuarioRepository sessoes;
    @Autowired PasswordEncoder encoder;

    private final List<Long> userIds = new ArrayList<>();
    private final List<Long> roleIds = new ArrayList<>();
    private final List<Long> permissionIds = new ArrayList<>();

    @Test
    void loginRefreshELogoutPreservamJwtLongoSemQuebrarIsolamentoAB() throws Exception {
        long a = empresa("TOKEN-A");
        long b = empresa("TOKEN-B");
        Funcao adminA = funcao(a);
        Funcao adminB = funcao(b);
        // Real capability claims exercise the prior >255 storage failure.
        for (int i = 0; i < 15; i++) {
            String value = "TEST_SESSION_CAPABILITY_" + a + "_" + i + "_" + "X".repeat(80);
            Permissao permissao = permissoes.save(TestDataBuilder.umaPermissao()
                    .comChave("TOKEN_CAP_" + a + "_" + i).comValor(value).build());
            permissionIds.add(permissao.getId());
            jdbc.update("insert into funcoes_permissoes (funcao_id, permissao_id) values (?, ?)",
                    adminA.getId(), permissao.getId());
        }
        String emailA = "a" + a + "_" + "x".repeat(45) + "@" + "d".repeat(55) + "." + "e".repeat(50) + ".com";
        String emailB = "b" + b + "@t.co";
        Usuario userA = usuario(a, emailA, adminA);
        Usuario userB = usuario(b, emailB, adminB);

        JsonNode loginA = login(a, emailA);
        String accessA = loginA.get("accessToken").asText();
        String refreshA = loginA.get("refreshToken").asText();
        assertThat(accessA.length()).isGreaterThan(500);
        assertThat(refreshA.length()).isGreaterThan(255);
        assertThat(sessoes.findByTokenSessao(accessA)).isPresent();
        assertThat(sessoes.findByRefreshToken(refreshA)).isPresent();
        assertThat(jdbc.queryForObject(
                "select count(*) from information_schema.columns " +
                "where table_schema='public' and table_name='sessoes_usuario' " +
                "and column_name in ('token_sessao', 'refresh_token') and data_type='text'",
                Integer.class)).isEqualTo(2);

        JsonNode loginB = login(b, emailB);
        String accessB = loginB.get("accessToken").asText();
        mvc.perform(get("/usuarios/{id}", userA.getId())
                .header("Authorization", "Bearer " + accessA))
                .andExpect(status().isOk());
        mvc.perform(get("/usuarios/{id}", userB.getId())
                .header("Authorization", "Bearer " + accessB))
                .andExpect(status().isOk());
        mvc.perform(get("/usuarios/{id}", userA.getId())
                .header("Authorization", "Bearer " + accessA)
                .header("X-Tenant-Id", Long.toString(b)))
                .andExpect(status().isForbidden());

        // Refresh must also persist a long access token, atomically replacing the old one.
        var refreshResult = mvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("refreshToken", refreshA))))
                .andExpect(status().isOk()).andReturn();
        String refreshedA = json.readTree(refreshResult.getResponse().getContentAsString())
                .get("accessToken").asText();
        assertThat(refreshedA.length()).isGreaterThan(500);
        assertThat(sessoes.findByTokenSessao(refreshedA)).isPresent();
        mvc.perform(get("/usuarios/{id}", userA.getId())
                .header("Authorization", "Bearer " + refreshedA))
                .andExpect(status().isOk());

        mvc.perform(post("/auth/logout").header("Authorization", "Bearer " + refreshedA))
                .andExpect(status().isNoContent());
        assertThat(sessoes.findByTokenSessao(refreshedA).orElseThrow().getAtivo()).isFalse();
        mvc.perform(get("/usuarios/{id}", userA.getId())
                .header("Authorization", "Bearer " + refreshedA))
                .andExpect(status().isForbidden());
        // A's logout does not affect another tenant's session.
        mvc.perform(get("/usuarios/{id}", userB.getId())
                .header("Authorization", "Bearer " + accessB))
                .andExpect(status().isOk());
    }

    @AfterEach
    void cleanupOnlyThisTest() {
        if (!userIds.isEmpty()) {
            String binds = String.join(",", Collections.nCopies(userIds.size(), "?"));
            Object[] ids = userIds.toArray();
            jdbc.update("delete from logs_acesso where usuario_id in (" + binds + ")", ids);
            jdbc.update("delete from sessoes_usuario where usuario_id in (" + binds + ")", ids);
            jdbc.update("delete from perfis_usuario where usuario_id in (" + binds + ")", ids);
            jdbc.update("delete from usuarios_funcoes where usuario_id in (" + binds + ")", ids);
            jdbc.update("delete from usuarios where id in (" + binds + ")", ids);
        }
        if (!roleIds.isEmpty()) {
            String binds = String.join(",", Collections.nCopies(roleIds.size(), "?"));
            Object[] ids = roleIds.toArray();
            jdbc.update("delete from funcoes_permissoes where funcao_id in (" + binds + ")", ids);
            jdbc.update("delete from funcoes where id in (" + binds + ")", ids);
        }
        if (!permissionIds.isEmpty()) {
            String binds = String.join(",", Collections.nCopies(permissionIds.size(), "?"));
            jdbc.update("delete from permissoes where id in (" + binds + ")", permissionIds.toArray());
        }
    }

    private long empresa(String suffix) {
        return jdbc.queryForObject(
                "insert into empresa (cnpj, nome_fantasia, ativo, data_cadastro) values (?, ?, true, now()) returning id",
                Long.class, suffix + Long.toString(System.nanoTime(), 36), suffix);
    }

    private Funcao funcao(long company) {
        Funcao role = funcoes.save(TestDataBuilder.umaFuncao().comEmpresaId(company).comNome("ADMIN").build());
        roleIds.add(role.getId());
        return role;
    }

    private Usuario usuario(long company, String email, Funcao funcao) {
        Usuario user = usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(company)
                .comEmail(email).comSenha(encoder.encode("senha123")).comFuncao(funcao).build());
        userIds.add(user.getId());
        return user;
    }

    private JsonNode login(long company, String email) throws Exception {
        var response = mvc.perform(post("/auth/login")
                .header("X-Tenant-Id", Long.toString(company))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email", email, "senha", "senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString());
    }
}
