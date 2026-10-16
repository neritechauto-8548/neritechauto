package com.neritech.saas.ordemservico;

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

@AutoConfigureMockMvc
class OrcamentoHttpContractIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void contratoV1ListarCriarEditarETratarHttp401403422409() throws Exception {
        long empresaA = empresa("A");
        long empresaB = empresa("B");
        long clienteA = jdbc.queryForObject(
                "insert into cliente (empresa_id, tipo_cliente, nome_completo, data_cadastro) " +
                "values (?, 'PESSOA_FISICA', 'Cliente orçamento', now()) returning id",
                Long.class, empresaA);
        long osA = jdbc.queryForObject(
                "insert into ordens_servico (empresa_id, numero_os, cliente_id, tipo_os, valor_total, data_cadastro) " +
                "values (?, ?, ?, 'ORCAMENTO', 10, now()) returning id",
                Long.class, empresaA, "OS-" + Long.toString(System.nanoTime(), 36), clienteA);
        String numero = "ORC-" + Long.toString(System.nanoTime(), 36);
        String tokenA = login(empresaA, "orc-a-" + empresaA + "@t.co");

        mvc.perform(get("/v1/orcamentos/ordem-servico/{id}", osA))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/v1/orcamentos/ordem-servico/{id}", osA)
                .header("Authorization", "Bearer " + tokenA)
                .header("X-Tenant-Id", Long.toString(empresaB)))
                .andExpect(status().isForbidden());
        mvc.perform(get("/v1/orcamentos/ordem-servico/{id}", osA)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0));

        mvc.perform(post("/v1/orcamentos")
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("ordemServicoId", osA))))
                .andExpect(status().isUnprocessableEntity());

        String payload = json.writeValueAsString(Map.of(
                "ordemServicoId", osA,
                "numeroOrcamento", numero,
                "tipoOrcamento", "SIMPLES",
                "status", "RASCUNHO",
                "metodoEnvio", "PRESENCIAL",
                "valorTotal", 75));
        var created = mvc.perform(post("/v1/orcamentos")
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.ordemServicoId").value(osA))
                .andExpect(jsonPath("$.data.tipoOrcamento").value("SIMPLES"))
                .andExpect(jsonPath("$.data.status").value("RASCUNHO"))
                .andExpect(jsonPath("$.data.metodoEnvio").value("PRESENCIAL"))
                .andReturn();
        long id = json.readTree(created.getResponse().getContentAsString()).path("data").path("id").asLong();

        mvc.perform(get("/v1/orcamentos/ordem-servico/{id}", osA)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(id));
        mvc.perform(get("/v1/orcamentos/{id}", id)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.numeroOrcamento").value(numero));

        String updated = json.writeValueAsString(Map.of(
                "ordemServicoId", osA,
                "numeroOrcamento", numero,
                "tipoOrcamento", "DETALHADO",
                "status", "ENVIADO",
                "valorTotal", 90));
        mvc.perform(put("/v1/orcamentos/{id}", id)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tipoOrcamento").value("DETALHADO"))
                .andExpect(jsonPath("$.data.valorTotal").value(90));

        mvc.perform(post("/v1/orcamentos")
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isConflict());

        assertThat(jdbc.queryForObject(
                "select count(*) from orcamentos where numero_orcamento=?",
                Integer.class, numero)).isEqualTo(1);
    }

    private long empresa(String suffix) {
        return jdbc.queryForObject(
                "insert into empresa (cnpj, nome_fantasia, razao_social, ativo, data_cadastro) " +
                "values (?, ?, ?, true, now()) returning id",
                Long.class, suffix + "-" + Long.toString(System.nanoTime(), 36),
                "Empresa " + suffix, "Empresa " + suffix);
    }

    private String login(long empresa, String email) throws Exception {
        var funcao = funcoes.save(TestDataBuilder.umaFuncao()
                .comNome("ADMIN").comEmpresaId(empresa).build());
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa)
                .comEmail(email).comSenha(encoder.encode("senha123")).comFuncao(funcao).build());
        var result = mvc.perform(post("/auth/login")
                .header("X-Tenant-Id", Long.toString(empresa))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email", email, "senha", "senha123"))))
                .andExpect(status().isOk())
                .andReturn();
        return json.readTree(result.getResponse().getContentAsString()).path("accessToken").asText();
    }
}
