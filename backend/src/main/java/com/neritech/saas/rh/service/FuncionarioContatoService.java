package com.neritech.saas.rh.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FuncionarioContatoService {
    private final JdbcTemplate jdbc;
    private final UsuarioRepository usuarios;
    private final ObjectMapper json;
    public record MeioRequest(@NotBlank String tipo, @NotBlank @Size(max=255) String valor) {}
    public record ContatoRequest(@NotBlank @Size(max=160) String nome,
        String sexo, LocalDate dataNascimento, boolean principal, @Valid List<MeioRequest> meios) {}
    public record Meio(long id, String tipo, String valor) {}
    public record Contato(long id, String nome, String sexo, LocalDate dataNascimento,
        boolean principal, List<Meio> meios) {}
    private long tenant() { return TenantAccess.requireCurrent(); }
    private void funcionario(long id, long empresa) {
        var n = jdbc.queryForObject("select count(*) from funcionarios where id=? and empresa_id=?",
            Integer.class, id, empresa);
        if (n == null || n == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado");
    }
    private long actor(long tenant) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth == null ? java.util.Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
            : usuarios.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant))
            .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("Autor inválido")).getId();
    }
    private void audit(long tenant, long id, String operation, Map<String, ?> values) {
        try {
            jdbc.update("""
                insert into logs_alteracoes
                (empresa_id,tabela_afetada,registro_id,operacao,campos_alterados,valores_novos,
                usuario_responsavel,contexto_operacao,data_alteracao)
                values (?,'rh_funcionario_contatos',?,?,?,?,?,'ADM-004-CONTATOS',now())
                """,tenant,id,operation,json.writeValueAsString(values.keySet()),json.writeValueAsString(values),actor(tenant));
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) { throw new IllegalStateException("Falha na auditoria",ex); }
    }
    @Transactional(readOnly=true)
    public List<Contato> listar(long funcionarioId) {
        long t = tenant(); funcionario(funcionarioId,t);
        return jdbc.query("""
            select id,nome,sexo,data_nascimento,principal from rh_funcionario_contatos
            where empresa_id=? and funcionario_id=? order by principal desc,nome,id
            """,(rs,i)->{
                long id=rs.getLong(1);
                var meios=jdbc.query("select id,tipo,valor from rh_funcionario_contato_meios where contato_id=? order by id",
                    (r,j)-> new Meio(r.getLong(1),r.getString(2),r.getString(3)),id);
                return new Contato(id,rs.getString(2),rs.getString(3),
                    rs.getDate(4)==null?null:rs.getDate(4).toLocalDate(),rs.getBoolean(5),meios);
            },t,funcionarioId);
    }
    private void validate(ContatoRequest request) {
        if(request.nome()==null||request.nome().trim().length()<2)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Nome obrigatório");
        if(request.sexo()!=null&&!request.sexo().isBlank()&&!List.of("M","F","O").contains(request.sexo()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Gênero inválido");
        if(request.dataNascimento()!=null&&request.dataNascimento().isAfter(LocalDate.now()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Nascimento inválido");
        if(request.meios()!=null) for(var meio:request.meios()){
            if(!List.of("EMAIL","WHATSAPP","CELULAR","TELEFONE").contains(meio.tipo())
                    || meio.valor()==null||meio.valor().trim().length()<3)
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Contato inválido");
            if("EMAIL".equals(meio.tipo()) && !meio.valor().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"E-mail inválido");
        }
    }
    @Transactional
    public Contato criar(long funcionarioId, ContatoRequest request) {
        long t=tenant(); funcionario(funcionarioId,t); validate(request);
        // Bloqueia funcionário: evita corrida na troca do contato principal.
        jdbc.queryForObject("select id from funcionarios where id=? and empresa_id=? for update",
            Long.class,funcionarioId,t);
        if(request.principal()) jdbc.update("update rh_funcionario_contatos set principal=false where empresa_id=? and funcionario_id=?",t,funcionarioId);
        Long id=jdbc.queryForObject("""
            insert into rh_funcionario_contatos(empresa_id,funcionario_id,nome,sexo,data_nascimento,principal)
            values (?,?,?,?,?,?) returning id
            """,Long.class,t,funcionarioId,request.nome().trim(),request.sexo(),
                request.dataNascimento(),request.principal());
        if(id==null) throw new IllegalStateException("Contato não gravado");
        salvarMeios(id,request.meios());
        audit(t,id,"INSERT",Map.of("funcionarioId",funcionarioId,"nome",request.nome().trim()));
        return listar(funcionarioId).stream().filter(c->c.id()==id).findFirst().orElseThrow();
    }
    @Transactional
    public Contato atualizar(long funcionarioId,long id,ContatoRequest request) {
        long t=tenant();funcionario(funcionarioId,t);validate(request);
        jdbc.queryForObject("select id from funcionarios where id=? and empresa_id=? for update",Long.class,funcionarioId,t);
        int exists=jdbc.queryForObject("select count(*) from rh_funcionario_contatos where id=? and empresa_id=? and funcionario_id=?",
            Integer.class,id,t,funcionarioId);
        if(exists==0)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Contato não encontrado");
        if(request.principal())jdbc.update("update rh_funcionario_contatos set principal=false where empresa_id=? and funcionario_id=?",t,funcionarioId);
        jdbc.update("""
            update rh_funcionario_contatos set nome=?,sexo=?,data_nascimento=?,principal=?,
            data_atualizacao=now(),versao=versao+1 where id=? and empresa_id=? and funcionario_id=?
            """,request.nome().trim(),request.sexo(),request.dataNascimento(),request.principal(),id,t,funcionarioId);
        jdbc.update("delete from rh_funcionario_contato_meios where contato_id=?",id);
        salvarMeios(id,request.meios());
        audit(t,id,"UPDATE",Map.of("nome",request.nome().trim()));
        return listar(funcionarioId).stream().filter(c->c.id()==id).findFirst().orElseThrow();
    }
    private void salvarMeios(long id,List<MeioRequest> meios) {
        if(meios==null)return;
        for(var m:meios)jdbc.update("insert into rh_funcionario_contato_meios(contato_id,tipo,valor) values (?,?,?)",
            id,m.tipo(),m.valor().trim());
    }
    @Transactional
    public void excluir(long funcionarioId,long id) {
        long t=tenant();funcionario(funcionarioId,t);
        long total=jdbc.queryForObject("select count(*) from rh_funcionario_contatos where id=? and empresa_id=? and funcionario_id=?",
            Long.class,id,t,funcionarioId);
        if(total==0)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Contato não encontrado");
        jdbc.update("delete from rh_funcionario_contatos where id=? and empresa_id=? and funcionario_id=?",id,t,funcionarioId);
        audit(t,id,"DELETE",Map.of("funcionarioId",funcionarioId));
    }
}
