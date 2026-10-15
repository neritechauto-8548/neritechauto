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
class ProdutoTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void empresaANaoPodeExcluirProdutoDaEmpresaB() throws Exception {
        long empresaA = empresa("A");
        long empresaB = empresa("B");
        long produtoA = produto(empresaA, "A");
        long produtoB = produto(empresaB, "B");
        String tokenA = login(empresaA, "a-produto-" + empresaA + "@t.co");

        mvc.perform(delete("/v1/produtos/{id}", produtoB)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());

        assertThat(jdbc.queryForObject("select count(*) from produtos where id=? and empresa_id=?",
                Integer.class, produtoB, empresaB)).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from produtos where id=? and empresa_id=?",
                Integer.class, produtoA, empresaA)).isEqualTo(1);
    }

    private long empresa(String suffix) {
        return jdbc.queryForObject(
                "insert into empresa (cnpj, nome_fantasia, razao_social, ativo, data_cadastro) " +
                "values (?, ?, ?, true, now()) returning id",
                Long.class, suffix + "-" + Long.toString(System.nanoTime(), 36),
                "Empresa " + suffix, "Empresa " + suffix);
    }

    private long produto(long empresa, String suffix) {
        return jdbc.queryForObject(
                "insert into produtos (empresa_id, codigo_interno, nome, preco_custo, preco_venda, ativo, data_cadastro, versao) " +
                "values (?, ?, ?, 5, 10, true, now(), 0) returning id",
                Long.class, empresa, "BASE-01-" + suffix + "-" + Long.toString(System.nanoTime(), 36),
                "Produto " + suffix);
    }

    private String login(long empresa, String email) throws Exception {
        var funcao = funcoes.save(TestDataBuilder.umaFuncao()
                .comNome("ADMIN").comEmpresaId(empresa).build());
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa)
                .comEmail(email).comSenha(encoder.encode("senha123")).comFuncao(funcao).build());
        var response = mvc.perform(post("/auth/login")
                .header("X-Tenant-Id", Long.toString(empresa))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email", email, "senha", "senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString()).get("accessToken").asText();
    }
}
