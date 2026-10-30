package com.neritech.saas.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import com.neritech.saas.gestaoUsuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Layout pessoal da Home. Nenhuma chave de empresa ou usuario e recebida do cliente. */
@RestController
@RequestMapping({"/api/dashboard/configuracao", "/dashboard/configuracao"})
@RequiredArgsConstructor
public class DashboardConfiguracaoController {
    private final JdbcTemplate jdbc;
    private final UsuarioService usuarioService;
    private final ObjectMapper mapper;

    private static final Set<String> COMPONENTES = Set.of(
        "ordens", "financeiro-os", "gerencial", "padrao", "faturamento-geral",
        "ticket-medio", "faturamento-passagem", "fluxo-caixa", "custo-vendas",
        "despesas-receitas", "faturamento-despesas", "comparativo-seguradoras",
        "custo-fixo-variavel", "pecas-servicos", "passagem-veiculos",
        "desempenho-equipe", "clientes-sem-movimentacao");
    private static final Set<String> TIPOS = Set.of("grafico","indicador","listagem");
    private static final List<WidgetConfig> PADRAO = List.of(
        new WidgetConfig("financeiro-os","listagem",1),
        new WidgetConfig("ordens","indicador",1));

    public record WidgetConfig(String id, String tipo, Integer colunas) {}
    public record Configuracao(List<WidgetConfig> componentes) {}

    private record Escopo(Long empresaId, Long usuarioId) {}

    private Escopo escopo() {
        long empresaId = TenantAccess.requireCurrent();
        UsuarioResponse usuario = usuarioService.getCurrentUser();
        if (usuario == null || usuario.getId() == null || usuario.getEmpresaId() == null
            || !usuario.getEmpresaId().equals(empresaId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Usuario sem vinculo com a empresa atual");
        }
        return new Escopo(empresaId, usuario.getId());
    }

    @GetMapping
    @Transactional(readOnly = true)
    public Configuracao consultar() {
        Escopo e = escopo();
        List<String> encontrados = jdbc.query(
            "SELECT configuracao FROM dashboard_componentes_usuario WHERE empresa_id=? AND usuario_id=?",
            (rs, row) -> rs.getString(1), e.empresaId(), e.usuarioId());
        if (encontrados.isEmpty()) return new Configuracao(PADRAO);
        try {
            List<WidgetConfig> items = mapper.readValue(encontrados.get(0), new TypeReference<>() {});
            validar(items);
            return new Configuracao(items);
        } catch (JsonProcessingException | ResponseStatusException exc) {
            // Nao ocultar corrupcao de layout substituindo dados silenciosamente.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Configuracao do painel invalida", exc);
        }
    }

    @PutMapping
    @Transactional
    public Configuracao salvar(@RequestBody Configuracao entrada) {
        Escopo e = escopo();
        if (entrada == null) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Layout invalido");
        validar(entrada.componentes());
        try {
            String json = mapper.writeValueAsString(entrada.componentes());
            jdbc.update("""
                INSERT INTO dashboard_componentes_usuario
                    (empresa_id, usuario_id, configuracao, atualizado_em)
                VALUES (?, ?, ?, CURRENT_TIMESTAMP)
                ON CONFLICT (empresa_id, usuario_id) DO UPDATE
                    SET configuracao = EXCLUDED.configuracao,
                        atualizado_em = CURRENT_TIMESTAMP
                """, e.empresaId(), e.usuarioId(), json);
            return new Configuracao(List.copyOf(entrada.componentes()));
        } catch (JsonProcessingException exc) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                "Falha na serializacao da configuracao", exc);
        }
    }

    private static void validar(List<WidgetConfig> items) {
        if (items == null || items.size() > COMPONENTES.size()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                "Quantidade de componentes invalida");
        }
        Set<String> ids = new HashSet<>();
        for (WidgetConfig w: items) {
            if (w == null || !COMPONENTES.contains(w.id()) || !ids.add(w.id())
                || !TIPOS.contains(w.tipo()) || w.colunas() == null
                || w.colunas() < 1 || w.colunas() > 2) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Componente ou configuracao nao permitidos");
            }
        }
    }
}
