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

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class OrdemServicoTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void jwtDaEmpresaAIsolaLeituraEdicaoExclusaoEReferencias() throws Exception {
        long a = company("A");
        long b = company("B");
        long clienteA = cliente(a);
        long clienteB = cliente(b);
        long veiculoB = veiculo(b, clienteB);
        long osB = os(b, clienteB, veiculoB);
        long osA = os(a, clienteA, null);
        long itemB = jdbc.queryForObject("insert into itens_os_servicos (ordem_servico_id, valor_unitario, valor_total, valor_final, data_cadastro) values (?, 10, 10, 10, now()) returning id", Long.class, osB);
        String tokenA = login(a, "a-os@test.invalid");
        String tokenB = login(b, "b-os@test.invalid");
        String viewer = loginWithoutCapability(a, "viewer-os@test.invalid");

        mvc.perform(get("/v1/ordens-servico/{id}", osA).header("Authorization", "Bearer " + viewer))
                .andExpect(status().isForbidden());
        mvc.perform(get("/v1/ordens-servico/empresa/{id}", b).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        mvc.perform(post("/v1/ordens-servico").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(b, clienteB, veiculoB, "BAD-TENANT")))
                .andExpect(status().isForbidden());

        mvc.perform(get("/v1/ordens-servico/{id}", osB).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());
        mvc.perform(get("/public/ordem-servico/{id}", osB).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());
        mvc.perform(get("/public/ordem-servico/{id}", osB))
                .andExpect(status().isForbidden());
        mvc.perform(put("/v1/ordens-servico/{id}", osB).header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a, clienteA, null, "CROSS")))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/v1/ordens-servico/{id}", osB).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());
        mvc.perform(get("/v1/itens-os-servicos/{id}", itemB).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/v1/itens-os-servicos/{id}", itemB).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());
        mvc.perform(get("/v1/ordens-servico/cliente/{id}", clienteB).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.totalElements").value(0));
        mvc.perform(post("/v1/ordens-servico").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a, clienteB, veiculoB, "BAD-CREATE")))
                .andExpect(status().isNotFound());
        mvc.perform(post("/v1/ordens-servico").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a, clienteA, veiculoB, "BAD-VEHICLE")))
                .andExpect(status().isNotFound());
        mvc.perform(put("/v1/ordens-servico/{id}", osA).header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a, clienteB, veiculoB, "BAD-UPDATE")))
                .andExpect(status().isNotFound());
        assertThat(jdbc.queryForObject("select numero_os from ordens_servico where id=?", String.class, osA)).isNotEqualTo("BAD-UPDATE");
        assertThat(jdbc.queryForObject("select numero_os from ordens_servico where id=?", String.class, osB)).isNotEqualTo("CROSS");

        mvc.perform(get("/v1/ordens-servico/{id}", osB).header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.id").value(osB));
        mvc.perform(put("/v1/ordens-servico/{id}", osB).header("Authorization", "Bearer " + tokenB)
                .contentType(MediaType.APPLICATION_JSON).content(payload(b, clienteB, veiculoB, "OWN-UPDATE")))
                .andExpect(status().isOk());
        assertThat(jdbc.queryForObject("select numero_os from ordens_servico where id=?", String.class, osB)).isEqualTo("OWN-UPDATE");
        mvc.perform(delete("/v1/itens-os-servicos/{id}", itemB).header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isNoContent());
        mvc.perform(delete("/v1/ordens-servico/{id}", osB).header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isNoContent());
        assertThat(jdbc.queryForObject("select count(*) from ordens_servico where id=?", Integer.class, osB)).isZero();
    }

    private long company(String suffix) {
        return jdbc.queryForObject("insert into empresa (cnpj, ativo, data_cadastro) values (?, true, now()) returning id", Long.class,
                suffix + Long.toString(System.nanoTime(), 36));
    }
    private long cliente(long empresa) {
        return jdbc.queryForObject("insert into cliente (empresa_id, tipo_cliente, nome_completo, data_cadastro) values (?, 'PESSOA_FISICA', 'Cliente teste', now()) returning id", Long.class, empresa);
    }
    private long veiculo(long empresa, long cliente) {
        return jdbc.queryForObject("insert into veiculos (empresa_id, cliente_id, placa, data_cadastro) values (?, ?, ?, now()) returning id", Long.class,
                empresa, cliente, "T" + Long.toString(System.nanoTime(), 36).substring(0, 6));
    }
    private long os(long empresa, long cliente, Long veiculo) {
        return jdbc.queryForObject("insert into ordens_servico (empresa_id, numero_os, cliente_id, veiculo_id, tipo_os, valor_total, data_cadastro) values (?, ?, ?, ?, 'MANUTENCAO', 10, now()) returning id", Long.class,
                empresa, "OS-" + Long.toString(System.nanoTime(), 36), cliente, veiculo);
    }
    private String login(long empresa, String email) throws Exception {
        return loginWithRole(empresa, email, "ADMIN");
    }
    private String loginWithoutCapability(long empresa, String email) throws Exception {
        return loginWithRole(empresa, email, "OS_SEM_PERMISSAO");
    }
    private String loginWithRole(long empresa, String email, String role) throws Exception {
        var admin = funcoes.save(TestDataBuilder.umaFuncao().comNome(role).comEmpresaId(empresa).build());
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa).comEmail(email)
                .comSenha(encoder.encode("senha123")).comFuncao(admin).build());
        var response = mvc.perform(post("/auth/login").header("X-Tenant-Id", Long.toString(empresa))
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(Map.of("email", email, "senha", "senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString()).get("accessToken").asText();
    }
    private String payload(long empresa, long cliente, Long veiculo, String numero) throws Exception {
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("empresaId", empresa);
        body.put("numeroOS", numero);
        body.put("clienteId", cliente);
        body.put("tipoOS", "MANUTENCAO");
        body.put("valorTotal", BigDecimal.TEN);
        if (veiculo != null) body.put("veiculoId", veiculo);
        return json.writeValueAsString(body);
    }
}
