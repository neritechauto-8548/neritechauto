package com.neritech.saas.empresa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.AbstractIntegrationTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class AreaTenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired MockMvc mvc; @Autowired ObjectMapper json; @Autowired JdbcTemplate jdbc;
    @Autowired FuncaoRepository funcoes; @Autowired UsuarioRepository usuarios; @Autowired PasswordEncoder encoder;

    @Test
    void protegeDuplicidadeInativacaoVinculosTenantEPermissao() throws Exception {
        long a=empresa("A"), b=empresa("B"); String adminA=login(a,"admin"+a+"@t.co","ADMIN");
        String adminB=login(b,"admin"+b+"@t.co","ADMIN"); String viewer=login(a,"viewer"+a+"@t.co","LEITOR");
        var created=mvc.perform(post("/v1/departamentos-contabio-empresa").header("Authorization","Bearer "+adminA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a,"MEC","Mecânica",null)))
                .andExpect(status().isCreated()).andReturn();
        var body=json.readTree(created.getResponse().getContentAsString()); if(body.has("data")) body=body.get("data");
        long id=body.get("id").asLong(); int version=body.get("versao").asInt();
        long areaB=jdbc.queryForObject("insert into departamentos_contabio (empresa_id,codigo,descricao,ativo,ordem,data_cadastro,versao) values (?,'MEC','Mecânica',true,0,now(),0) returning id",Long.class,b);

        mvc.perform(get("/v1/departamentos-contabio-empresa/{id}",areaB).header("Authorization","Bearer "+adminA)).andExpect(status().isNotFound());
        mvc.perform(get("/v1/departamentos-contabio-empresa/{id}",areaB).header("Authorization","Bearer "+adminB)).andExpect(status().isOk());
        mvc.perform(get("/v1/departamentos-contabio-empresa").header("Authorization","Bearer "+viewer)).andExpect(status().isForbidden());
        mvc.perform(post("/v1/departamentos-contabio-empresa").header("Authorization","Bearer "+adminA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a,"OUT"," mecânica ",null))).andExpect(status().isConflict());

        mvc.perform(put("/v1/departamentos-contabio-empresa/{id}",id).header("Authorization","Bearer "+adminA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a,"MEC","Mecânica Geral",version))).andExpect(status().isOk());
        mvc.perform(put("/v1/departamentos-contabio-empresa/{id}",id).header("Authorization","Bearer "+adminA)
                .contentType(MediaType.APPLICATION_JSON).content(payload(a,"MEC","Obsoleta",version))).andExpect(status().isConflict());

        jdbc.update("insert into funcionarios (empresa_id,departamento_id,matricula,nome_completo,data_admissao,status,data_cadastro,versao) values (?,?,?,'Funcionário',current_date,'ATIVO',now(),0)",a,id,"MAT"+a);
        mvc.perform(delete("/v1/departamentos-contabio-empresa/{id}",id).header("Authorization","Bearer "+adminA)).andExpect(status().isNoContent());
        assertThat(jdbc.queryForObject("select ativo from departamentos_contabio where id=?",Boolean.class,id)).isFalse();
        assertThat(jdbc.queryForObject("select count(*) from funcionarios where departamento_id=?",Integer.class,id)).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from logs_alteracoes where empresa_id=? and contexto_operacao='ADM-007'",Integer.class,a)).isGreaterThanOrEqualTo(3);
    }
    private String payload(long empresa,String codigo,String nome,Integer versao) throws Exception {
        var map=new java.util.HashMap<String,Object>(); map.put("empresaId",empresa); map.put("codigo",codigo); map.put("descricao",nome); map.put("ativo",true); map.put("ordem",0); if(versao!=null)map.put("versao",versao); return json.writeValueAsString(map);
    }
    private long empresa(String s){return jdbc.queryForObject("insert into empresa (cnpj,nome_fantasia,ativo,data_cadastro) values (?,?,true,now()) returning id",Long.class,s+Long.toString(System.nanoTime(),36),"Empresa "+s);}
    private String login(long empresa,String email,String role)throws Exception{var f=funcoes.save(TestDataBuilder.umaFuncao().comEmpresaId(empresa).comNome(role).build());usuarios.save(TestDataBuilder.umUsuario().comEmpresaId(empresa).comEmail(email).comSenha(encoder.encode("senha123")).comFuncao(f).build());var r=mvc.perform(post("/auth/login").header("X-Tenant-Id",Long.toString(empresa)).contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(Map.of("email",email,"senha","senha123")))).andExpect(status().isOk()).andReturn();return json.readTree(r.getResponse().getContentAsString()).get("accessToken").asText();}
}
