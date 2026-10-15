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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class EmpresaTenantIntegrationTest extends AbstractIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void empresaANaoPodeConsultarEditarExcluirNemEnviarLogoParaEmpresaB() throws Exception {
        long empresaA = empresa("A");
        long empresaB = empresa("B");
        String tokenA = login(empresaA, "a" + empresaA + "@t.co", "ADMIN");
        String tokenB = login(empresaB, "b" + empresaB + "@t.co", "ADMIN");
        String viewerA = login(empresaA, "v" + empresaA + "@t.co", "LEITOR");

        mvc.perform(get("/v1/empresas/{id}", empresaA).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk());
        mvc.perform(get("/v1/empresas/{id}", empresaB).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        mvc.perform(get("/v1/empresas/{id}", empresaB).header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk());

        // Download da logo também exige isolamento, inclusive quando não existe arquivo.
        mvc.perform(get("/v1/empresas/{id}/logo", empresaB)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        mvc.perform(get("/v1/empresas/{id}/logo", empresaA)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());

        mvc.perform(get("/v1/empresas").header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(empresaA));
        String cnpjB = jdbc.queryForObject("select cnpj from empresa where id=?", String.class, empresaB);
        mvc.perform(get("/v1/empresas").param("cnpj", cnpjB)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(0));

        String payload = json.writeValueAsString(Map.of(
                "nomeFantasia", "ALTERACAO-CRUZADA",
                "razaoSocial", "ALTERACAO-CRUZADA",
                "cnpj", "12345678909"
        ));
        mvc.perform(put("/v1/empresas/{id}", empresaB)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/v1/empresas/{id}", empresaB)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        mvc.perform(multipart("/v1/empresas/{id}/logo", empresaB)
                .file(new MockMultipartFile("file", "logo.png", "image/png", new byte[]{1, 2, 3}))
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        mvc.perform(put("/v1/empresas/{id}", empresaA)
                .header("Authorization", "Bearer " + viewerA)
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isForbidden());

        // O cadastro de outras empresas não pode ser liberado por uma ROLE_ADMIN local.
        mvc.perform(post("/v1/empresas").header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isForbidden());

        assertThat(jdbc.queryForObject("select count(*) from empresa where id=?", Integer.class, empresaB)).isEqualTo(1);
        assertThat(jdbc.queryForObject("select nome_fantasia from empresa where id=?", String.class, empresaB)).isEqualTo("Empresa B");
        assertThat(jdbc.queryForObject("select logo_path from empresa where id=?", String.class, empresaB)).isNull();
        assertThat(jdbc.queryForObject("select count(*) from empresa where id=?", Integer.class, empresaA)).isEqualTo(1);
    }

    @Test
    void configuracoesDaEmpresaNaoVazamEntreTenants() throws Exception {
        long empresaA = empresa("A");
        long empresaB = empresa("B");
        String tokenA = login(empresaA, "a" + empresaA + "@t.co", "ADMIN");

        for (String base : new String[] {
                "/v1/enderecos-empresa", "/v1/configuracoes-empresa",
                "/v1/configuracoes-fiscais", "/v1/configuracoes-oficina"
        }) {
            mvc.perform(get(base + "/empresa/{id}", empresaB)
                    .header("Authorization", "Bearer " + tokenA))
                    .andExpect(status().isForbidden());
            mvc.perform(get(base).header("Authorization", "Bearer " + tokenA))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.totalElements").value(0));
        }
    }

    @Test
    void logoInvalidaNaoEPersistidaEExclusaoFisicaDaEmpresaEProibida() throws Exception {
        long empresaA = empresa("A");
        String tokenA = login(empresaA, "a" + empresaA + "@t.co", "ADMIN");

        mvc.perform(multipart("/v1/empresas/{id}/logo", empresaA)
                .file(new MockMultipartFile("file", "logo.svg", "image/svg+xml",
                        "<svg onload='alert(1)'/>".getBytes(java.nio.charset.StandardCharsets.UTF_8)))
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isUnprocessableEntity());
        mvc.perform(multipart("/v1/empresas/{id}/logo", empresaA)
                .file(new MockMultipartFile("file", "imagem.png", "image/png", new byte[]{1, 2, 3}))
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isUnprocessableEntity());

        mvc.perform(delete("/v1/empresas/{id}", empresaA)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        assertThat(jdbc.queryForObject("select logo_path from empresa where id=?", String.class, empresaA)).isNull();
        assertThat(jdbc.queryForObject("select count(*) from empresa where id=?", Integer.class, empresaA)).isEqualTo(1);
    }

    @Test
    void rejeitaVersaoObsoletaEIgnoraLogoPathForjado() throws Exception {
        long empresaA = empresa("A");
        String tokenA = login(empresaA, "a" + empresaA + "@t.co", "ADMIN");
        int versao = jdbc.queryForObject("select versao from empresa where id=?", Integer.class, empresaA);
        String atualizacao = json.writeValueAsString(Map.of(
                "nomeFantasia", "Oficina segura", "razaoSocial", "Empresa segura",
                "cnpj", "52998224725", "versao", versao,
                "logoPath", "/etc/passwd"));
        mvc.perform(put("/v1/empresas/{id}", empresaA)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(atualizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.logoPath").doesNotExist());
        assertThat(jdbc.queryForObject("select logo_path from empresa where id=?", String.class, empresaA)).isNull();

        mvc.perform(put("/v1/empresas/{id}", empresaA)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(atualizacao))
                .andExpect(status().isConflict());
    }

    @Test
    void mudancaFiscalGravaHistoricoVersionadoComAutorRealESemSegredos() throws Exception {
        long empresaA = empresa("A");
        long empresaB = empresa("B");
        String emailA = "audit" + empresaA + "@t.co";
        String tokenA = login(empresaA, emailA, "ADMIN");
        String tokenB = login(empresaB, "audit" + empresaB + "@t.co", "ADMIN");

        String criar = json.writeValueAsString(Map.of(
                "empresaId", empresaA, "regimeTributario", "SIMPLES_NACIONAL",
                "ambienteNfe", "HOMOLOGACAO"));
        mvc.perform(post("/v1/configuracoes-fiscais")
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(criar))
                .andExpect(status().isCreated());
        long configId = jdbc.queryForObject(
                "select id from configuracoes_fiscais where empresa_id=?", Long.class, empresaA);

        String alterar = json.writeValueAsString(Map.of(
                "empresaId", empresaA, "regimeTributario", "LUCRO_PRESUMIDO",
                "ambienteNfe", "PRODUCAO"));
        mvc.perform(put("/v1/configuracoes-fiscais/{id}", configId)
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(alterar))
                .andExpect(status().isOk());

        long autor = usuarios.findByEmailIgnoreCaseAndEmpresaId(emailA, empresaA).orElseThrow().getId();
        assertThat(jdbc.queryForObject("""
                select count(*) from logs_alteracoes where empresa_id = ?
                and tabela_afetada = 'configuracoes_fiscais' and contexto_operacao = 'ADM-001'
                and usuario_responsavel = ? and campos_alterados like '%regimeTributario%'
                """, Long.class, empresaA, autor)).isEqualTo(2L);
        String auditPayload = jdbc.queryForObject("""
                select string_agg(coalesce(valores_novos, ''), ' ')
                from logs_alteracoes where empresa_id = ? and contexto_operacao = 'ADM-001'
                """, String.class, empresaA);
        assertThat(auditPayload).doesNotContain("senhaCertificado", "tokenAcessoProvedor", "certificadoDigitalA1");

        mvc.perform(get("/v1/empresas/{id}/historico", empresaA)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").isNumber())
                .andExpect(jsonPath("$.data.content[0].autor").value(autor));
        mvc.perform(get("/v1/empresas/{id}/historico", empresaA)
                .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isForbidden());
        mvc.perform(get("/v1/empresas/{id}/historico", empresaA)
                .header("Authorization", "Bearer " + tokenA).param("size", "1000"))
                .andExpect(status().isUnprocessableEntity());

        mvc.perform(put("/v1/configuracoes-fiscais/{id}", configId)
                .header("Authorization", "Bearer " + tokenB)
                .contentType(MediaType.APPLICATION_JSON).content(alterar))
                .andExpect(status().isForbidden());
    }

    @Test
    void preferenciasDeTemaFusoEMoedaSaoValidadasAuditadasEIsoladas() throws Exception {
        long a = empresa("A");
        long b = empresa("B");
        String email = "tema" + a + "@t.co";
        String token = login(a, email, "ADMIN");
        String other = login(b, "tema" + b + "@t.co", "ADMIN");
        String payload = json.writeValueAsString(Map.of(
                "empresaId", a, "corTema", "dark", "timezone", "America/Recife", "moeda", "BRL"));
        mvc.perform(post("/v1/configuracoes-oficina")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.corTema").value("dark"));
        assertThat(jdbc.queryForObject(
                "select cor_tema from configuracoes_oficina where empresa_id = ?",
                String.class, a)).isEqualTo("dark");
        assertThat(jdbc.queryForObject("""
                select count(*) from logs_alteracoes
                where empresa_id = ? and contexto_operacao = 'ADM-001'
                and campos_alterados like '%corTema%'
                """, Long.class, a)).isGreaterThan(0L);
        mvc.perform(get("/v1/configuracoes-oficina/empresa/{id}", a)
                .header("Authorization", "Bearer " + other))
                .andExpect(status().isForbidden());

        for (Map<String, Object> fields : java.util.List.<Map<String, Object>>of(
                Map.of("empresaId", a, "corTema", "qualquer"),
                Map.of("empresaId", a, "moeda", "USD"),
                Map.of("empresaId", a, "timezone", "Fuso/Invalido")
        )) {
            mvc.perform(post("/v1/configuracoes-oficina")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(fields)))
                    .andExpect(status().isUnprocessableEntity());
        }
        mvc.perform(post("/v1/configuracoes-oficina")
                .header("Authorization", "Bearer " + other)
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isForbidden());
    }

    @Test
    void documentoInvalidoEPermissaoDeEdicaoSaoValidadosNoServidor() throws Exception {
        long a = empresa("A");
        String token = login(a, "cnpj" + a + "@t.co", "ADMIN");
        String viewer = login(a, "view" + a + "@t.co", "LEITOR");
        String invalido = json.writeValueAsString(Map.of(
                "nomeFantasia", "Oficina", "razaoSocial", "Oficina Fiscal",
                "cnpj", "00000000000000"));
        mvc.perform(put("/v1/empresas/{id}", a)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(invalido))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/v1/empresas/{id}", a)
                .header("Authorization", "Bearer " + viewer)
                .contentType(MediaType.APPLICATION_JSON).content(invalido))
                .andExpect(status().isForbidden());
        String endereco = json.writeValueAsString(Map.of(
                "empresaId", a, "cep", "123", "logradouro", "Rua A", "numero", "1",
                "bairro", "Centro", "cidade", "Recife", "estado", "PE"));
        mvc.perform(post("/v1/enderecos-empresa")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(endereco))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void uploadERemocaoDeLogoRegistramVersoesSemExporCaminhoPrivado() throws Exception {
        long empresaA = empresa("A");
        String tokenA = login(empresaA, "marca" + empresaA + "@t.co", "ADMIN");
        java.awt.image.BufferedImage imagem = new java.awt.image.BufferedImage(
                2, 2, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        javax.imageio.ImageIO.write(imagem, "png", buffer);

        mvc.perform(multipart("/v1/empresas/{id}/logo", empresaA)
                .file(new MockMultipartFile("file", "marca.png", "image/png", buffer.toByteArray()))
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.logoPath").value("available"));

        mvc.perform(get("/v1/empresas/{id}", empresaA)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.logoPath").value("available"));
        mvc.perform(get("/v1/empresas/{id}/logo", empresaA)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/png"));

        mvc.perform(delete("/v1/empresas/{id}/logo", empresaA)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNoContent());
        mvc.perform(get("/v1/empresas/{id}/logo", empresaA)
                .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());

        Long alteracoes = jdbc.queryForObject("""
                select count(*) from logs_alteracoes where empresa_id = ?
                and contexto_operacao = 'ADM-001' and campos_alterados like '%marca%'
                """, Long.class, empresaA);
        assertThat(alteracoes).isEqualTo(2L);
        String historico = jdbc.queryForObject("""
                select string_agg(valores_novos, ' ') from logs_alteracoes
                where empresa_id = ? and contexto_operacao = 'ADM-001'
                """, String.class, empresaA);
        assertThat(historico).doesNotContain("/uploads/", "logo_");
    }

    private long empresa(String suffix) {
        String document = suffix + "-" + Long.toString(System.nanoTime(), 36);
        return jdbc.queryForObject(
                "insert into empresa (cnpj, nome_fantasia, razao_social, ativo, data_cadastro) " +
                "values (?, ?, ?, true, now()) returning id",
                Long.class, document, "Empresa " + suffix, "Empresa " + suffix);
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
