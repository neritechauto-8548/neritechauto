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
import java.util.LinkedHashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.test.context.jdbc.Sql(scripts = "/sql/adm009_010_test_schema.sql")
@AutoConfigureMockMvc
class TransportadoraTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test void documentosDuplicadosEReusoExplicitoSemOutroCadastro() throws Exception {
        long empresa = empresa("A");
        String token = login(empresa, "carrier1" + empresa + "@t.invalid", "ADMIN");
        String payload = request("11222333000181", null, true);
        mvc.perform(post("/v1/transportadoras").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isCreated());
        mvc.perform(post("/v1/transportadoras").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isConflict());
        mvc.perform(post("/v1/transportadoras").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(request("00000000000000", null, true)))
                .andExpect(status().isUnprocessableEntity());
        mvc.perform(post("/v1/transportadoras").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("tipoPessoa", "FISICA",
                    "nomeFantasia", "Transportadora física", "cpf", "11111111111",
                    "modalidade", "RODOVIARIO"))))
                .andExpect(status().isUnprocessableEntity());
        mvc.perform(post("/v1/transportadoras").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("tipoPessoa", "JURIDICA",
                    "nomeFantasia", "CEP inválido", "cnpj", "11222333000181",
                    "cep", "abc", "modalidade", "RODOVIARIO"))))
                .andExpect(status().isBadRequest());
        long supplierId = jdbc.queryForObject("""
            INSERT INTO fornecedores (empresa_id, tipo_pessoa, nome_fantasia, cnpj, ativo,
                                      transportadora, transportadora_ativa, data_cadastro)
            VALUES (?, 'JURIDICA', 'Fornecedor Compartilhado', '11444777000161', true, false, false, now())
            RETURNING id
            """, Long.class, empresa);
        mvc.perform(post("/v1/transportadoras").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(request("11444777000161", null, true)))
                .andExpect(status().isConflict());
        mvc.perform(post("/v1/transportadoras").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(request("11444777000161", supplierId, true)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.fornecedorId").value(supplierId));
        assertThat(jdbc.queryForObject("""
            SELECT COUNT(*) FROM fornecedores WHERE empresa_id=? AND cnpj = '11444777000161'
            """, Long.class, empresa)).isEqualTo(1L);
    }

    @Test void vinculoPedidoPreservaDocumentoEInativacaoNaoDesativaFornecedor() throws Exception {
        long a = empresa("A");
        long b = empresa("B");
        String tokenA = login(a, "carrierA" + a + "@t.invalid", "ADMIN");
        String tokenB = login(b, "carrierB" + b + "@t.invalid", "ADMIN");
        String tokenViewer = login(a, "carrierV" + a + "@t.invalid", "SEM_ACESSO");
        mvc.perform(post("/v1/transportadoras").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(request("11222333000181", null, true)))
                .andExpect(status().isCreated());
        long carrier = jdbc.queryForObject("""
            SELECT id FROM fornecedores WHERE empresa_id=? AND transportadora=true
            """, Long.class, a);
        long sourceSupplier = jdbc.queryForObject("""
            INSERT INTO fornecedores (empresa_id, tipo_pessoa, nome_fantasia, cnpj, ativo, data_cadastro)
            VALUES (?, 'JURIDICA', 'Peças Oficina', '11444777000161', true, now()) RETURNING id
            """, Long.class, a);
        long purchase = jdbc.queryForObject("""
            INSERT INTO pedidos_fornecedor (empresa_id, fornecedor_id, numero_pedido, responsavel,
                numero_nf, status, data_cadastro) VALUES (?, ?, 750, 'Comprador', 'NF-PRESERVADA', 'RECEBIDO', now())
            RETURNING id
            """, Long.class, a, sourceSupplier);
        long otherPurchase = jdbc.queryForObject("""
            INSERT INTO pedidos_fornecedor (empresa_id, fornecedor_id, numero_pedido, responsavel,
                status, data_cadastro) VALUES (?, ?, 751, 'Comprador', 'PENDENTE', now()) RETURNING id
            """, Long.class, b, fornecedorBasico(b));
        mvc.perform(get("/v1/transportadoras/{id}", carrier)
                .header("Authorization", "Bearer " + tokenB)).andExpect(status().isForbidden());
        mvc.perform(put("/v1/transportadoras/{id}/pedidos/{pedido}", carrier, otherPurchase)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content("{\"documentoRastreio\":\"TESTE\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(put("/v1/transportadoras/{id}/pedidos/{pedido}", carrier, purchase)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content("{\"documentoRastreio\":\"TRK-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.numeroNf").value("NF-PRESERVADA"))
                .andExpect(jsonPath("$.data.documentoRastreio").value("TRK-01"));
        mvc.perform(get("/v1/transportadoras/pedidos/{id}", purchase)
                .header("Authorization", "Bearer " + tokenB)).andExpect(status().isForbidden());
        mvc.perform(put("/v1/transportadoras/{id}", carrier)
                .header("Authorization", "Bearer " + tokenA)
                .header("If-Match", "0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request("33200000000194", carrier, true)))
                .andExpect(status().isConflict());
        mvc.perform(delete("/v1/transportadoras/{id}", carrier)
                .header("Authorization", "Bearer " + tokenViewer)).andExpect(status().isForbidden());
        mvc.perform(delete("/v1/transportadoras/{id}", carrier)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNoContent());
        assertThat(jdbc.queryForObject("SELECT ativo FROM fornecedores WHERE id=?", Boolean.class, carrier)).isTrue();
        assertThat(jdbc.queryForObject("SELECT transportadora_ativa FROM fornecedores WHERE id=?",
                Boolean.class, carrier)).isFalse();
        assertThat(jdbc.queryForObject("SELECT numero_nf FROM pedidos_fornecedor WHERE id=?",
                String.class, purchase)).isEqualTo("NF-PRESERVADA");
        assertThat(jdbc.queryForObject("SELECT transportadora_id FROM pedidos_fornecedor WHERE id=?",
                Long.class, purchase)).isEqualTo(carrier);
        // Um novo pedido da empresa A rejeita transportadora inativa.
        long novoPedido = jdbc.queryForObject("""
            INSERT INTO pedidos_fornecedor (empresa_id, fornecedor_id, numero_pedido, responsavel,
                status, data_cadastro) VALUES (?, ?, 752, 'Comprador', 'PENDENTE', now()) RETURNING id
            """, Long.class, a, sourceSupplier);
        mvc.perform(put("/v1/transportadoras/{id}/pedidos/{pedido}", carrier, novoPedido)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isConflict());
        // IDs de outra empresa são proibidos mesmo após inativação (tenant precede status).
        mvc.perform(put("/v1/transportadoras/{id}/pedidos/{pedido}", carrier, otherPurchase)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
        // Reativação por edição versionada recupera o papel logístico, sem trocar o fornecedor.
        Integer versaoAtual = jdbc.queryForObject(
                "SELECT versao FROM fornecedores WHERE id=?", Integer.class, carrier);
        mvc.perform(put("/v1/transportadoras/{id}", carrier)
                .header("Authorization", "Bearer " + tokenA)
                .header("If-Match", Integer.toString(versaoAtual))
                .contentType(MediaType.APPLICATION_JSON)
                .content(request("11222333000181", carrier, true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ativo").value(true));
        mvc.perform(put("/v1/transportadoras/{id}/pedidos/{pedido}", carrier, novoPedido)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk());
        mvc.perform(get("/v1/transportadoras/{id}/historico", carrier)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").isNumber());
    }

    @Test void conflitoDeVersaoPerfilEIsolamentoDeListagem() throws Exception {
        long a = empresa("A"), b = empresa("B");
        String token = login(a, "carrierC" + a + "@t.invalid", "ADMIN");
        String other = login(b, "carrierD" + b + "@t.invalid", "ADMIN");
        String viewer = login(a, "carrierNo" + a + "@t.invalid", "SEM_ACESSO");
        mvc.perform(post("/v1/transportadoras").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(request("11222333000181", null, true)))
                .andExpect(status().isCreated());
        long carrier = jdbc.queryForObject("SELECT id FROM fornecedores WHERE empresa_id=? AND transportadora=true",
                Long.class, a);
        mvc.perform(get("/v1/transportadoras").header("Authorization", "Bearer " + other)
                .param("size", "20")).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(0));
        mvc.perform(post("/v1/transportadoras").header("Authorization", "Bearer " + viewer)
                .contentType(MediaType.APPLICATION_JSON).content(request("11444777000161", null, true)))
                .andExpect(status().isForbidden());
        mvc.perform(put("/v1/transportadoras/{id}", carrier).header("Authorization", "Bearer " + token)
                .header("If-Match", "-1").contentType(MediaType.APPLICATION_JSON)
                .content(request("11222333000181", carrier, true))).andExpect(status().isConflict());
        mvc.perform(put("/v1/transportadoras/{id}", carrier).header("Authorization", "Bearer " + other)
                .header("If-Match", "0").contentType(MediaType.APPLICATION_JSON)
                .content(request("11222333000181", carrier, true))).andExpect(status().isForbidden());
    }

    private String request(String cnpj, Long supplierId, boolean ativo) throws Exception {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("fornecedorId", supplierId);
        data.put("tipoPessoa", "JURIDICA");
        data.put("nomeFantasia", "Transporte Teste");
        data.put("razaoSocial", "Transportes Teste Ltda");
        data.put("cnpj", cnpj);
        data.put("contato", "Equipe");
        data.put("telefone", "81999999999");
        data.put("email", "contato@teste.com");
        data.put("endereco", "Rua A");
        data.put("cidade", "Recife");
        data.put("estado", "PE");
        data.put("modalidade", "RODOVIARIO");
        data.put("ativo", ativo);
        return json.writeValueAsString(data);
    }
    private long empresa(String suffix) {
        return jdbc.queryForObject("""
            INSERT INTO empresa (cnpj, nome_fantasia, razao_social, ativo, data_cadastro)
            VALUES (?, ?, ?, true, now()) RETURNING id
            """, Long.class, suffix + Long.toString(System.nanoTime(), 36), "Empresa " + suffix, "Empresa " + suffix);
    }
    private long fornecedorBasico(long tenant) {
        return jdbc.queryForObject("""
            INSERT INTO fornecedores (empresa_id, tipo_pessoa, nome_fantasia, ativo, data_cadastro)
            VALUES (?, 'JURIDICA', 'Fornecedor', true, now()) RETURNING id
            """, Long.class, tenant);
    }
    private String login(long empresa, String email, String role) throws Exception {
        var funcao = funcoes.save(TestDataBuilder.umaFuncao().comNome(role).comEmpresaId(empresa).build());
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa).comEmail(email)
                .comSenha(encoder.encode("senha123")).comFuncao(funcao).build());
        var result = mvc.perform(post("/auth/login").header("X-Tenant-Id", Long.toString(empresa))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email", email, "senha", "senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(result.getResponse().getContentAsString()).get("accessToken").asText();
    }
}
