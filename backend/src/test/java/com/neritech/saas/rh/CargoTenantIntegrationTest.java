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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@org.springframework.test.context.jdbc.Sql(scripts="/db/migration/V297__tipo_cargos_referencia.sql",
    executionPhase=org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CargoTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void cargoTemUnicidadeConcorrenciaSoftDeleteAuditoriaETenant() throws Exception {
        long a = empresa("A"); long b = empresa("B");
        String tokenA = login(a, "admin" + a + "@t.co", "ADMIN");
        String tokenB = login(b, "admin" + b + "@t.co", "ADMIN");
        String viewer = login(a, "viewer" + a + "@t.co", "LEITOR");

        var created = mvc.perform(post("/v1/rh/cargos").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a, "Técnico", "TEC", true, null)))
                .andExpect(status().isCreated()).andReturn();
        long cargoA = json.readTree(created.getResponse().getContentAsString()).path("data").path("id").asLong();
        int version = json.readTree(created.getResponse().getContentAsString()).path("data").path("versao").asInt();
        long cargoB = jdbc.queryForObject("insert into cargos (empresa_id,nome,codigo_cbo,ativo,data_cadastro,versao) values (?,'Técnico','TEC',true,now(),0) returning id", Long.class, b);

        mvc.perform(get("/v1/rh/cargos/{id}", cargoB).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());
        mvc.perform(get("/v1/rh/cargos/{id}", cargoB).header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk());
        mvc.perform(get("/v1/rh/cargos").param("empresaId", Long.toString(b))
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isForbidden());
        mvc.perform(get("/v1/rh/cargos").param("empresaId", Long.toString(a))
                .header("Authorization", "Bearer " + viewer)).andExpect(status().isForbidden());

        mvc.perform(post("/v1/rh/cargos").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a, " TÉCNICO ", "OUT", true, null)))
                .andExpect(status().isConflict());
        mvc.perform(put("/v1/rh/cargos/{id}", cargoA).header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a, "Técnico Sênior", "TEC", true, version)))
                .andExpect(status().isOk());
        mvc.perform(put("/v1/rh/cargos/{id}", cargoA).header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a, "Obsoleto", "TEC", true, version)))
                .andExpect(status().isConflict());

        jdbc.update("insert into funcionarios (empresa_id,cargo_id,matricula,nome_completo,data_admissao,status,data_cadastro,versao) values (?,?,?,'Funcionário',current_date,'ATIVO',now(),0)", a, cargoA, "MAT" + a);
        mvc.perform(delete("/v1/rh/cargos/{id}", cargoA).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNoContent());
        assertThat(jdbc.queryForObject("select ativo from cargos where id=?", Boolean.class, cargoA)).isFalse();
        assertThat(jdbc.queryForObject("select count(*) from funcionarios where cargo_id=?", Integer.class, cargoA)).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from logs_alteracoes where empresa_id=? and tabela_afetada='cargos' and contexto_operacao='ADM-005'", Integer.class, a)).isGreaterThanOrEqualTo(3);
    }

    private String payload(long empresa, String nome, String codigo, boolean ativo, Integer versao) throws Exception {
        Map<String,Object> body = new HashMap<>(); body.put("empresaId", empresa); body.put("nome", nome);
        body.put("codigoCbo", codigo); body.put("temComissao", false); body.put("ativo", ativo);
        if (versao != null) body.put("versao", versao);
        return json.writeValueAsString(body);
    }
    private long empresa(String suffix) {
        return jdbc.queryForObject("insert into empresa (cnpj,nome_fantasia,ativo,data_cadastro) values (?,?,true,now()) returning id", Long.class, suffix + Long.toString(System.nanoTime(),36), "Empresa " + suffix);
    }
    private String login(long empresa, String email, String role) throws Exception {
        var funcao = funcoes.save(TestDataBuilder.umaFuncao().comEmpresaId(empresa).comNome(role).build());
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa).comEmail(email).comSenha(encoder.encode("senha123")).comFuncao(funcao).build());
        var response = mvc.perform(post("/auth/login").header("X-Tenant-Id", Long.toString(empresa))
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(Map.of("email",email,"senha","senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString()).get("accessToken").asText();
    }
}
