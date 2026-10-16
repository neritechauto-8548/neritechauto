package com.neritech.saas.gestaoUsuarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.AbstractIntegrationTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
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
import java.util.Set;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class RbacTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void protegeMatrizContraEscaladaTenantConflitoEUsoDeTokenRevogado() throws Exception {
        long a = empresa("A"); long b = empresa("B");
        Funcao adminRole = role(a, "ADMIN");
        String adminA = userToken(a, "admin" + a + "@t.co", adminRole);
        String adminB = userToken(b, "admin" + b + "@t.co", role(b, "ADMIN"));
        String limitado = userToken(a, "limitado" + a + "@t.co", role(a, "FUNCAO.UPDATE"));

        var created = mvc.perform(post("/v1/funcoes").header("Authorization", "Bearer " + adminA)
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(Map.of(
                        "nome", "Aprovador", "descricao", "Aprova operações", "ativo", true,
                        "permissaoIds", Set.of())))).andExpect(status().isCreated()).andReturn();
        var body = json.readTree(created.getResponse().getContentAsString());
        if (body.has("data")) body = body.get("data");
        long id = body.get("id").asLong(); int version = body.get("versao").asInt();

        mvc.perform(get("/v1/funcoes").header("Authorization", "Bearer " + limitado)).andExpect(status().isForbidden());
        mvc.perform(get("/v1/funcoes/{id}", id).header("Authorization", "Bearer " + adminB)).andExpect(status().isNotFound());
        mvc.perform(post("/v1/funcoes").header("Authorization", "Bearer " + adminA)
                .contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\" aprovador \",\"permissaoIds\":[]}"))
                .andExpect(status().isConflict());

        mvc.perform(put("/v1/funcoes/{id}", id).header("Authorization", "Bearer " + adminA)
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(Map.of(
                        "nome", "Aprovador Sênior", "ativo", true, "permissaoIds", Set.of(), "versao", version))))
                .andExpect(status().isOk());
        mvc.perform(put("/v1/funcoes/{id}", id).header("Authorization", "Bearer " + adminA)
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(Map.of(
                        "nome", "Desatualizado", "ativo", true, "permissaoIds", Set.of(), "versao", version))))
                .andExpect(status().isConflict());
        mvc.perform(delete("/v1/funcoes/{id}", adminRole.getId()).header("Authorization", "Bearer " + adminA))
                .andExpect(status().isConflict());

        var secondAdmin = TestDataBuilder.umUsuario().comEmpresaId(a).comEmail("segundo" + a + "@t.co")
                .comSenha(encoder.encode("senha123")).comFuncao(adminRole).build();
        usuarios.save(secondAdmin);
        String oldToken = login(a, secondAdmin.getEmail());
        mvc.perform(get("/v1/funcoes").header("Authorization", "Bearer " + oldToken)).andExpect(status().isOk());
        jdbc.update("delete from usuarios_funcoes where usuario_id=? and funcao_id=?", secondAdmin.getId(), adminRole.getId());
        mvc.perform(get("/v1/funcoes").header("Authorization", "Bearer " + oldToken)).andExpect(status().isForbidden());
    }

    private Funcao role(long empresa, String nome) { return funcoes.save(Funcao.builder().empresaId(empresa).nome(nome).ativo(true).sistema(false).build()); }
    private String userToken(long empresa, String email, Funcao role) throws Exception {
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa).comEmail(email).comSenha(encoder.encode("senha123")).comFuncao(role).build());
        return login(empresa, email);
    }
    private String login(long empresa, String email) throws Exception {
        var response = mvc.perform(post("/auth/login").header("X-Tenant-Id", Long.toString(empresa))
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(Map.of("email",email,"senha","senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString()).get("accessToken").asText();
    }
    private long empresa(String suffix) {
        return jdbc.queryForObject("insert into empresa (cnpj,nome_fantasia,ativo,data_cadastro) values (?,?,true,now()) returning id",
                Long.class, suffix + Long.toString(System.nanoTime(),36), "Empresa " + suffix);
    }
}
