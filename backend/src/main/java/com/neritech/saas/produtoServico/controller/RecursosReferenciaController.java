package com.neritech.saas.produtoServico.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.empresa.dto.DepartamentoContabioRequest;
import com.neritech.saas.empresa.service.DepartamentoContabioService;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.produtoServico.dto.ServicoRequest;
import com.neritech.saas.produtoServico.service.ServicoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Interface de referência de Gestão de Recursos. Aproveita as áreas e serviços já
 * utilizados por OS/RH e adiciona apenas os vínculos que faltavam. Não usa mocks.
 */
@RestController
@RequestMapping("/v1/recursos")
@PreAuthorize("isAuthenticated()")
public class RecursosReferenciaController {
    private final JdbcTemplate jdbc;
    private final ServicoService servicos;
    private final DepartamentoContabioService areas;
    private final UsuarioRepository usuarios;
    private final ObjectMapper mapper;

    public RecursosReferenciaController(JdbcTemplate jdbc, ServicoService servicos,
            DepartamentoContabioService areas, UsuarioRepository usuarios, ObjectMapper mapper) {
        this.jdbc = jdbc;
        this.servicos = servicos;
        this.areas = areas;
        this.usuarios = usuarios;
        this.mapper = mapper;
    }

    private static void invalid(String message) { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, message); }
    private static void conflict(String message) { throw new ResponseStatusException(HttpStatus.CONFLICT, message); }
    private static void missing(String message) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, message); }
    private static String description(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().length() > 200) invalid("Descrição obrigatória (até 200 caracteres).");
        return value.trim();
    }
    private static BigDecimal nonnegative(BigDecimal value, String field) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) invalid(field + " deve ser maior ou igual a zero.");
        return value.setScale(2, RoundingMode.HALF_UP);
    }
    private static int checkedNonnegative(Integer value, String field) {
        if (value == null || value < 0) invalid(field + " inválido.");
        return value;
    }
    private void assertServico(Long id) {
        if (id == null || jdbc.queryForObject("select count(*) from servicos where id=? and empresa_id=?", Integer.class,
                id, TenantAccess.requireCurrent()) == 0) missing("Serviço não encontrado.");
    }
    private void assertArea(Long id) {
        if (id == null || jdbc.queryForObject("select count(*) from departamentos_contabio where id=? and empresa_id=?", Integer.class,
                id, TenantAccess.requireCurrent()) == 0) missing("Área não encontrada.");
    }
    private void audit(String tabela, Long id, String operacao, Object dados) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var actor = auth == null ? Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
                : usuarios.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), TenantAccess.requireCurrent());
        if (actor.isEmpty()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário autenticado não identificado.");
        try {
            jdbc.update("""
                insert into logs_alteracoes
                    (empresa_id, tabela_afetada, registro_id, operacao, campos_alterados,
                     valores_novos, usuario_responsavel, contexto_operacao, data_alteracao)
                values (?, ?, ?, ?, '[]', ?, ?, 'GESTAO_RECURSOS', current_timestamp)
                """, TenantAccess.requireCurrent(), tabela, id, operacao,
                    mapper.writeValueAsString(dados), actor.get().getId());
        } catch (JsonProcessingException e) { throw new IllegalStateException("Auditoria indisponível.", e); }
    }

    // ----- Áreas e subáreas -------------------------------------------------
    public record SubareaInput(Long id, String descricao, Boolean ativo) {}
    public record AreaInput(String descricao, Boolean ativo, Integer versao, List<SubareaInput> subareas) {}

    @GetMapping("/areas")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_LISTAR_SERV') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public List<Map<String,Object>> listarAreas(@RequestParam(required = false) String busca,
            @RequestParam(required = false) Boolean ativo) {
        return jdbc.queryForList("""
            select id, descricao, ativo, versao from departamentos_contabio
             where empresa_id=? and (cast(? as boolean) is null or ativo=?)
               and (cast(? as varchar) is null or lower(descricao) like lower('%' || ? || '%'))
             order by descricao, id limit 1000
            """, TenantAccess.requireCurrent(), ativo, ativo, busca, busca);
    }

    @GetMapping("/areas/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_LISTAR_SERV') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public Map<String,Object> obterArea(@PathVariable Long id) {
        assertArea(id);
        Map<String,Object> area = new LinkedHashMap<>(jdbc.queryForMap(
                "select id, descricao, ativo, versao from departamentos_contabio where id=? and empresa_id=?",
                id, TenantAccess.requireCurrent()));
        area.put("subareas", jdbc.queryForList("""
            select id, descricao, ativo from subareas_oficina
             where area_id=? and empresa_id=? order by id
            """, id, TenantAccess.requireCurrent()));
        return area;
    }

    @PostMapping("/areas")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public Map<String,Object> criarArea(@RequestBody AreaInput input) {
        String desc = description(input.descricao());
        Long tenant = TenantAccess.requireCurrent();
        if (jdbc.queryForObject("select count(*) from departamentos_contabio where empresa_id=? and lower(descricao)=lower(?)",
                Integer.class, tenant, desc) > 0) conflict("Área já cadastrada.");
        String codigo = "AREA-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase(Locale.ROOT);
        Long id = jdbc.queryForObject("""
            insert into departamentos_contabio (empresa_id, codigo, descricao, ativo, ordem, versao, data_cadastro)
            values (?, ?, ?, true, 0, 0, current_timestamp) returning id
            """, Long.class, tenant, codigo, desc);
        salvarSubareas(id, input.subareas());
        audit("departamentos_contabio", id, "INSERT", Map.of("descricao", desc));
        return obterArea(id);
    }

    @PutMapping("/areas/{id}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public Map<String,Object> atualizarArea(@PathVariable Long id, @RequestBody AreaInput input) {
        assertArea(id);
        if (input.versao() == null) conflict("Recarregue a área antes de editar.");
        String desc = description(input.descricao());
        Long tenant = TenantAccess.requireCurrent();
        if (jdbc.queryForObject("""
            select count(*) from departamentos_contabio
            where empresa_id=? and id<>? and lower(descricao)=lower(?)
            """, Integer.class, tenant, id, desc) > 0) conflict("Área já cadastrada.");
        int changed = jdbc.update("""
            update departamentos_contabio set descricao=?, ativo=?, versao=versao+1,
                data_atualizacao=current_timestamp where id=? and empresa_id=? and versao=?
            """, desc, !Boolean.FALSE.equals(input.ativo()), id, tenant, input.versao());
        if (changed != 1) conflict("Esta área foi alterada por outro usuário.");
        salvarSubareas(id, input.subareas());
        audit("departamentos_contabio", id, "UPDATE", Map.of("descricao", desc));
        return obterArea(id);
    }

    private void salvarSubareas(Long id, List<SubareaInput> subareas) {
        if (subareas == null) return;
        Long tenant = TenantAccess.requireCurrent();
        Set<Long> recebidos = new HashSet<>();
        Set<String> nomes = new HashSet<>();
        for (SubareaInput item : subareas) {
            String desc = description(item.descricao());
            if (!nomes.add(desc.toLowerCase(Locale.ROOT))) conflict("Subárea repetida: " + desc);
            if (item.id() == null) {
                Long subId = jdbc.queryForObject("""
                    insert into subareas_oficina (empresa_id, area_id, descricao, ativo)
                    values (?, ?, ?, ?) returning id
                    """, Long.class, tenant, id, desc, !Boolean.FALSE.equals(item.ativo()));
                audit("subareas_oficina", subId, "INSERT", Map.of("descricao", desc));
                recebidos.add(subId);
            } else {
                int changed = jdbc.update("""
                    update subareas_oficina set descricao=?, ativo=?, data_atualizacao=current_timestamp,
                        versao=versao+1 where id=? and area_id=? and empresa_id=?
                    """, desc, !Boolean.FALSE.equals(item.ativo()), item.id(), id, tenant);
                if (changed != 1) missing("Subárea não encontrada na área informada.");
                audit("subareas_oficina", item.id(), "UPDATE", Map.of("descricao", desc));
                recebidos.add(item.id());
            }
        }
        // Subáreas omitidas em uma edição são inativadas, jamais removidas fisicamente.
        for (Map<String,Object> existente : jdbc.queryForList(
                "select id from subareas_oficina where empresa_id=? and area_id=? and ativo", tenant, id)) {
            Long subId = ((Number) existente.get("id")).longValue();
            if (!recebidos.contains(subId)) {
                jdbc.update("update subareas_oficina set ativo=false where id=? and empresa_id=?", subId, tenant);
                audit("subareas_oficina", subId, "UPDATE", Map.of("ativo", false));
            }
        }
    }

    @DeleteMapping("/areas/{id}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public void desativarArea(@PathVariable Long id) {
        assertArea(id);
        jdbc.update("update departamentos_contabio set ativo=false, versao=versao+1 where id=? and empresa_id=?",
                id, TenantAccess.requireCurrent());
        audit("departamentos_contabio", id, "UPDATE", Map.of("ativo", false));
    }

    // ----- Serviços, preços e aplicação -----------------------------------
    public record ServicoInput(String descricao, String codigoTempario, Long areaId, String unidade,
            BigDecimal tempo, Integer vidaUtilDias, Integer kmManutencao, String nbs, String observacao,
            BigDecimal preco, BigDecimal custoFixo, BigDecimal comissao, Boolean ativo) {}

    @GetMapping("/servicos")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_LISTAR_SERV')")
    public List<Map<String,Object>> listarServicos(@RequestParam(required = false) String busca,
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) Long areaId, @RequestParam(required = false) Boolean ativo) {
        return jdbc.queryForList("""
            select s.id, s.nome as descricao, s.codigo as "codigoTempario",
                s.area_id as "areaId", coalesce(a.descricao,'Outros') as area,
                s.unidade, s.tempo_previsto_horas as tempo,
                s.vida_util_dias as "vidaUtilDias", s.km_manutencao as "kmManutencao",
                s.nbs, s.instrucoes_execucao as observacao,
                s.ativo, s.versao,
                coalesce(p.preco, s.preco_base, 0) as preco,
                coalesce(p.custo_fixo, s.custo, 0) as "custoFixo",
                coalesce(p.comissao_percentual, 0) as comissao
            from servicos s
            left join departamentos_contabio a on a.id=s.area_id and a.empresa_id=s.empresa_id
            left join precos_servico_empresa p on p.servico_id=s.id and p.empresa_id=s.empresa_id
            where s.empresa_id=?
              and (cast(? as varchar) is null or lower(s.nome) like lower('%' || ? || '%'))
              and (cast(? as varchar) is null or lower(coalesce(s.codigo,'')) like lower('%' || ? || '%'))
              and (cast(? as bigint) is null or s.area_id=?)
              and (cast(? as boolean) is null or s.ativo=?)
            order by s.nome, s.id limit 1000
            """, TenantAccess.requireCurrent(), busca, busca, codigo, codigo, areaId, areaId, ativo, ativo);
    }

    @GetMapping("/servicos/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_LISTAR_SERV')")
    public Map<String,Object> obterServico(@PathVariable Long id) {
        assertServico(id);
        return jdbc.queryForMap("""
            select s.id, s.nome as descricao, s.codigo as "codigoTempario",
                s.area_id as "areaId", coalesce(a.descricao,'Outros') as area,
                s.unidade, s.tempo_previsto_horas as tempo,
                s.vida_util_dias as "vidaUtilDias", s.km_manutencao as "kmManutencao",
                s.nbs, s.instrucoes_execucao as observacao, s.ativo, s.versao,
                coalesce(p.preco,s.preco_base,0) as preco,
                coalesce(p.custo_fixo,s.custo,0) as "custoFixo",
                coalesce(p.comissao_percentual,0) as comissao
            from servicos s
            left join departamentos_contabio a on a.id=s.area_id and a.empresa_id=s.empresa_id
            left join precos_servico_empresa p on p.servico_id=s.id and p.empresa_id=s.empresa_id
            where s.id=? and s.empresa_id=?
            """, id, TenantAccess.requireCurrent());
    }

    private ServicoRequest toRequest(ServicoInput input) {
        if (input == null) invalid("Dados de serviço obrigatórios.");
        String desc = description(input.descricao());
        assertArea(input.areaId());
        if (input.unidade() == null || input.unidade().isBlank()) invalid("Unidade obrigatória.");
        if (input.tempo() == null || input.tempo().compareTo(BigDecimal.ZERO) <= 0) invalid("Tempo deve ser maior que zero.");
        if (input.comissao() != null && (input.comissao().compareTo(BigDecimal.ZERO)<0 || input.comissao().compareTo(new BigDecimal("100"))>0))
            invalid("Comissão deve estar entre 0 e 100%.");
        return new ServicoRequest(input.codigoTempario(), desc, input.areaId(), input.unidade(),
                input.tempo(), nonnegative(input.preco(), "Preço"), nonnegative(input.custoFixo(), "Custo fixo"),
                null, null, input.observacao(), !Boolean.FALSE.equals(input.ativo()));
    }
    private void complementosServico(Long id, ServicoInput input) {
        if (input.vidaUtilDias()!=null) checkedNonnegative(input.vidaUtilDias(),"Vida útil");
        if (input.kmManutencao()!=null) checkedNonnegative(input.kmManutencao(),"Km manutenção");
        if (input.nbs()!=null && input.nbs().length()>30) invalid("NBS deve ter até 30 caracteres.");
        jdbc.update("""
            update servicos set vida_util_dias=?, km_manutencao=?, nbs=? where id=? and empresa_id=?
            """, input.vidaUtilDias(), input.kmManutencao(), input.nbs(), id, TenantAccess.requireCurrent());
        jdbc.update("""
            insert into precos_servico_empresa (empresa_id, servico_id, preco, custo_fixo, comissao_percentual)
            values (?, ?, ?, ?, ?)
            on conflict (empresa_id, servico_id)
            do update set preco=excluded.preco, custo_fixo=excluded.custo_fixo,
                comissao_percentual=excluded.comissao_percentual,
                data_atualizacao=current_timestamp, versao=precos_servico_empresa.versao+1
            """, TenantAccess.requireCurrent(), id, nonnegative(input.preco(),"Preço"),
                nonnegative(input.custoFixo(),"Custo fixo"),
                nonnegative(input.comissao()==null ? BigDecimal.ZERO : input.comissao(),"Comissão"));
    }

    @PostMapping("/servicos")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_INC_SERV')")
    public Map<String,Object> criarServico(@RequestBody ServicoInput input) {
        ServicoRequest req=toRequest(input);
        Long id=servicos.create(req).id();
        complementosServico(id,input);
        audit("servicos", id, "INSERT", Map.of("descricao", req.nome()));
        return obterServico(id);
    }

    @PutMapping("/servicos/{id}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_EDIT_SERV')")
    public Map<String,Object> atualizarServico(@PathVariable Long id,@RequestBody ServicoInput input) {
        assertServico(id);
        ServicoRequest req=toRequest(input);
        servicos.update(id,req);
        complementosServico(id,input);
        audit("servicos", id, "UPDATE", Map.of("descricao", req.nome()));
        return obterServico(id);
    }

    @DeleteMapping("/servicos/{id}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_EXC_SERV')")
    public void desativarServico(@PathVariable Long id) {
        assertServico(id);
        servicos.delete(id);
        audit("servicos", id, "UPDATE", Map.of("ativo", false));
    }

    public record PrecosLote(String operacao, BigDecimal valor, Long areaId) {}
    @PostMapping("/servicos/precos-lote")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_EDIT_SERV')")
    public Map<String,Integer> precosEmLote(@RequestBody PrecosLote input) {
        BigDecimal valor=nonnegative(input.valor(),"Valor");
        if (!List.of("SOMAR_VALOR","SUBSTITUIR_VALOR","SOMAR_PERCENTUAL").contains(input.operacao()))
            invalid("Operação de preços inválida.");
        if (input.areaId()!=null) assertArea(input.areaId());
        Long tenant=TenantAccess.requireCurrent();
        var rows=jdbc.queryForList("""
            select s.id, coalesce(p.preco,s.preco_base,0) as preco
            from servicos s left join precos_servico_empresa p
            on p.servico_id=s.id and p.empresa_id=s.empresa_id
            where s.empresa_id=? and s.ativo=true and (cast(? as bigint) is null or s.area_id=?)
            """,tenant,input.areaId(),input.areaId());
        for (var row:rows) {
            Long id=((Number)row.get("id")).longValue();
            BigDecimal atual=(BigDecimal)row.get("preco");
            BigDecimal novo=switch(input.operacao()) {
                case "SUBSTITUIR_VALOR" -> valor;
                case "SOMAR_PERCENTUAL" -> atual.multiply(BigDecimal.ONE.add(valor.divide(new BigDecimal("100"),8,RoundingMode.HALF_UP)));
                default -> atual.add(valor);
            };
            novo=novo.setScale(2,RoundingMode.HALF_UP);
            jdbc.update("update servicos set preco_base=? where id=? and empresa_id=?",novo,id,tenant);
            jdbc.update("""
                insert into precos_servico_empresa (empresa_id,servico_id,preco,custo_fixo,comissao_percentual)
                values (?,?,?,0,0) on conflict (empresa_id,servico_id) do update
                set preco=excluded.preco, data_atualizacao=current_timestamp, versao=precos_servico_empresa.versao+1
                """,tenant,id,novo);
            audit("precos_servico_empresa",id,"UPDATE",Map.of("preco",novo));
        }
        return Map.of("alterados",rows.size());
    }

    public record ImportacaoLinha(String codigo, String descricao, Long areaId,
                                  String unidade, BigDecimal tempo, BigDecimal preco) {}
    @PostMapping("/servicos/importar")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_INC_SERV')")
    public Map<String,Integer> importarServicos(@RequestBody List<ImportacaoLinha> linhas) {
        if (linhas==null || linhas.isEmpty() || linhas.size()>1000) invalid("Informe de 1 a 1000 serviços.");
        Set<String> codigos=new HashSet<>();
        int total=0;
        for (var l:linhas) {
            String codigo=l.codigo()==null?"":l.codigo().trim();
            if (codigo.isBlank() || codigo.length()>50 || !codigos.add(codigo.toLowerCase(Locale.ROOT)))
                invalid("Código de serviço obrigatório e único no arquivo.");
            if (l.unidade()==null || l.unidade().length()>5) invalid("Unidade inválida (até 5 caracteres).");
            criarServico(new ServicoInput(l.descricao(),codigo,l.areaId(),l.unidade(),
                    l.tempo()==null ? BigDecimal.ONE:l.tempo(),null,null,null,null,
                    l.preco()==null ? BigDecimal.ZERO:l.preco(),BigDecimal.ZERO,BigDecimal.ZERO,true));
            total++;
        }
        return Map.of("importados",total);
    }

    @GetMapping("/marcas")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_LISTAR_SERV')")
    public List<Map<String,Object>> marcas() {
        return jdbc.queryForList("""
            select id,nome from marcas_veiculos
            where empresa_id is null or empresa_id=? order by nome limit 500
            """, TenantAccess.requireCurrent());
    }
    @GetMapping("/modelos")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_LISTAR_SERV')")
    public List<Map<String,Object>> modelos(@RequestParam Long marcaId) {
        return jdbc.queryForList("""
            select m.id,m.nome from modelos_veiculos m
            join marcas_veiculos a on a.id=m.marca_id
            where m.marca_id=? and (m.empresa_id is null or m.empresa_id=?)
              and (a.empresa_id is null or a.empresa_id=?)
            order by m.nome limit 1000
            """, marcaId,TenantAccess.requireCurrent(),TenantAccess.requireCurrent());
    }
    @GetMapping("/servicos/{id}/aplicacoes")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_LISTAR_SERV')")
    public List<Map<String,Object>> aplicacoes(@PathVariable Long id) {
        assertServico(id);
        return jdbc.queryForList("""
            select a.id, a.marca_id as "marcaId", m.nome as marca,
                a.modelo_id as "modeloId", mo.nome as modelo, a.tempo
            from aplicacoes_servico_modelo a
            join marcas_veiculos m on m.id=a.marca_id
            join modelos_veiculos mo on mo.id=a.modelo_id
            where a.empresa_id=? and a.servico_id=? order by m.nome,mo.nome
            """,TenantAccess.requireCurrent(),id);
    }
    public record AplicacaoInput(Long marcaId, Long modeloId, BigDecimal tempo) {}
    @PostMapping("/servicos/{id}/aplicacoes")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_EDIT_SERV')")
    public void adicionarAplicacao(@PathVariable Long id,@RequestBody AplicacaoInput in) {
        assertServico(id);
        if(in.tempo()==null || in.tempo().compareTo(BigDecimal.ZERO)<=0) invalid("Tempo inválido.");
        if(jdbc.queryForObject("""
            select count(*) from modelos_veiculos mo join marcas_veiculos ma on ma.id=mo.marca_id
            where mo.id=? and ma.id=? and (mo.empresa_id is null or mo.empresa_id=?)
            and (ma.empresa_id is null or ma.empresa_id=?)
            """,Integer.class,in.modeloId(),in.marcaId(),TenantAccess.requireCurrent(),TenantAccess.requireCurrent())!=1)
            invalid("Modelo e montadora não correspondem.");
        Long appId=jdbc.queryForObject("""
            insert into aplicacoes_servico_modelo(empresa_id,servico_id,marca_id,modelo_id,tempo)
            values (?,?,?,?,?) returning id
            """,Long.class,TenantAccess.requireCurrent(),id,in.marcaId(),in.modeloId(),in.tempo());
        audit("aplicacoes_servico_modelo",appId,"INSERT",Map.of("servicoId",id));
    }
    @PutMapping("/servicos/{id}/aplicacoes/{aplicacaoId}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_EDIT_SERV')")
    public void atualizarAplicacao(@PathVariable Long id,@PathVariable Long aplicacaoId,@RequestBody AplicacaoInput in) {
        assertServico(id);
        if(in.tempo()==null || in.tempo().compareTo(BigDecimal.ZERO)<=0) invalid("Tempo inválido.");
        if(jdbc.update("""
            update aplicacoes_servico_modelo set tempo=? where id=? and servico_id=? and empresa_id=?
            """,in.tempo(),aplicacaoId,id,TenantAccess.requireCurrent())!=1) missing("Aplicação não encontrada.");
        audit("aplicacoes_servico_modelo",aplicacaoId,"UPDATE",Map.of("tempo",in.tempo()));
    }
    @DeleteMapping("/servicos/{id}/aplicacoes/{aplicacaoId}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_EDIT_SERV')")
    public void removerAplicacao(@PathVariable Long id,@PathVariable Long aplicacaoId) {
        assertServico(id);
        if(jdbc.update("delete from aplicacoes_servico_modelo where id=? and servico_id=? and empresa_id=?",
                aplicacaoId,id,TenantAccess.requireCurrent())!=1) missing("Aplicação não encontrada.");
        audit("aplicacoes_servico_modelo",aplicacaoId,"DELETE",Map.of("servicoId",id));
    }

    // ----- Acessórios de conferência --------------------------------------
    public record AcessorioInput(String descricao, Boolean checklist, Boolean padrao, Boolean ativo) {}
    @GetMapping("/acessorios")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PS_LISTAR_SERV') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public List<Map<String,Object>> listarAcessorios(@RequestParam(required = false) String busca,
           @RequestParam(required = false) Boolean ativo,
           @RequestParam(required = false) Boolean checklist) {
        return jdbc.queryForList("""
            select id,descricao,checklist,padrao,ativo,versao from acessorios_veiculo
            where empresa_id=? and (cast(? as boolean) is null or ativo=?) and (cast(? as boolean) is null or checklist=?)
              and (cast(? as varchar) is null or lower(descricao) like lower('%'||?||'%'))
            order by descricao,id limit 1000
            """,TenantAccess.requireCurrent(),ativo,ativo,checklist,checklist,busca,busca);
    }
    private void validateAcessorio(AcessorioInput in,Long currentId) {
        String desc=description(in.descricao());
        if(jdbc.queryForObject("""
            select count(*) from acessorios_veiculo
            where empresa_id=? and lower(descricao)=lower(?) and ativo and id<>?
            """,Integer.class,TenantAccess.requireCurrent(),desc,currentId==null?-1:currentId)>0)
            conflict("Descrição já cadastrada. Evite itens duplicados no checklist.");
    }
    @PostMapping("/acessorios")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public Map<String,Object> criarAcessorio(@RequestBody AcessorioInput in) {
        validateAcessorio(in,null);
        Long id=jdbc.queryForObject("""
            insert into acessorios_veiculo(empresa_id,descricao,checklist,padrao,ativo)
            values(?,?,?,?,true) returning id
            """,Long.class,TenantAccess.requireCurrent(),description(in.descricao()),
            Boolean.TRUE.equals(in.checklist()),false);
        audit("acessorios_veiculo",id,"INSERT",Map.of("descricao",in.descricao()));
        return jdbc.queryForMap("select id,descricao,checklist,padrao,ativo,versao from acessorios_veiculo where id=? and empresa_id=?",
                id,TenantAccess.requireCurrent());
    }
    @PutMapping("/acessorios/{id}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public Map<String,Object> atualizarAcessorio(@PathVariable Long id,@RequestBody AcessorioInput in) {
        if(jdbc.queryForObject("select count(*) from acessorios_veiculo where id=? and empresa_id=?",
                Integer.class,id,TenantAccess.requireCurrent())!=1) missing("Acessório não encontrado.");
        validateAcessorio(in,id);
        jdbc.update("""
            update acessorios_veiculo set descricao=?,checklist=?,ativo=?,
                data_atualizacao=current_timestamp,versao=versao+1 where id=? and empresa_id=?
            """,description(in.descricao()),Boolean.TRUE.equals(in.checklist()),
                !Boolean.FALSE.equals(in.ativo()),id,TenantAccess.requireCurrent());
        audit("acessorios_veiculo",id,"UPDATE",Map.of("descricao",in.descricao()));
        return jdbc.queryForMap("select id,descricao,checklist,padrao,ativo,versao from acessorios_veiculo where id=? and empresa_id=?",
                id,TenantAccess.requireCurrent());
    }
    @DeleteMapping("/acessorios/{id}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public void desativarAcessorio(@PathVariable Long id) {
        if(jdbc.update("""
            update acessorios_veiculo set ativo=false,versao=versao+1 where id=? and empresa_id=?
            """,id,TenantAccess.requireCurrent())!=1) missing("Acessório não encontrado.");
        audit("acessorios_veiculo",id,"UPDATE",Map.of("ativo",false));
    }
}
