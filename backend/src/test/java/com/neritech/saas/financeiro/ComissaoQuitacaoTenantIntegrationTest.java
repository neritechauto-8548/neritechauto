package com.neritech.saas.financeiro;

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
@org.springframework.test.context.jdbc.Sql(scripts="/db/migration/V298__vinculo_comissao_conta_pagar.sql",
    executionPhase=org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ComissaoQuitacaoTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;

    @Test
    void aprovacaoQuitacaoEstornoAutorizacaoEIdempotencia() throws Exception {
        long a = empresa("A"), b = empresa("B");
        long funcA = funcionario(a, "A"), funcB = funcionario(b, "B");
        long comissaoA = comissao(a, funcA);
        long comissaoB = comissao(b, funcB);
        String tokenA = login(a, "comissao" + a + "@t.co", "ADMIN");
        String tokenB = login(b, "comissao" + b + "@t.co", "ADMIN");
        String viewerA = login(a, "viewcom" + a + "@t.co", "LEITOR");

        mvc.perform(get("/v1/financeiro/comissoes").param("empresaId", Long.toString(a))
                .param("size", "20").header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.content[0].pago").value(false));
        mvc.perform(get("/v1/financeiro/comissoes").param("empresaId", Long.toString(a))
                .param("paga", "true").param("size", "20").header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.totalElements").value(0));
        mvc.perform(get("/v1/financeiro/comissoes").param("empresaId", Long.toString(b))
                .header("Authorization", "Bearer " + tokenA)).andExpect(status().isForbidden());
        mvc.perform(get("/v1/financeiro/comissoes").param("empresaId", Long.toString(a))
                .header("Authorization", "Bearer " + viewerA)).andExpect(status().isForbidden());
        mvc.perform(get("/v1/financeiro/comissoes/{id}", comissaoB)
                .param("empresaId", Long.toString(a)).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());

        String pagamento = json.writeValueAsString(Map.of("dataPagamento", "2026-10-29", "gerarContaPagar", false));
        mvc.perform(post("/v1/financeiro/comissoes/{id}/quitar", comissaoA)
                .param("empresaId", Long.toString(a)).header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(pagamento)).andExpect(status().isConflict());
        mvc.perform(post("/v1/financeiro/comissoes/{id}/aprovar", comissaoA)
                .param("empresaId", Long.toString(b)).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
        mvc.perform(post("/v1/financeiro/comissoes/{id}/aprovar", comissaoB)
                .param("empresaId", Long.toString(a)).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());
        mvc.perform(post("/v1/financeiro/comissoes/{id}/aprovar", comissaoA)
                .param("empresaId", Long.toString(a)).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.aprovado").value(true));

        String indevido = json.writeValueAsString(Map.of("dataPagamento", "2026-10-29", "gerarContaPagar", true));
        mvc.perform(post("/v1/financeiro/comissoes/{id}/quitar", comissaoA)
                .param("empresaId", Long.toString(a)).header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(indevido)).andExpect(status().isUnprocessableEntity());
        mvc.perform(post("/v1/financeiro/comissoes/{id}/quitar", comissaoA)
                .param("empresaId", Long.toString(a)).header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(pagamento))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.pago").value(true));
        mvc.perform(post("/v1/financeiro/comissoes/{id}/quitar", comissaoA)
                .param("empresaId", Long.toString(a)).header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON).content(pagamento)).andExpect(status().isConflict());
        assertThat(jdbc.queryForObject("select paga from fin_comissoes_funcionarios where id=?", Boolean.class, comissaoA)).isTrue();

        mvc.perform(post("/v1/financeiro/comissoes/{id}/estornar", comissaoA)
                .param("empresaId", Long.toString(b)).header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isNotFound());
        mvc.perform(post("/v1/financeiro/comissoes/{id}/estornar", comissaoA)
                .param("empresaId", Long.toString(a)).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.pago").value(false));
        mvc.perform(post("/v1/financeiro/comissoes/{id}/estornar", comissaoA)
                .param("empresaId", Long.toString(a)).header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isConflict());
        assertThat(jdbc.queryForObject("select count(*) from logs_alteracoes where empresa_id=? and contexto_operacao='FIN-007'", Integer.class, a))
                .isGreaterThanOrEqualTo(3);
        assertThat(jdbc.queryForObject("select paga from fin_comissoes_funcionarios where id=?", Boolean.class, comissaoB)).isFalse();
    }

    @Test
    void geraContaPagarPendenteUnicaEEstornoCancelaSemTransferencia() throws Exception {
        long empresa = empresa("CP");
        long funcionario = funcionario(empresa,"CP");
        long comissao = comissao(empresa,funcionario);
        String token = login(empresa,"contapagar"+empresa+"@t.co","ADMIN");
        long plano = jdbc.queryForObject("""
                insert into fin_plano_contas
                  (empresa_id,codigo,nome,nivel,ativo,aceita_lancamento,data_cadastro,versao)
                values (?,'4.2.1','Comissões',1,true,true,now(),0) returning id
                """,Long.class,empresa);
        mvc.perform(post("/v1/financeiro/comissoes/{id}/aprovar",comissao)
                .param("empresaId",Long.toString(empresa)).header("Authorization","Bearer "+token))
                .andExpect(status().isOk());
        var corpo = json.writeValueAsString(Map.of(
                "dataPagamento","2026-10-29","gerarContaPagar",true,
                "planoContaId",plano,"tipoDocumento","OUTROS"));
        mvc.perform(post("/v1/financeiro/comissoes/{id}/quitar",comissao)
                .param("empresaId",Long.toString(empresa))
                .header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.pago").value(true));
        assertThat(jdbc.queryForObject("""
                select count(*) from fin_comissao_conta_pagar r join fin_contas_pagar c
                  on c.id=r.conta_pagar_id and c.empresa_id=r.empresa_id
                where r.empresa_id=? and r.comissao_id=? and c.status='ABERTO'
                  and c.plano_contas_id=? and c.valor_pago=0
                """,Integer.class,empresa,comissao,plano)).isEqualTo(1);
        mvc.perform(post("/v1/financeiro/comissoes/{id}/quitar",comissao)
                .param("empresaId",Long.toString(empresa))
                .header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isConflict());
        mvc.perform(post("/v1/financeiro/comissoes/{id}/estornar",comissao)
                .param("empresaId",Long.toString(empresa))
                .header("Authorization","Bearer "+token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.pago").value(false));
        assertThat(jdbc.queryForObject("""
                select count(*) from fin_comissao_conta_pagar r join fin_contas_pagar c
                    on c.id=r.conta_pagar_id and c.empresa_id=r.empresa_id
                where r.empresa_id=? and r.comissao_id=? and c.status='CANCELADO'
                """,Integer.class,empresa,comissao)).isEqualTo(1);
    }

    @Test
    void quitacaoDiretaELoteSaoAtomicosEIsoladosPorEmpresa() throws Exception {
        long empresaA=empresa("LOTE-A"), empresaB=empresa("LOTE-B");
        long funcA=funcionario(empresaA,"L1");
        long comissao1=comissao(empresaA,funcA);
        long comissao2=comissao(empresaA,funcA);
        long comissaoB=comissao(empresaB,funcionario(empresaB,"L2"));
        String token=login(empresaA,"lote"+empresaA+"@t.co","ADMIN");
        var dados=Map.of("dataPagamento","2026-10-29","gerarContaPagar",false);
        var invalidos=json.writeValueAsString(Map.of("ids",new long[]{comissao1,comissaoB},"dados",dados));
        mvc.perform(post("/v1/financeiro/comissoes/quitar-lote")
                .param("empresaId",Long.toString(empresaA))
                .header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(invalidos))
                .andExpect(status().isNotFound());
        assertThat(jdbc.queryForObject("select count(*) from fin_comissoes_funcionarios where empresa_id=? and paga=true",
                Integer.class,empresaA)).isZero();

        var lote=json.writeValueAsString(Map.of("ids",new long[]{comissao1,comissao2},"dados",dados));
        mvc.perform(post("/v1/financeiro/comissoes/quitar-lote")
                .param("empresaId",Long.toString(empresaA))
                .header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(lote))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.length()").value(2));
        assertThat(jdbc.queryForObject("select count(*) from fin_comissoes_funcionarios where empresa_id=? and paga=true",
                Integer.class,empresaA)).isEqualTo(2);
        assertThat(jdbc.queryForObject("select count(*) from fin_contas_pagar where empresa_id=?",
                Integer.class,empresaA)).isZero();
    }

    private long empresa(String suffix) {
        return jdbc.queryForObject("insert into empresa (cnpj,nome_fantasia,ativo,data_cadastro) values (?,?,true,now()) returning id",
                Long.class, suffix + Long.toString(System.nanoTime(),36), "Comissão " + suffix);
    }
    private long funcionario(long empresa, String suffix) {
        return jdbc.queryForObject(
                "insert into funcionarios (empresa_id,matricula,nome_completo,data_admissao,status,data_cadastro,versao) values (?,?,?,current_date,'ATIVO',now(),0) returning id",
                Long.class, empresa, "COM" + suffix + empresa, "Funcionário " + suffix);
    }
    private long comissao(long empresa, long funcionario) {
        return jdbc.queryForObject("""
                insert into fin_comissoes_funcionarios (empresa_id,funcionario_id,base_calculo,percentual_comissao,
                    valor_comissao,data_competencia,paga,versao,data_cadastro)
                values (?,?,100,10,10,'2026-10-01',false,0,now()) returning id
                """, Long.class, empresa, funcionario);
    }
    private String login(long empresa, String email, String role) throws Exception {
        var funcao = funcoes.save(TestDataBuilder.umaFuncao().comEmpresaId(empresa).comNome(role).build());
        usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa).comEmail(email).comSenha(encoder.encode("senha123")).comFuncao(funcao).build());
        var response = mvc.perform(post("/auth/login").header("X-Tenant-Id", Long.toString(empresa))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("email",email,"senha","senha123"))))
                .andExpect(status().isOk()).andReturn();
        return json.readTree(response.getResponse().getContentAsString()).get("accessToken").asText();
    }
}
