package com.neritech.saas.gestaoUsuarios.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.AbstractIntegrationTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class UsuarioFuncaoTenantIntegrationTest extends AbstractIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void jwtARejeitaFuncaoAdminDeBEFazRollbackSemBloquearFuncaoLocal() throws Exception {
        long a = empresa("A");
        long b = empresa("B");
        Funcao adminA = funcao(a, "ADMIN");
        Funcao leitorA = funcao(a, "LEITOR");
        Funcao adminB = funcao(b, "ADMIN");
        Usuario alvoA = usuario(a, "alvo" + a + "@t.co", leitorA);
        String tokenA = login(a, "admin" + a + "@t.co", adminA);
        String novoEmail = "novo" + a + "@t.co";

        mvc.perform(get("/v1/funcoes/{id}", adminB.getId())
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());

        // A user with role administration in A cannot forge FK to B while creating.
        mvc.perform(post("/usuarios")
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("Tentativa", novoEmail, "senha123", Set.of(adminB.getId()))))
                .andExpect(status().isNotFound());
        assertThat(jdbc.queryForObject("select count(*) from usuarios where email=?", Integer.class, novoEmail))
                .isZero();

        // Mixed local/foreign role IDs must also fail atomically.
        mvc.perform(put("/usuarios/{id}", alvoA.getId())
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("Nome indevido", alvoA.getEmail(), null, Set.of(leitorA.getId(), adminB.getId()))))
                .andExpect(status().isNotFound());
        assertThat(jdbc.queryForObject("select nome_completo from usuarios where id=?", String.class, alvoA.getId()))
                .isEqualTo("João da Silva");
        assertThat(jdbc.queryForObject("select count(*) from usuarios_funcoes where usuario_id=? and funcao_id=?",
                Integer.class, alvoA.getId(), leitorA.getId())).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from usuarios_funcoes where usuario_id=? and funcao_id=?",
                Integer.class, alvoA.getId(), adminB.getId())).isZero();

        // Explicit mismatch between the header and the JWT is always 403.
        mvc.perform(put("/usuarios/{id}", alvoA.getId())
                .header("Authorization", "Bearer " + tokenA)
                .header("X-Tenant-Id", Long.toString(b))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("Outro nome", alvoA.getEmail(), null, Set.of(leitorA.getId()))))
                .andExpect(status().isForbidden());

        // The normal in-tenant update still succeeds.
        mvc.perform(put("/usuarios/{id}", alvoA.getId())
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("Nome correto", alvoA.getEmail(), null, Set.of(leitorA.getId()))))
                .andExpect(status().isOk());
        assertThat(jdbc.queryForObject("select nome_completo from usuarios where id=?", String.class, alvoA.getId()))
                .isEqualTo("Nome correto");

        mvc.perform(post("/usuarios")
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload("Novo local", novoEmail, "senha123", Set.of(leitorA.getId()))))
                .andExpect(status().isOk());
        Long novoId = jdbc.queryForObject("select id from usuarios where email=?", Long.class, novoEmail);
        assertThat(jdbc.queryForObject("select empresa_id from usuarios where id=?", Long.class, novoId)).isEqualTo(a);
        assertThat(jdbc.queryForObject("select count(*) from usuarios_funcoes where usuario_id=? and funcao_id=?",
                Integer.class, novoId, leitorA.getId())).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from usuarios_funcoes where funcao_id=? and usuario_id in (?, ?)",
                Integer.class, adminB.getId(), alvoA.getId(), novoId)).isZero();
    }

    private long empresa(String prefix) {
        return jdbc.queryForObject(
                "insert into empresa (cnpj, nome_fantasia, ativo, data_cadastro) values (?, ?, true, now()) returning id",
                Long.class, prefix + Long.toString(System.nanoTime(), 36), "Tenant " + prefix);
    }

    private Funcao funcao(long empresaId, String nome) {
        return funcoes.save(TestDataBuilder.umaFuncao().comEmpresaId(empresaId)
                .comNome(nome).build());
    }

    private Usuario usuario(long empresaId, String email, Funcao role) {
        return usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresaId)
                .comEmail(email).comSenha(encoder.encode("senha123"))
                .comFuncao(role).build());
    }

    private String login(long empresaId, String email, Funcao role) throws Exception {
        usuario(empresaId, email, role);
        var response = mvc.perform(post("/auth/login")
                .header("X-Tenant-Id", Long.toString(empresaId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email", email, "senha", "senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString()).get("accessToken").asText();
    }

    private String payload(String nome, String email, String senha, Set<Long> roleIds) throws Exception {
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("nomeCompleto", nome);
        body.put("email", email);
        body.put("ativo", true);
        body.put("bloqueado", false);
        body.put("funcoesIds", roleIds);
        if (senha != null) body.put("senha", senha);
        return json.writeValueAsString(body);
    }
}
