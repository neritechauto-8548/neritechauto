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
import org.springframework.mock.web.MockMultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
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
        String tokenA = login(empresaA, "a-produto-" + empresaA + "@t.co", "ADMIN");
        String tokenB = login(empresaB, "b-produto-" + empresaB + "@t.co", "ADMIN");
        String leitorA = login(empresaA, "l-produto-" + empresaA + "@t.co", "LEITOR");
        jdbc.update("insert into estoques (empresa_id, produto_id, quantidade_atual, preco_custo_lote, data_cadastro, data_atualizacao, status, versao) values (?, ?, 4, 5, now(), now(), 'ATIVO', 0)",
                empresaB, produtoB);
        long categoriaB = jdbc.queryForObject(
                "insert into categorias_produtos (empresa_id, nome, ativo) values (?, ?, true) returning id",
                Long.class, empresaB, "Categoria B " + produtoB);
        long unidadeB = jdbc.queryForObject(
                "insert into unidades_medida (empresa_id, nome, sigla, ativo) values (?, ?, ?, true) returning id",
                Long.class, empresaB, "Unidade B", "BX" + produtoB);

        mvc.perform(get("/v1/produtos/{id}", produtoA)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isOk());
        mvc.perform(get("/v1/produtos/{id}", produtoB)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNotFound());
        mvc.perform(get("/v1/produtos/{id}", produtoB)
                .header("Authorization", "Bearer " + tokenB)).andExpect(status().isOk());
        mvc.perform(get("/v1/produtos").param("empresaId", Long.toString(empresaB))
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isForbidden());

        String requestA = json.writeValueAsString(Map.of("empresaId", empresaA,
                "codigoInterno", "MOD-BLOQUEADA-" + produtoB, "nome", "Alteracao indevida",
                "precoCusto", 5, "precoVenda", 10));
        mvc.perform(put("/v1/produtos/{id}", produtoB)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(requestA))
                .andExpect(status().isNotFound());
        mvc.perform(post("/v1/produtos/{id}/duplicar", produtoB)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNotFound());

        String forgedB = json.writeValueAsString(Map.of("empresaId", empresaB,
                "codigoInterno", "INJETADO-B-" + produtoB, "nome", "Outro tenant",
                "precoCusto", 5, "precoVenda", 10));
        mvc.perform(post("/v1/produtos").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(forgedB))
                .andExpect(status().isForbidden());
        String fkCategoriaB = json.writeValueAsString(Map.of("empresaId", empresaA,
                "categoriaId", categoriaB, "codigoInterno", "FK-CAT-" + produtoB,
                "nome", "Referencia cruzada", "precoCusto", 5, "precoVenda", 10));
        mvc.perform(post("/v1/produtos").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(fkCategoriaB))
                .andExpect(status().isNotFound());
        String fkUnidadeB = json.writeValueAsString(Map.of("empresaId", empresaA,
                "unidadeMedidaId", unidadeB, "codigoInterno", "FK-UNIT-" + produtoB,
                "nome", "Referencia cruzada", "precoCusto", 5, "precoVenda", 10));
        mvc.perform(post("/v1/produtos").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(fkUnidadeB))
                .andExpect(status().isNotFound());

        Path fotoB = Path.of("uploads", "produtos", "fotos", "produto_" + produtoB + ".png");
        Files.deleteIfExists(fotoB);
        mvc.perform(multipart("/v1/produtos/{id}/foto", produtoB)
                .file(new MockMultipartFile("file", "foto.png", "image/png", new byte[]{1, 2, 3}))
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());
        assertThat(Files.exists(fotoB)).isFalse();
        mvc.perform(get("/v1/produtos/{id}/foto", produtoB)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNotFound());

        mvc.perform(delete("/v1/produtos/{id}", produtoA)
                .header("Authorization", "Bearer " + leitorA)).andExpect(status().isForbidden());
        mvc.perform(delete("/v1/produtos/{id}", produtoB)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());

        assertThat(jdbc.queryForObject("select count(*) from produtos where id=? and empresa_id=?",
                Integer.class, produtoB, empresaB)).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from produtos where id=? and empresa_id=?",
                Integer.class, produtoA, empresaA)).isEqualTo(1);
        assertThat(jdbc.queryForObject("select nome from produtos where id=?", String.class, produtoB))
                .isEqualTo("Produto B");
        assertThat(jdbc.queryForObject("select foto_principal_url from produtos where id=?",
                String.class, produtoB)).isNull();
        assertThat(jdbc.queryForObject("select count(*) from estoques where produto_id=?",
                Integer.class, produtoB)).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from produtos where codigo_interno=?",
                Integer.class, "INJETADO-B-" + produtoB)).isZero();
        mvc.perform(delete("/v1/produtos/{id}", produtoA)
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isNoContent());
        assertThat(jdbc.queryForObject("select count(*) from produtos where id=?",
                Integer.class, produtoA)).isZero();
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

    private String login(long empresa, String email, String role) throws Exception {
        var funcao = funcoes.save(TestDataBuilder.umaFuncao()
                .comNome(role).comEmpresaId(empresa).build());
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
