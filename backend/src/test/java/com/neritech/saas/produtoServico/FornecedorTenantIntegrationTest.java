package com.neritech.saas.produtoServico;

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
class FornecedorTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void documentosDuplicadosOuInvalidosSaoBloqueadosPorTenant() throws Exception {
        long a = empresa("A");
        long b = empresa("B");
        String tokenA = login(a, "a" + a + "@fornecedor.t", "ADMIN");
        String tokenB = login(b, "b" + b + "@fornecedor.t", "ADMIN");

        mvc.perform(post("/v1/fornecedores").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(supplier(a, "11222333000181")))
                .andExpect(status().isCreated());
        mvc.perform(post("/v1/fornecedores").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(supplier(a, "11.222.333/0001-81")))
                .andExpect(status().isConflict());
        mvc.perform(post("/v1/fornecedores").header("Authorization", "Bearer " + tokenB)
                .contentType(MediaType.APPLICATION_JSON).content(supplier(b, "11222333000181")))
                .andExpect(status().isCreated());
        mvc.perform(post("/v1/fornecedores").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(supplier(b, "52998224725")))
                .andExpect(status().isForbidden());
        mvc.perform(post("/v1/fornecedores").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(supplier(a, "00000000000000")))
                .andExpect(status().isUnprocessableEntity());
        String cepRuim = json.writeValueAsString(Map.of("empresaId", a,
                "tipoPessoa", "JURIDICA", "nomeFantasia", "Fornecedor", "cnpj", "11222333000181",
                "cep", "000"));
        mvc.perform(post("/v1/fornecedores").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(cepRuim))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void inativacaoPreservaPedidoHistoricoEImpedeNovaCompra() throws Exception {
        long a = empresa("A");
        long b = empresa("B");
        String tokenA = login(a, "purchase" + a + "@fornecedor.t", "ADMIN");
        String tokenB = login(b, "purchase" + b + "@fornecedor.t", "ADMIN");
        mvc.perform(post("/v1/fornecedores").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(supplier(a, "11222333000181")))
                .andExpect(status().isCreated());
        long supplier = jdbc.queryForObject(
                "SELECT id FROM fornecedores WHERE empresa_id=? AND cnpj=?", Long.class, a, "11222333000181");
        String order = json.writeValueAsString(Map.of(
                "empresaId", a, "fornecedorId", supplier, "responsavel", "Comprador"));
        mvc.perform(post("/v1/pedidos-fornecedor").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(order))
                .andExpect(status().isCreated());

        mvc.perform(get("/v1/fornecedores/{id}", supplier).header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/v1/fornecedores/{id}", supplier).header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isForbidden());
        mvc.perform(get("/v1/fornecedores").param("empresaId", Long.toString(b))
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isForbidden());
        mvc.perform(delete("/v1/fornecedores/{id}", supplier).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNoContent());
        assertThat(jdbc.queryForObject("SELECT ativo FROM fornecedores WHERE id=?", Boolean.class, supplier))
                .isFalse();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM pedidos_fornecedor WHERE fornecedor_id=?",
                Long.class, supplier)).isEqualTo(1L);
        mvc.perform(post("/v1/pedidos-fornecedor").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(order))
                .andExpect(status().isConflict());
        mvc.perform(get("/v1/fornecedores/{id}/historico", supplier)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").isNumber());
        mvc.perform(get("/v1/fornecedores/{id}/historico", supplier)
                .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isForbidden());
        assertThat(jdbc.queryForObject("""
            SELECT COUNT(*) FROM logs_alteracoes
             WHERE empresa_id=? AND registro_id=? AND contexto_operacao='ADM-008'
              AND campos_alterados LIKE '%ativo%'
            """, Long.class, a, supplier)).isGreaterThan(0L);
    }

    @Test
    void usuarioSemPermissaoNaoEditaEConflitoDeVersaoRetorna409() throws Exception {
        long a = empresa("A");
        String token = login(a, "admin" + a + "@fornecedor.t", "ADMIN");
        String viewer = login(a, "viewer" + a + "@fornecedor.t", "LEITOR");
        mvc.perform(post("/v1/fornecedores").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(supplier(a, "11222333000181")))
                .andExpect(status().isCreated());
        long supplier = jdbc.queryForObject("SELECT id FROM fornecedores WHERE empresa_id=?",
                Long.class, a);
        mvc.perform(put("/v1/fornecedores/{id}", supplier)
                .header("Authorization", "Bearer " + viewer)
                .contentType(MediaType.APPLICATION_JSON).content(supplier(a, "11222333000181")))
                .andExpect(status().isForbidden());
        mvc.perform(put("/v1/fornecedores/{id}", supplier).header("Authorization", "Bearer " + token)
                .header("If-Match", "-1")
                .contentType(MediaType.APPLICATION_JSON).content(supplier(a, "11222333000181")))
                .andExpect(status().isConflict());
        mvc.perform(get("/v1/fornecedores").header("Authorization", "Bearer " + token)
                .param("empresaId", Long.toString(a)).param("ativo", "true")
                .param("cidade", "Recife").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    private String supplier(long empresa, String cnpj) throws Exception {
        return json.writeValueAsString(Map.of(
                "empresaId", empresa, "tipoPessoa", "JURIDICA",
                "nomeFantasia", "Fornecedor Automotivo", "razaoSocial", "Fornecedor Automotivo Ltda",
                "cnpj", cnpj, "cidade", "Recife", "estado", "PE", "ativo", true));
    }

    private long empresa(String suffix) {
        String document = suffix + "-" + Long.toString(System.nanoTime(), 36);
        return jdbc.queryForObject("""
                INSERT INTO empresa (cnpj, nome_fantasia, razao_social, ativo, data_cadastro)
                VALUES (?, ?, ?, true, now()) RETURNING id
                """, Long.class, document, "Empresa " + suffix, "Empresa " + suffix);
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
