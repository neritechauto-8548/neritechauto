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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class FuncionarioTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void jwtDaEmpresaAProtegeLeituraMutacoesFotosEFksDaEmpresaB() throws Exception {
        long a = empresa("A");
        long b = empresa("B");
        long funcionarioA = funcionario(a, "A");
        long funcionarioB = funcionario(b, "B");
        long cargoB = jdbc.queryForObject(
                "insert into cargos (empresa_id, nome, data_cadastro, versao) values (?, 'Cargo B', now(), 0) returning id",
                Long.class, b);
        long departamentoB = jdbc.queryForObject(
                "insert into departamentos_contabio (empresa_id, descricao, data_cadastro, versao) values (?, 'Depto B', now(), 0) returning id",
                Long.class, b);
        String tokenA = login(a, "a" + a + "@t.co", "ADMIN");
        String tokenB = login(b, "b" + b + "@t.co", "ADMIN");
        String viewerA = login(a, "v" + a + "@t.co", "LEITOR");
        long usuarioB = usuarios.findByEmailIgnoreCase("b" + b + "@t.co").orElseThrow().getId();

        mvc.perform(get("/v1/rh/funcionarios/{id}", funcionarioA)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isOk());
        mvc.perform(get("/v1/rh/funcionarios/{id}", funcionarioB)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNotFound());
        mvc.perform(get("/v1/rh/funcionarios/{id}", funcionarioB)
                .header("Authorization", "Bearer " + tokenB)).andExpect(status().isOk());
        mvc.perform(get("/v1/rh/funcionarios/{id}", funcionarioA)
                .header("Authorization", "Bearer " + viewerA)).andExpect(status().isForbidden());
        mvc.perform(get("/v1/rh/funcionarios/{id}", Long.MAX_VALUE)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNotFound());

        mvc.perform(get("/v1/rh/funcionarios").param("empresaId", Long.toString(a))
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(funcionarioA));
        mvc.perform(get("/v1/rh/funcionarios").param("empresaId", Long.toString(b))
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isForbidden());
        mvc.perform(get("/v1/rh/funcionarios/usuario/{id}", usuarioB)
                .param("empresaId", Long.toString(a))
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNotFound());

        mvc.perform(put("/v1/rh/funcionarios/{id}", funcionarioB)
                .header("Authorization", "Bearer " + tokenA).contentType(MediaType.APPLICATION_JSON)
                .content(payload(a, "ALTERADO", null, null, null))).andExpect(status().isNotFound());
        mvc.perform(delete("/v1/rh/funcionarios/{id}", funcionarioB)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNotFound());
        mvc.perform(multipart("/v1/rh/funcionarios/{id}/foto", funcionarioB)
                .file(new MockMultipartFile("file", "foto.png", "image/png", new byte[]{1, 2, 3}))
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNotFound());
        mvc.perform(get("/v1/rh/funcionarios/{id}/foto", funcionarioB)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNotFound());
        mvc.perform(post("/v1/rh/funcionarios").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(b, "FORJADO", null, null, null)))
                .andExpect(status().isForbidden());

        mvc.perform(post("/v1/rh/funcionarios").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a, "FK-CARGO", cargoB, null, null)))
                .andExpect(status().isNotFound());
        mvc.perform(post("/v1/rh/funcionarios").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a, "FK-DEP", null, departamentoB, null)))
                .andExpect(status().isNotFound());
        mvc.perform(post("/v1/rh/funcionarios").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a, "FK-USUARIO", null, null, usuarioB)))
                .andExpect(status().isNotFound());

        assertThat(jdbc.queryForObject("select nome_completo from funcionarios where id=?", String.class, funcionarioB))
                .isEqualTo("Funcionário B");
        assertThat(jdbc.queryForObject("select count(*) from funcionarios where empresa_id=?", Integer.class, b)).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from funcionarios where empresa_id=?", Integer.class, a)).isEqualTo(1);

        mvc.perform(put("/v1/rh/funcionarios/{id}", funcionarioA)
                .header("Authorization", "Bearer " + tokenA).contentType(MediaType.APPLICATION_JSON)
                .content(payload(a, "EDITADO-A", null, null, null))).andExpect(status().isOk());
        assertThat(jdbc.queryForObject("select matricula from funcionarios where id=?", String.class, funcionarioA))
                .isEqualTo("EDITADO-A");
        mvc.perform(delete("/v1/rh/funcionarios/{id}", funcionarioA)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNoContent());
        assertThat(jdbc.queryForObject("select count(*) from funcionarios where id=?", Integer.class, funcionarioA)).isZero();
        assertThat(jdbc.queryForObject("select count(*) from funcionarios where id=?", Integer.class, funcionarioB)).isEqualTo(1);
    }

    private long empresa(String suffix) {
        return jdbc.queryForObject("insert into empresa (cnpj, nome_fantasia, ativo, data_cadastro) values (?, ?, true, now()) returning id",
                Long.class, suffix + Long.toString(System.nanoTime(), 36), "Teste " + suffix);
    }
    private long funcionario(long empresa, String suffix) {
        return jdbc.queryForObject(
                "insert into funcionarios (empresa_id, matricula, nome_completo, data_admissao, data_cadastro, versao) values (?, ?, ?, CURRENT_DATE, now(), 0) returning id",
                Long.class, empresa, "MAT" + suffix + empresa, "Funcionário " + suffix);
    }
    private String payload(long empresa, String matricula, Long cargo, Long departamento, Long usuario) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("empresaId", empresa);
        body.put("matricula", matricula);
        body.put("nomeCompleto", "Tentativa interempresa");
        body.put("cpf", "52998224725");
        body.put("dataAdmissao", "2026-01-01");
        body.put("cep", "50000000");
        body.put("logradouro", "Rua Teste");
        body.put("bairro", "Centro");
        body.put("cidade", "Recife");
        body.put("estado", "PE");
        if (cargo != null) body.put("cargoId", cargo);
        if (departamento != null) body.put("departamentoId", departamento);
        if (usuario != null) body.put("usuarioId", usuario);
        return json.writeValueAsString(body);
    }
    private String login(long empresa, String email, String role) throws Exception {
        var funcao = funcoes.save(TestDataBuilder.umaFuncao().comNome(role).comEmpresaId(empresa).build());
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa).comEmail(email)
                .comSenha(encoder.encode("senha123")).comFuncao(funcao).build());
        var response = mvc.perform(post("/auth/login").header("X-Tenant-Id", Long.toString(empresa))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email", email, "senha", "senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString()).get("accessToken").asText();
    }
}
