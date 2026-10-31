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
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.test.context.jdbc.Sql(scripts = "/sql/adm009_010_test_schema.sql")
@AutoConfigureMockMvc
class SeguradoraTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void identificaDuplicatasValidaContatoEIsolaEmpresa() throws Exception {
        long a = company("A");
        long b = company("B");
        String tokenA = login(a, "insuranceA" + a + "@t.invalid", "ADMIN");
        String tokenB = login(b, "insuranceB" + b + "@t.invalid", "ADMIN");
        String viewer = login(a, "insuranceView" + a + "@t.invalid", "SEM_ACESSO");

        mvc.perform(post("/v1/seguradoras").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(request("11222333000181")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.razaoSocial").value("Seguradora Teste Ltda"))
                .andExpect(jsonPath("$.data.retencaoIss").value(true));
        long id = jdbc.queryForObject("SELECT id FROM seguradoras WHERE empresa_id=?", Long.class, a);

        mvc.perform(post("/v1/seguradoras").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(request("11.222.333/0001-81")))
                .andExpect(status().isConflict());
        mvc.perform(post("/v1/seguradoras").header("Authorization", "Bearer " + tokenB)
                .contentType(MediaType.APPLICATION_JSON).content(request("11222333000181")))
                .andExpect(status().isCreated());
        mvc.perform(post("/v1/seguradoras").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(request("00000000000000")))
                .andExpect(status().isUnprocessableEntity());
        mvc.perform(post("/v1/seguradoras").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(
                    json.writeValueAsString(Map.of("cnpj", "11222333000181",
                        "razaoSocial", "Teste", "email", "invalido"))))
                .andExpect(status().is4xxClientError());

        assertThat(jdbc.queryForObject("""
                SELECT retencao_iss FROM seguradoras WHERE empresa_id=? AND id=?
                """, Boolean.class, a, id)).isTrue();
        assertThat(jdbc.queryForObject("""
                SELECT COUNT(*) FROM seguradoras WHERE empresa_id=? AND cnpj=?
                """, Long.class, a, "11222333000181")).isEqualTo(1L);
        mvc.perform(get("/v1/seguradoras")
                .header("Authorization", "Bearer " + tokenA)
                .param("nome", "Seguradora Teste Ltda")
                .param("nomeFantasia", "Seguradora Teste")
                .param("cnpj", "11.222.333/0001-81")
                .param("ativo", "true")).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].razaoSocial").value("Seguradora Teste Ltda"));

        mvc.perform(get("/v1/seguradoras/{id}", id).header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isForbidden());
        mvc.perform(put("/v1/seguradoras/{id}", id)
                .header("Authorization", "Bearer " + tokenB)
                .contentType(MediaType.APPLICATION_JSON).content(request("11222333000181")))
                .andExpect(status().isForbidden());
        mvc.perform(get("/v1/seguradoras").header("Authorization", "Bearer " + viewer))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/v1/seguradoras/{id}", id).header("Authorization", "Bearer " + viewer))
                .andExpect(status().isForbidden());
        mvc.perform(put("/v1/seguradoras/{id}", id).header("Authorization", "Bearer " + tokenA)
                .header("If-Match", "-1")
                .contentType(MediaType.APPLICATION_JSON).content(request("11222333000181")))
                .andExpect(status().isConflict());

        assertThat(jdbc.queryForObject("""
                SELECT COUNT(*) FROM logs_alteracoes
                 WHERE empresa_id=? AND contexto_operacao='ADM-009' AND registro_id=?
                """, Long.class, a, id)).isGreaterThan(0L);
    }

    @Test
    void vinculoRealInativacaoEProtecaoEntreEmpresas() throws Exception {
        long a = company("A");
        long b = company("B");
        String tokenA = login(a, "insOSa" + a + "@t.invalid", "ADMIN");
        String tokenB = login(b, "insOSb" + b + "@t.invalid", "ADMIN");
        mvc.perform(post("/v1/seguradoras").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(request("11222333000181")))
                .andExpect(status().isCreated());
        long id = jdbc.queryForObject("SELECT id FROM seguradoras WHERE empresa_id=?", Long.class, a);
        long osA = ordem(a);
        long osB = ordem(b);

        mvc.perform(post("/v1/seguradoras/{id}/os/{osId}", id, osB).param("tipo", "CONVENIO")
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        mvc.perform(post("/v1/seguradoras/{id}/os/{osId}", id, osA).param("tipo", "CONVENIO")
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isCreated());
        mvc.perform(post("/v1/seguradoras/{id}/os/{osId}", id, osA).param("tipo", "CONVENIO")
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isConflict());
        mvc.perform(get("/v1/seguradoras/os/{id}", osA).header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isForbidden());

        mvc.perform(delete("/v1/seguradoras/{id}", id).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNoContent());
        assertThat(jdbc.queryForObject("SELECT ativo FROM seguradoras WHERE id=?", Boolean.class, id)).isFalse();
        assertThat(jdbc.queryForObject("SELECT count(*) FROM seguradoras_os WHERE seguradora_id=?",
                Long.class, id)).isEqualTo(1L);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM ordens_servico WHERE id=?",
                Long.class, osA)).isEqualTo(1L);
        mvc.perform(post("/v1/seguradoras/{id}/os/{osId}", id, ordem(a)).param("tipo", "REEMBOLSO")
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isConflict());
        mvc.perform(get("/v1/seguradoras/{id}/historico", id)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk());
    }

    private String request(String cnpj) throws Exception {
        return json.writeValueAsString(Map.of(
                "cnpj", cnpj, "razaoSocial", "Seguradora Teste Ltda",
                "nomeFantasia", "Seguradora Teste",
                "inscricaoEstadual", "123456789",
                "inscricaoMunicipal", "987654321",
                "email", "contato@teste.com",
                "observacoes", "Cadastro de referência",
                "margemRefatoramento", 12.5,
                "site", "https://seguradora.example.com",
                "retencaoIss", true));
    }

    private long company(String suffix) {
        return jdbc.queryForObject("""
          INSERT INTO empresa (cnpj, nome_fantasia, razao_social, ativo, data_cadastro)
          VALUES (?, ?, ?, true, now()) RETURNING id
          """, Long.class, suffix + Long.toString(System.nanoTime(), 36),
            "Empresa " + suffix, "Empresa " + suffix);
    }

    private long ordem(long empresa) {
        return jdbc.queryForObject("""
          INSERT INTO ordens_servico (empresa_id, numero_os, tipo_os, valor_total, data_cadastro)
          VALUES (?, ?, 'MANUTENCAO', 10, now()) RETURNING id
          """, Long.class, empresa, "OS-" + Long.toString(System.nanoTime(), 36));
    }

    private String login(long empresa, String email, String role) throws Exception {
        var funcao = funcoes.save(TestDataBuilder.umaFuncao()
            .comNome(role).comEmpresaId(empresa).build());
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa)
            .comEmail(email).comSenha(encoder.encode("senha123")).comFuncao(funcao).build());
        var response = mvc.perform(post("/auth/login").header("X-Tenant-Id", Long.toString(empresa))
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(Map.of("email", email, "senha", "senha123"))))
            .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString()).get("accessToken").asText();
    }
}
