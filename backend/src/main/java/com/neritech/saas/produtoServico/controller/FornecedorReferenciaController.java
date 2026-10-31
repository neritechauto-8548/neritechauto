package com.neritech.saas.produtoServico.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.produtoServico.dto.FornecedorRequest;
import com.neritech.saas.produtoServico.service.FornecedorService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/** Recursos adicionais da interface de fornecedores; cadastro principal permanece na API existente. */
@RestController
@RequestMapping("/v1/fornecedores/referencia")
@PreAuthorize("isAuthenticated()")
public class FornecedorReferenciaController {
    private final JdbcTemplate jdbc;
    private final UsuarioRepository usuarios;
    private final ObjectMapper json;
    private final FornecedorService fornecedores;

    public FornecedorReferenciaController(JdbcTemplate jdbc, UsuarioRepository usuarios,
            ObjectMapper json, FornecedorService fornecedores) {
        this.jdbc=jdbc; this.usuarios=usuarios; this.json=json; this.fornecedores=fornecedores;
    }

    private static void fail(String reason) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }
    private static void missing(String reason) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
    }
    private static String required(String text, int limit, String label) {
        if (text==null || text.isBlank() || text.strip().length()>limit) fail(label+" obrigatório (até "+limit+" caracteres).");
        return text.strip();
    }
    private void fornecedor(Long id) {
        Long tenant=TenantAccess.requireCurrent();
        if (id==null || jdbc.queryForObject("select count(*) from fornecedores where id=? and empresa_id=?",
                Integer.class,id,tenant)!=1) missing("Fornecedor não encontrado.");
    }
    private void contato(Long fornecedorId, Long contatoId) {
        fornecedor(fornecedorId);
        if(contatoId==null || jdbc.queryForObject("""
            select count(*) from contatos_fornecedor
            where id=? and fornecedor_id=? and empresa_id=?
            """, Integer.class,contatoId,fornecedorId,TenantAccess.requireCurrent())!=1)
            missing("Contato não encontrado.");
    }
    private void auditar(String tabela, Long id, String operation, Object data) {
        Long tenant=TenantAccess.requireCurrent();
        var auth=SecurityContextHolder.getContext().getAuthentication();
        var autor=auth==null ? Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
                : usuarios.findByEmailIgnoreCaseAndEmpresaId(auth.getName(),tenant);
        if (autor.isEmpty()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Autor não encontrado nesta empresa");
        try {
            jdbc.update("""
                insert into logs_alteracoes
                (empresa_id,tabela_afetada,registro_id,operacao,campos_alterados,
                 valores_antigos,valores_novos,usuario_responsavel,contexto_operacao,
                 dados_adicionais,data_alteracao)
                values (?,?,?,?,?,?,?,?,?,'{}',current_timestamp)
                """,tenant,tabela,id,operation,"[\"registro\"]","{}",
                json.writeValueAsString(data),autor.get().getId(),"ADM-008");
        } catch(JsonProcessingException e) {
            throw new IllegalStateException("Falha na auditoria de fornecedor", e);
        }
    }

    @GetMapping("/plano-contas")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('PS_LISTAR_FORN') or hasAuthority('FORNECEDOR_VISUALIZAR')")
    public List<Map<String,Object>> planoContas() {
        return jdbc.queryForList("""
            select id,codigo,nome from fin_plano_contas
            where empresa_id=? and ativo=true order by codigo limit 1000
            """,TenantAccess.requireCurrent());
    }

    public record Canal(@NotBlank String tipo, @NotBlank String valor, Boolean whatsapp) {}
    public record Contato(@NotBlank String nome, String sexo, LocalDate dataNascimento,
                          Boolean contatoPrincipal, List<@Valid Canal> meios) {}

    @GetMapping("/{fornecedorId}/contatos")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('PS_LISTAR_FORN') or hasAuthority('FORNECEDOR_VISUALIZAR')")
    public List<Map<String,Object>> contatos(@PathVariable Long fornecedorId) {
        fornecedor(fornecedorId);
        Long tenant=TenantAccess.requireCurrent();
        var rows=jdbc.queryForList("""
            select id,nome,sexo,data_nascimento as "dataNascimento",
                   contato_principal as "contatoPrincipal",ativo,versao
            from contatos_fornecedor where fornecedor_id=? and empresa_id=? and ativo=true order by nome,id
            """,fornecedorId,tenant);
        for(var item:rows){
            item.put("meios",jdbc.queryForList("""
                select id,tipo,valor,whatsapp from meios_contato_fornecedor
                where contato_id=? and empresa_id=? order by id
                """,item.get("id"),tenant));
        }
        return rows;
    }

    @PostMapping("/{fornecedorId}/contatos")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORNECEDOR_CRIAR') or hasAuthority('FORNECEDOR_EDITAR')")
    public Map<String,Object> criarContato(@PathVariable Long fornecedorId,@Valid @RequestBody Contato dto) {
        fornecedor(fornecedorId);
        String nome=required(dto.nome(),255,"Nome");
        if(dto.sexo()!=null && !List.of("MASCULINO","FEMININO","NAO_INFORMADO").contains(dto.sexo()))
            fail("Sexo inválido");
        if(dto.dataNascimento()!=null && dto.dataNascimento().isAfter(LocalDate.now())) fail("Data de nascimento futura.");
        Long tenant=TenantAccess.requireCurrent();
        if(Boolean.TRUE.equals(dto.contatoPrincipal())) jdbc.update("""
            update contatos_fornecedor set contato_principal=false
            where fornecedor_id=? and empresa_id=? and ativo=true
            """,fornecedorId,tenant);
        Long id=jdbc.queryForObject("""
            insert into contatos_fornecedor(empresa_id,fornecedor_id,nome,sexo,data_nascimento,contato_principal)
            values (?,?,?,?,?,?) returning id
            """,Long.class,tenant,fornecedorId,nome,dto.sexo(),dto.dataNascimento(),
                Boolean.TRUE.equals(dto.contatoPrincipal()));
        if(dto.meios()!=null) {
            if(dto.meios().size()>20) fail("Limite de 20 meios de contato.");
            for(Canal meio:dto.meios()) salvarCanal(id,meio);
        }
        auditar("contatos_fornecedor",id,"INSERT",Map.of("nome",nome,"fornecedorId",fornecedorId));
        return Map.of("id",id,"nome",nome);
    }

    private void salvarCanal(Long id,Canal meio) {
        String tipo=required(meio.tipo(),20,"Tipo de contato").toUpperCase(Locale.ROOT);
        String valor=required(meio.valor(),255,"Valor de contato");
        if(!List.of("EMAIL","CELULAR","TELEFONE").contains(tipo)) fail("Tipo de contato inválido.");
        if("EMAIL".equals(tipo) && !valor.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) fail("E-mail inválido.");
        if(!"EMAIL".equals(tipo) && !valor.matches("[+0-9() .-]{8,25}")) fail("Telefone inválido.");
        jdbc.update("""
            insert into meios_contato_fornecedor(empresa_id,contato_id,tipo,valor,whatsapp)
            values (?,?,?,?,?)
            """,TenantAccess.requireCurrent(),id,tipo,valor,
                "CELULAR".equals(tipo) && Boolean.TRUE.equals(meio.whatsapp()));
    }

    @PutMapping("/{fornecedorId}/contatos/{contatoId}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORNECEDOR_EDITAR')")
    public Map<String,Object> editarContato(@PathVariable Long fornecedorId,@PathVariable Long contatoId,
            @Valid @RequestBody Contato dto) {
        contato(fornecedorId,contatoId);
        String nome=required(dto.nome(),255,"Nome");
        if(dto.sexo()!=null&&!List.of("MASCULINO","FEMININO","NAO_INFORMADO").contains(dto.sexo()))fail("Sexo inválido.");
        if(dto.dataNascimento()!=null&&dto.dataNascimento().isAfter(LocalDate.now()))fail("Data futura");
        Long tenant=TenantAccess.requireCurrent();
        if(Boolean.TRUE.equals(dto.contatoPrincipal()))jdbc.update("""
            update contatos_fornecedor set contato_principal=false
            where fornecedor_id=? and empresa_id=? and id<>?
            """,fornecedorId,tenant,contatoId);
        jdbc.update("""
            update contatos_fornecedor set nome=?,sexo=?,data_nascimento=?,contato_principal=?,
             versao=versao+1,data_atualizacao=current_timestamp
            where id=? and fornecedor_id=? and empresa_id=?
            """,nome,dto.sexo(),dto.dataNascimento(),Boolean.TRUE.equals(dto.contatoPrincipal()),
                contatoId,fornecedorId,tenant);
        if(dto.meios()!=null){
            if(dto.meios().size()>20)fail("Limite de 20 meios de contato.");
            jdbc.update("delete from meios_contato_fornecedor where contato_id=? and empresa_id=?",contatoId,tenant);
            for(Canal meio:dto.meios())salvarCanal(contatoId,meio);
        }
        auditar("contatos_fornecedor",contatoId,"UPDATE",Map.of("nome",nome,"fornecedorId",fornecedorId));
        return Map.of("id",contatoId,"nome",nome);
    }

    @DeleteMapping("/{fornecedorId}/contatos/{contatoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORNECEDOR_EDITAR')")
    public void desativarContato(@PathVariable Long fornecedorId,@PathVariable Long contatoId){
        contato(fornecedorId,contatoId);
        jdbc.update("""
            update contatos_fornecedor set ativo=false,contato_principal=false,versao=versao+1
            where id=? and fornecedor_id=? and empresa_id=?
            """,contatoId,fornecedorId,TenantAccess.requireCurrent());
        auditar("contatos_fornecedor",contatoId,"UPDATE",Map.of("ativo",false));
    }

    @GetMapping("/{fornecedorId}/devolucoes")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('PS_LISTAR_FORN') or hasAuthority('FORNECEDOR_VISUALIZAR')")
    public List<Map<String,Object>> devolucoes(@PathVariable Long fornecedorId,
        @RequestParam(required=false) String busca,@RequestParam(defaultValue="desc") String ordem){
        fornecedor(fornecedorId);
        if(busca!=null&&busca.length()>100)fail("Filtro muito longo");
        String sort="asc".equalsIgnoreCase(ordem)?"asc":"desc";
        return jdbc.queryForList("""
            select d.id, e.nome_fantasia as empresa,
              p.numero_pedido as compra, coalesce(pr.nome,'') as descricao,
              d.chave_nfe as "chaveNfe", d.quantidade, d.valor
            from itens_devolvidos_fornecedor d
            join pedidos_fornecedor p on p.id=d.pedido_id and p.empresa_id=d.empresa_id
              and p.fornecedor_id=d.fornecedor_id
            join empresa e on e.id=d.empresa_id
            join produtos pr on pr.id=d.produto_id
            where d.empresa_id=? and d.fornecedor_id=?
              and (?='' or cast(p.numero_pedido as text) like '%' || ? || '%')
            order by d.id """+sort+" limit 1000",
            TenantAccess.requireCurrent(),fornecedorId, busca==null?"":busca, busca==null?"":busca);
    }

    @GetMapping("/{fornecedorId}/creditos")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('PS_LISTAR_FORN') or hasAuthority('FORNECEDOR_VISUALIZAR')")
    public List<Map<String,Object>> creditos(@PathVariable Long fornecedorId,
       @RequestParam(required=false) String busca,@RequestParam(defaultValue="desc") String ordem){
        fornecedor(fornecedorId);
        if(busca!=null&&busca.length()>100)fail("Filtro muito longo");
        String sort="asc".equalsIgnoreCase(ordem)?"asc":"desc";
        return jdbc.queryForList("""
            select c.id, e.nome_fantasia as empresa,
              p.numero_pedido as "codigoCompra", c.numero_documento as "numeroDocumento",
              c.lancamento_referencia as "lancamentoReferencia",
              pc.nome as conta,c.valor
            from creditos_fornecedor c
            left join pedidos_fornecedor p on p.id=c.pedido_id and p.empresa_id=c.empresa_id
              and p.fornecedor_id=c.fornecedor_id
            left join fin_plano_contas pc on pc.id=c.conta_id and pc.empresa_id=c.empresa_id
            join empresa e on e.id=c.empresa_id
            where c.empresa_id=? and c.fornecedor_id=?
              and (?='' or cast(p.numero_pedido as text) like '%' || ? || '%')
            order by c.id """+sort+" limit 1000",
            TenantAccess.requireCurrent(),fornecedorId,busca==null?"":busca,busca==null?"":busca);
    }

    public record LinhaImportacao(String tipoPessoa,String cpf,String cnpj,String razaoSocial,
                                   String nomeFantasia,String email,String classificacao) {}
    @PostMapping("/importar")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('PS_INC_FORN') or hasAuthority('FORNECEDOR_CRIAR')")
    public Map<String,Integer> importar(@RequestBody List<LinhaImportacao> linhas) {
        if(linhas==null||linhas.isEmpty()||linhas.size()>500)fail("Importe de 1 a 500 fornecedores.");
        Set<String> documentos=new HashSet<>();
        for(LinhaImportacao l:linhas){
            if(l==null || !List.of("FISICA","JURIDICA").contains(l.tipoPessoa()))fail("Tipo de pessoa inválido.");
            required(l.razaoSocial(),255,"Nome/Razão social");
            String documento="FISICA".equals(l.tipoPessoa())?l.cpf():l.cnpj();
            if(documento!=null&&!documento.isBlank()&&!documentos.add(documento.replaceAll("[^A-Za-z0-9]","").toUpperCase(Locale.ROOT)))
                fail("Documento duplicado no arquivo");
            if(l.classificacao()!=null&&!List.of("OUTROS","A","B","C","D","E").contains(l.classificacao()))fail("Classificação inválida");
        }
        Long tenant=TenantAccess.requireCurrent();
        int count=0;
        for(LinhaImportacao l:linhas){
            Map<String,Object> data=new LinkedHashMap<>();
            data.put("empresaId",tenant);
            data.put("tipoPessoa",l.tipoPessoa());
            data.put("cpf",l.cpf());data.put("cnpj",l.cnpj());
            data.put("razaoSocial",l.razaoSocial().trim());
            data.put("nomeFantasia",l.nomeFantasia()==null||l.nomeFantasia().isBlank()?l.razaoSocial().trim():l.nomeFantasia().trim());
            data.put("emailPrincipal",l.email());
            data.put("classificacao",l.classificacao()==null||l.classificacao().isBlank()?"OUTROS":l.classificacao());
            data.put("ativo",true);
            fornecedores.create(json.convertValue(data,FornecedorRequest.class));
            count++;
        }
        return Map.of("importados",count);
    }
}
