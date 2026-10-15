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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class EmpresaTenantIntegrationTest extends AbstractIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void empresaANaoPodeConsultarEditarExcluirNemEnviarLogoParaEmpresaB() throws Exception {
        long empresaA = empresa("A");
        long empresaB = empresa("B");
        String tokenA = login(empresaA, "a" + empresaA + "@t.co", "ADMIN");
        String tokenB = login(empresaB, "b" + empresaB + "@t.co", "ADMIN");
        String viewerA = login(empresaA, "v" + empresaA + "@t.co", "LEITOR");

        mvc.perform(get("/v1/empresas/{id}", empresaA).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk());
        mvc.perform(get("/v1/empresas/{id}", empresaB).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        mvc.perform(get("/v1/empresas/{id}", empresaB).header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk());

        // Download da logo também exige isolamento, inclusive quando não existe arquivo.
        mvc.perform(get("/v1/empresas/{id}/logo", empresaB)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        mvc.perform(get("/v1/empresas/{id}/logo", empresaA)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());

        mvc.perform(get("/v1/empresas").header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(empresaA));
        String cnpjB = jdbc.queryForObject("select cnpj from empresa where id=?", String.class, empresaB);
        mvc.perform(get("/v1/empresas").param("cnpj", cnpjB)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(0));

        String payload = json.writeValueAsString(Map.of(
                "nomeFantasia", "ALTERACAO-CRUZADA",
                "razaoSocial", "ALTERACAO-CRUZADA",
                "cnpj", "12345678909"
        ));
        mvc.perform(put("/v1/empresas/{id}", empresaB)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/v1/empresas/{id}", empresaB)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        mvc.perform(multipart("/v1/empresas/{id}/logo", empresaB)
                .file(new MockMultipartFile("file", "logo.png", "image/png", new byte[]{1, 2, 3}))
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        mvc.perform(put("/v1/empresas/{id}", empresaA)
                .header("Authorization", "Bearer " + viewerA)
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isForbidden());

        // O cadastro de outras empresas não pode ser liberado por uma ROLE_ADMIN local.
        mvc.perform(post("/v1/empresas").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isForbidden());

        assertThat(jdbc.queryForObject("select count(*) from empresa where id=?", Integer.class, empresaB)).isEqualTo(1);
        assertThat(jdbc.queryForObject("select nome_fantasia from empresa where id=?", String.class, empresaB)).isEqualTo("Empresa B");
        assertThat(jdbc.queryForObject("select logo_path from empresa where id=?", String.class, empresaB)).isNull();
        assertThat(jdbc.queryForObject("select count(*) from empresa where id=?", Integer.class, empresaA)).isEqualTo(1);
    }

    @Test
    void configuracoesDaEmpresaNaoVazamEntreTenants() throws Exception {
        long empresaA = empresa("A");
        long empresaB = empresa("B");
        String tokenA = login(empresaA, "a" + empresaA + "@t.co", "ADMIN");

        for (String base : new String[] {
                "/v1/enderecos-empresa", "/v1/configuracoes-empresa",
                "/v1/configuracoes-fiscais", "/v1/configuracoes-oficina"
        }) {
            mvc.perform(get(base + "/empresa/{id}", empresaB)
                    .header("Authorization", "Bearer " + tokenA))
                    .andExpect(status().isForbidden());
            mvc.perform(get(base).header("Authorization", "Bearer " + tokenA))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.totalElements").value(0));
        }
    }

    @Test
    void logoInvalidaNaoEPersistidaEExclusaoFisicaDaEmpresaEProibida() throws Exception {
        long empresaA = empresa("A");
        String tokenA = login(empresaA, "a" + empresaA + "@t.co", "ADMIN");

        mvc.perform(multipart("/v1/empresas/{id}/logo", empresaA)
                .file(new MockMultipartFile("file", "logo.svg", "image/svg+xml",
                        "<svg onload='alert(1)'/>".getBytes(java.nio.charset.StandardCharsets.UTF_8)))
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isUnprocessableEntity());
        mvc.perform(multipart("/v1/empresas/{id}/logo", empresaA)
                .file(new MockMultipartFile("file", "imagem.png", "image/png", new byte[]{1, 2, 3}))
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isUnprocessableEntity());

        mvc.perform(delete("/v1/empresas/{id}", empresaA)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        assertThat(jdbc.queryForObject("select logo_path from empresa where id=?", String.class, empresaA)).isNull();
        assertThat(jdbc.queryForObject("select count(*) from empresa where id=?", Integer.class, empresaA)).isEqualTo(1);
    }

    @Test
    void rejeitaVersaoObsoletaEIgnoraLogoPathForjado() throws Exception {
        long empresaA = empresa("A");
        String tokenA = login(empresaA, "a" + empresaA + "@t.co", "ADMIN");
        int versao = jdbc.queryForObject("select versao from empresa where id=?", Integer.class, empresaA);
        String atualizacao = json.writeValueAsString(Map.of(
                "nomeFantasia", "Oficina segura", "razaoSocial", "Empresa segura",
                "cnpj", "52998224725", "versao", versao,
                "logoPath", "/etc/passwd"));
        mvc.perform(put("/v1/empresas/{id}", empresaA)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(atualizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.logoPath").doesNotExist());
        assertThat(jdbc.queryForObject("select logo_path from empresa where id=?", String.class, empresaA)).isNull();

        mvc.perform(put("/v1/empresas/{id}", empresaA)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(atualizacao))
                .andExpect(status().isConflict());
    }

    private long empresa(String suffix) {
        String document = suffix + "-" + Long.toString(System.nanoTime(), 36);
        return jdbc.queryForObject(
                "insert into empresa (cnpj, nome_fantasia, razao_social, ativo, data_cadastro) " +
                "values (?, ?, ?, true, now()) returning id",
                Long.class, document, "Empresa " + suffix, "Empresa " + suffix);
    }

    private String login(long empresa, String email, String role) throws Exception {
        var funcao = funcoes.save(TestDataBuilder.umaFuncao()
                .comNome(role).comEmpresaId(empresa).build());
        usuarios.save(TestDataBuilder.umUsuario()
                .comEmpresaId(empresa)
                .comEmail(email)
                .comSenha(encoder.encode("senha123"))
                .comFuncao(funcao).build());
        var response = mvc.perform(post("/auth/login")
                .header("X-Tenant-Id", Long.toString(empresa))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email", email, "senha", "senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString()).get("accessToken").asText();
    }
}
