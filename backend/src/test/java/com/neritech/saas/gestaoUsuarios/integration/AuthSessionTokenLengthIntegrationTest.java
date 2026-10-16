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

    @Test
    void resetConcorrenteRevogaTokensEPermiteNovoLoginSemImpactarOutraEmpresa() throws Exception {
        long tenantA = empresa("RESET-A");
        long tenantB = empresa("RESET-B");
        Usuario accountA = usuario(tenantA, "reset" + tenantA + "@test.com", funcao(tenantA));
        Usuario accountB = usuario(tenantB, "reset" + tenantB + "@test.com", funcao(tenantB));

        JsonNode originalA = login(tenantA, accountA.getEmail());
        JsonNode originalB = login(tenantB, accountB.getEmail());
        String oldAccessA = originalA.get("accessToken").asText();
        String oldRefreshA = originalA.get("refreshToken").asText();
        String accessB = originalB.get("accessToken").asText();

        String expiredRaw = "E".repeat(43);
        String expiredHash = java.util.HexFormat.of().formatHex(java.security.MessageDigest
                .getInstance("SHA-256").digest(expiredRaw.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        jdbc.update("insert into tokens_recuperacao_senha(token,usuario_id,data_criacao,data_expiracao,usado) values(?,?,now(),now()-interval '1 minute',false)",
                expiredHash, accountA.getId());
        mvc.perform(post("/auth/reset-password").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("token", expiredRaw, "novaSenha", "Nova frase-senha válida 2026 ✓"))))
                .andExpect(status().isBadRequest());

        String raw = "R".repeat(43);
        String hash = java.util.HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
                .digest(raw.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        jdbc.update("insert into tokens_recuperacao_senha(token,usuario_id,data_criacao,data_expiracao,usado) values(?,?,now(),now()+interval '30 minutes',false)",
                hash, accountA.getId());
        String newPassword = "Minha nova frase-senha segura ✓ 2026";
        String payload = json.writeValueAsString(Map.of("token", raw, "novaSenha", newPassword));
        java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newFixedThreadPool(2);
        java.util.concurrent.CountDownLatch start = new java.util.concurrent.CountDownLatch(1);
        try {
            java.util.concurrent.Callable<Integer> submit = () -> {
                start.await();
                return mvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                        .andReturn().getResponse().getStatus();
            };
            var first = pool.submit(submit);
            var second = pool.submit(submit);
            start.countDown();
            List<Integer> results = List.of(first.get(30, java.util.concurrent.TimeUnit.SECONDS),
                    second.get(30, java.util.concurrent.TimeUnit.SECONDS));
            assertThat(results).containsExactlyInAnyOrder(200, 400);
        } finally {
            pool.shutdownNow();
        }

        assertThat(jdbc.queryForObject("select count(*) from tokens_recuperacao_senha where usuario_id = ? and usado = true",
                Long.class, accountA.getId())).isEqualTo(2L);
        assertThat(encoder.matches(newPassword, usuarios.findById(accountA.getId()).orElseThrow().getSenha()))
                .isTrue();
        assertThat(sessoes.findByTokenSessao(oldAccessA).orElseThrow().getAtivo()).isFalse();

        mvc.perform(get("/usuarios/{id}", accountA.getId())
                .header("Authorization", "Bearer " + oldAccessA)).andExpect(status().isForbidden());
        mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("refreshToken", oldRefreshA))))
                .andExpect(status().isUnauthorized());

        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email", accountA.getEmail(), "senha", "senha123"))))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email", accountA.getEmail(), "senha", newPassword))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.accessToken").exists());

        mvc.perform(get("/usuarios/{id}", accountB.getId())
                .header("Authorization", "Bearer " + accessB)).andExpect(status().isOk());
    }

    @AfterEach
    void cleanupOnlyThisTest() {
        if (!userIds.isEmpty()) {
            String binds = String.join(",", Collections.nCopies(userIds.size(), "?"));
            Object[] ids = userIds.toArray();
            jdbc.update("delete from tokens_recuperacao_senha where usuario_id in (" + binds + ")", ids);
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
