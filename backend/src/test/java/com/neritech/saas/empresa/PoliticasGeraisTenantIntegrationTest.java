package com.neritech.saas.empresa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.AbstractIntegrationTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.jdbc.Sql;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
// application-test.properties desabilita Flyway; executa a migração real apenas nesta suíte.
@Sql(scripts = "classpath:db/migration/V276__adm011_politicas_gerais.sql",
     executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class PoliticasGeraisTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void salvaPorTenantPersisteEntreSessoesERegistraAutor() throws Exception {
        long a = empresa("A"), b = empresa("B");
        String adminA = login(a, "polA" + a + "@t.invalid", "ADMIN");
        String adminB = login(b, "polB" + b + "@t.invalid", "ADMIN");

        mvc.perform(get("/v1/politicas-gerais/locale").header("Authorization", "Bearer " + adminA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valorAplicado").value("pt-BR"))
                .andExpect(jsonPath("$.data.origem").value("PADRAO"));
        mvc.perform(post("/v1/politicas-gerais").header("Authorization", "Bearer " + adminA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("chave", "locale", "valor", "en-US"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.valorAplicado").value("en-US"));
        mvc.perform(get("/v1/politicas-gerais/locale").header("Authorization", "Bearer " + adminB))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valorAplicado").value("pt-BR"));
        // Nova autenticação, sem cache do cliente, lê do banco, não de sessionStorage.
        String novaSessaoA = login(a, "polA2" + a + "@t.invalid", "ADMIN");
        mvc.perform(get("/v1/politicas-gerais/locale").header("Authorization", "Bearer " + novaSessaoA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valorAplicado").value("en-US"));

        mvc.perform(put("/v1/politicas-gerais/locale").header("Authorization", "Bearer " + adminA)
                .header("If-Match", "0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("chave", "locale", "valor", "pt-BR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.versao").value(1));
        mvc.perform(put("/v1/politicas-gerais/locale").header("Authorization", "Bearer " + adminA)
                .header("If-Match", "0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("chave", "locale", "valor", "en-US"))))
                .andExpect(status().isConflict());

        assertThat(jdbc.queryForObject("""
            SELECT COUNT(*) FROM politicas_gerais_empresa WHERE empresa_id=? AND chave='locale'
            """, Long.class, a)).isEqualTo(1L);
        assertThat(jdbc.queryForObject("""
            SELECT COUNT(*) FROM politicas_gerais_empresa WHERE empresa_id=? AND chave='locale'
            """, Long.class, b)).isZero();
        assertThat(jdbc.queryForObject("""
            SELECT COUNT(*) FROM logs_alteracoes WHERE empresa_id=? AND contexto_operacao='ADM-011'
            """, Long.class, a)).isGreaterThanOrEqualTo(2L);
        mvc.perform(get("/v1/politicas-gerais/locale/historico")
                .header("Authorization", "Bearer " + adminA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    void negaInvalidoDuplicadoCredencialEAlteracaoSemCapacidade() throws Exception {
        long a = empresa("A");
        String token = login(a, "polValid" + a + "@t.invalid", "ADMIN");
        String viewer = login(a, "polView" + a + "@t.invalid", "SEM_ACESSO");
        mvc.perform(get("/v1/politicas-gerais")
                .header("Authorization", "Bearer " + viewer))
                .andExpect(status().isForbidden());
        mvc.perform(post("/v1/politicas-gerais").header("Authorization", "Bearer " + viewer)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"chave\":\"avisosAtivos\",\"valor\":\"true\"}"))
                .andExpect(status().isForbidden());
        for (var invalid : new String[][] {
            {"featureFlagHabilitarNF", "true"},
            {"validadeOrcamentoDias", "9999"},
            {"locale", "token-chave-secreta"},
            {"retencaoHistoricoDias", "5"}
        }) {
            mvc.perform(post("/v1/politicas-gerais").header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json.writeValueAsString(Map.of("chave", invalid[0], "valor", invalid[1]))))
                    .andExpect(status().isUnprocessableEntity());
        }
        mvc.perform(post("/v1/politicas-gerais").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"chave\":\"avisosAtivos\",\"valor\":\"true\"}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/v1/politicas-gerais").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"chave\":\"avisosAtivos\",\"valor\":\"false\"}"))
                .andExpect(status().isConflict());
        mvc.perform(put("/v1/politicas-gerais/locale").header("Authorization", "Bearer " + token)
                .header("If-Match", "0").contentType(MediaType.APPLICATION_JSON)
                .content("{\"chave\":\"avisosAtivos\",\"valor\":\"false\"}"))
                .andExpect(status().isUnprocessableEntity());
        mvc.perform(patch("/v1/politicas-gerais/avisosAtivos/status")
                .header("Authorization", "Bearer " + token).header("If-Match", "0")
                .contentType(MediaType.APPLICATION_JSON).content("{\"ativo\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.origem").value("PADRAO_INATIVA"));
        mvc.perform(get("/v1/politicas-gerais/avisosAtivos").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valorAplicado").value("true"));
    }

    private long empresa(String suffix) {
        return jdbc.queryForObject("""
            INSERT INTO empresa (cnpj, nome_fantasia, razao_social, ativo, data_cadastro)
            VALUES (?, ?, ?, true, now()) RETURNING id
            """, Long.class, suffix + "-" + Long.toString(System.nanoTime(), 36),
                "Empresa " + suffix, "Empresa " + suffix);
    }
    private String login(long empresa, String email, String role) throws Exception {
        var funcao = funcoes.save(TestDataBuilder.umaFuncao().comNome(role).comEmpresaId(empresa).build());
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa)
                .comEmail(email).comSenha(encoder.encode("senha123")).comFuncao(funcao).build());
        var response = mvc.perform(post("/auth/login").header("X-Tenant-Id", Long.toString(empresa))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email", email, "senha", "senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString()).get("accessToken").asText();
    }
}
