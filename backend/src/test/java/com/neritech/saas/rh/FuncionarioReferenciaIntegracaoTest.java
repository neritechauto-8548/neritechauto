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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Map;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Sql(scripts={
    "/db/migration/V294__funcionarios_referencia_profissional_contatos.sql",
    "/db/migration/V295__perfil_profissional_funcionarios.sql",
    "/db/migration/V296__remover_campos_legados_funcionarios.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FuncionarioReferenciaIntegracaoTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void cadastroMinimoContatosEColunasPortuguesasComIsolamento() throws Exception {
        // Confere que o contrato aditivo + limpeza destrutiva são executáveis no PostgreSQL 16.
        for(String removida : List.of("nome_mae","banco_codigo","estado_civil","comissao_percentual",
                "cnh_numero","email_pessoal","plano_saude","telefone_principal")) {
            Integer n=jdbc.queryForObject("""
                select count(*) from information_schema.columns
                where table_schema=current_schema() and table_name='funcionarios' and column_name=?
                """,Integer.class,removida);
            assertThat(n).as(removida).isZero();
        }
        for(String vigente : List.of("perfil_profissional","registro_profissional",
                "comissao_pecas","comissao_servicos","linhas_transporte","nome_completo")) {
            Integer n=jdbc.queryForObject("""
                select count(*) from information_schema.columns
                where table_schema=current_schema() and table_name='funcionarios' and column_name=?
                """,Integer.class,vigente);
            assertThat(n).as(vigente).isEqualTo(1);
        }
        long empresaA=empresa("A"), empresaB=empresa("B");
        String tokenA=token(empresaA,"refa"+empresaA+"@t.co","ADMIN");
        String tokenB=token(empresaB,"refb"+empresaB+"@t.co","ADMIN");
        var funcionario=jdbc.queryForObject("""
            insert into funcionarios(empresa_id,matricula,nome_completo,cpf,status,versao,data_cadastro)
            values (?,?,?,?,'ATIVO',0,now()) returning id
            """,Long.class,empresaA,"REF"+empresaA,"Funcionário de teste","935.411.347-80");
        String body=json.writeValueAsString(Map.of("nome","Contato principal","principal",true,
                "meios",List.of(Map.of("tipo","WHATSAPP","valor","81999998888"))));
        mvc.perform(post("/v1/rh/funcionarios/{id}/contatos",funcionario)
                .header("Authorization","Bearer "+tokenA).contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.meios[0].tipo").value("WHATSAPP"));
        mvc.perform(get("/v1/rh/funcionarios/{id}/contatos",funcionario)
                .header("Authorization","Bearer "+tokenA)).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
        mvc.perform(get("/v1/rh/funcionarios/{id}/contatos",funcionario)
                .header("Authorization","Bearer "+tokenB)).andExpect(status().isNotFound());
        mvc.perform(post("/v1/rh/funcionarios/{id}/contatos",funcionario)
                .header("Authorization","Bearer "+tokenB).contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isNotFound());
        assertThat(jdbc.queryForObject("select count(*) from rh_funcionario_contatos where empresa_id=?",
            Integer.class,empresaA)).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from logs_alteracoes where empresa_id=? and contexto_operacao='ADM-004-CONTATOS'",
            Integer.class,empresaA)).isGreaterThan(0);
    }
    private long empresa(String nome) {
        return jdbc.queryForObject("insert into empresa (cnpj,nome_fantasia,ativo,data_cadastro) values (?,?,true,now()) returning id",
                Long.class, nome + Long.toString(System.nanoTime(),36), "Empresa "+nome);
    }
    private String token(long empresa,String email,String papel) throws Exception {
        var funcao=funcoes.save(TestDataBuilder.umaFuncao().comEmpresaId(empresa).comNome(papel).build());
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa).comEmail(email).comSenha(encoder.encode("senha123")).comFuncao(funcao).build());
        var resposta=mvc.perform(post("/auth/login").header("X-Tenant-Id",Long.toString(empresa))
             .contentType(MediaType.APPLICATION_JSON)
             .content(json.writeValueAsString(Map.of("email",email,"senha","senha123"))))
             .andExpect(status().isOk()).andReturn();
        return json.readTree(resposta.getResponse().getContentAsString()).get("accessToken").asText();
    }
}
