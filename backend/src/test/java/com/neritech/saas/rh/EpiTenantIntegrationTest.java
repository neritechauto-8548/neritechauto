package com.neritech.saas.rh;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@org.springframework.test.context.jdbc.Sql(
    scripts = "/db/migration/V293__rh_epis_catalogo_entregas.sql",
    executionPhase = org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EpiTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void catalogoEntregasEIsolamentoEntreEmpresas() throws Exception {
        long a = empresa("A"), b = empresa("B");
        long funcionarioA = funcionario(a, "A"), funcionarioB = funcionario(b, "B");
        String tokenA = login(a, "epi" + a + "@t.co", "ADMIN");
        String tokenB = login(b, "epi" + b + "@t.co", "ADMIN");
        String viewerA = login(a, "viewepi" + a + "@t.co", "LEITOR");
        String body = json.writeValueAsString(Map.of("nome", "Luva anticorte", "numeroCa", "CA-123",
                "categoria", "Proteção das mãos", "fabricante", "Fornecedor de teste"));
        var created = mvc.perform(post("/v1/rh/epis")
                .header("Authorization", "Bearer " + tokenA).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andReturn();
        long epiId = json.readTree(created.getResponse().getContentAsString()).path("data").path("id").asLong();
        assertThat(epiId).isPositive();
        mvc.perform(post("/v1/rh/epis").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isConflict());
        mvc.perform(get("/v1/rh/epis").header("Authorization", "Bearer " + tokenA)
                .param("page", "0").param("size", "20")).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(1));
        mvc.perform(get("/v1/rh/epis").header("Authorization", "Bearer " + tokenB)
                .param("page", "0").param("size", "20")).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(0));
        mvc.perform(get("/v1/rh/epis/{id}", epiId)
                .header("Authorization", "Bearer " + tokenB)).andExpect(status().isNotFound());
        mvc.perform(get("/v1/rh/epis").header("Authorization", "Bearer " + viewerA))
                .andExpect(status().isForbidden());

        String entrega = json.writeValueAsString(Map.of("epiId", epiId,
                "dataEntrega", "2026-10-28", "dataValidade", "2027-10-28"));
        mvc.perform(post("/v1/rh/funcionarios/{id}/epis", funcionarioB)
                .header("Authorization", "Bearer " + tokenA).contentType(MediaType.APPLICATION_JSON)
                .content(entrega)).andExpect(status().isNotFound());
        mvc.perform(post("/v1/rh/funcionarios/{id}/epis", funcionarioA)
                .header("Authorization", "Bearer " + tokenB).contentType(MediaType.APPLICATION_JSON)
                .content(entrega)).andExpect(status().isNotFound());
        mvc.perform(post("/v1/rh/funcionarios/{id}/epis", funcionarioA)
                .header("Authorization", "Bearer " + tokenA).contentType(MediaType.APPLICATION_JSON)
                .content(entrega)).andExpect(status().isCreated());

        mvc.perform(delete("/v1/rh/epis/{id}", epiId)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNoContent());
        mvc.perform(get("/v1/rh/funcionarios/{id}/epis", funcionarioA)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].epiId").value(epiId));
        mvc.perform(get("/v1/rh/funcionarios/{id}/epis", funcionarioA)
                .header("Authorization", "Bearer " + tokenB)).andExpect(status().isNotFound());
        assertThat(jdbc.queryForObject("select count(*) from rh_epi_entregas where empresa_id=? and epi_id=?", Integer.class, a, epiId))
                .isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from logs_alteracoes where empresa_id=? and contexto_operacao='ADM-004-EPI'", Integer.class, a))
                .isGreaterThanOrEqualTo(3);
    }

    private long empresa(String suffix) {
        return jdbc.queryForObject("insert into empresa (cnpj,nome_fantasia,ativo,data_cadastro) values (?,?,true,now()) returning id",
                Long.class, suffix + Long.toString(System.nanoTime(),36), "EPI " + suffix);
    }
    private long funcionario(long empresa, String suffix) {
        return jdbc.queryForObject(
                "insert into funcionarios (empresa_id,matricula,nome_completo,data_admissao,status,data_cadastro,versao) values (?,?,?,current_date,'ATIVO',now(),0) returning id",
                Long.class, empresa, "EPI" + suffix + empresa, "Funcionário " + suffix);
    }
    private String login(long empresa, String email, String role) throws Exception {
        var funcao = funcoes.save(TestDataBuilder.umaFuncao().comEmpresaId(empresa).comNome(role).build());
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa).comEmail(email).comSenha(encoder.encode("senha123")).comFuncao(funcao).build());
        var response = mvc.perform(post("/auth/login").header("X-Tenant-Id", Long.toString(empresa))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email",email,"senha","senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString()).get("accessToken").asText();
    }
}
