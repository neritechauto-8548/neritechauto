package com.neritech.saas.empresa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PoliticasGeraisService {
    private enum Tipo { TEXTO, BOOLEANO, INTEIRO }
    private record Definicao(String secao, String titulo, String padrao, String tabela,
                             String coluna, String colunaAtiva, Tipo tipo) {}

    private static Definicao d(String secao, String titulo, String padrao, String tabela,
                               String coluna, Tipo tipo) {
        return new Definicao(secao, titulo, padrao, tabela, coluna, coluna + "_ativo", tipo);
    }

    private static final Map<String, Definicao> PERMITIDAS = Map.ofEntries(
        Map.entry("locale", d("Empresa", "Idioma e formato", "pt-BR", "preferencias_operacionais_empresa", "locale", Tipo.TEXTO)),
        Map.entry("precisaoQuantidades", d("Operação", "Casas decimais em quantidades", "2", "preferencias_operacionais_empresa", "precisao_quantidades", Tipo.INTEIRO)),
        Map.entry("validadeOrcamentoDias", d("Orçamentos", "Validade padrão (dias)", "15", "preferencias_operacionais_empresa", "validade_orcamento_dias", Tipo.INTEIRO)),
        Map.entry("avisosAtivos", d("Comunicação", "Exibir avisos internos", "true", "preferencias_operacionais_empresa", "avisos_ativos", Tipo.BOOLEANO)),
        Map.entry("retencaoHistoricoDias", d("Operação", "Retenção planejada (dias)", "365", "preferencias_operacionais_empresa", "retencao_historico_dias", Tipo.INTEIRO)),
        Map.entry("codigoPadraoPecas", d("Matriz", "Código padrão para peças e insumos", "INTERNO", "preferencias_operacionais_empresa", "codigo_padrao_pecas", Tipo.TEXTO)),
        Map.entry("kmObrigatorio", d("Matriz", "KM obrigatório no cadastro de veículo", "false", "preferencias_operacionais_empresa", "km_obrigatorio", Tipo.BOOLEANO)),
        Map.entry("atualizarKmAutomaticamente", d("Matriz", "Atualizar KM pela ordem de serviço", "true", "preferencias_operacionais_empresa", "atualizar_km_automaticamente", Tipo.BOOLEANO)),
        Map.entry("ordemSelecaoAvaria", d("Matriz", "Ordem de seleção da avaria", "LOCAL_AVARIA", "preferencias_operacionais_empresa", "ordem_selecao_avaria", Tipo.TEXTO)),
        Map.entry("periodoExpiracaoSenhas", d("Matriz", "Período de expiração de senhas", "NAO_EXPIRA", "preferencias_operacionais_empresa", "periodo_expiracao_senhas", Tipo.TEXTO)),
        Map.entry("categoriaEmContasReceber", d("Cliente", "Exibir categoria em Contas a receber", "false", "preferencias_clientes_empresa", "categoria_em_contas_receber", Tipo.BOOLEANO)),
        Map.entry("categoriaEmVendaDireta", d("Cliente", "Exibir categoria em Venda direta", "false", "preferencias_clientes_empresa", "categoria_em_venda_direta", Tipo.BOOLEANO)),
        Map.entry("categoriaEmNotaFiscal", d("Cliente", "Exibir categoria em Nota fiscal", "false", "preferencias_clientes_empresa", "categoria_em_nota_fiscal", Tipo.BOOLEANO)),
        Map.entry("categoriaEmOrdemServico", d("Cliente", "Exibir categoria em Ordem de serviço", "false", "preferencias_clientes_empresa", "categoria_em_ordem_servico", Tipo.BOOLEANO)),
        Map.entry("categoriaEmListagem", d("Cliente", "Exibir categoria na listagem de cliente", "false", "preferencias_clientes_empresa", "categoria_em_listagem", Tipo.BOOLEANO)),
        Map.entry("alterarPrecoCompra", d("Compras", "Alterar preço ao autorizar compra", "PERGUNTAR", "preferencias_compras_empresa", "alterar_preco_compra", Tipo.TEXTO)),
        Map.entry("enviarCustoCompraOs", d("Compras", "Enviar custo dos itens da OS à compra", "true", "preferencias_compras_empresa", "enviar_custo_compra_os", Tipo.BOOLEANO)),
        Map.entry("atualizarFabricanteCompra", d("Compras", "Atualizar fabricante pela compra", "true", "preferencias_compras_empresa", "atualizar_fabricante_compra", Tipo.BOOLEANO)),
        Map.entry("atualizarOrigemCompra", d("Compras", "Atualizar origem da peça pela compra", "true", "preferencias_compras_empresa", "atualizar_origem_compra", Tipo.BOOLEANO)),
        Map.entry("atualizarNcmImportacao", d("Compras", "Atualizar NCM na importação", "true", "preferencias_compras_empresa", "atualizar_ncm_importacao", Tipo.BOOLEANO)),
        Map.entry("liquidarBoletoNoVencimento", d("Financeiro", "Liquidar parcelas somente antes do vencimento", "true", "preferencias_financeiras_empresa", "liquidar_boleto_no_vencimento", Tipo.BOOLEANO)),
        Map.entry("liquidacaoAutomaticaBoleto", d("Financeiro", "Liquidar parcelas automaticamente após pagamento do boleto", "true", "preferencias_financeiras_empresa", "liquidacao_automatica_boleto", Tipo.BOOLEANO)),
        Map.entry("orientacaoImpressaoBoleto", d("Financeiro", "Orientação da impressão de boleto", "RETRATO", "preferencias_financeiras_empresa", "orientacao_impressao_boleto", Tipo.TEXTO)),
        Map.entry("estoqueTodasEmpresasVenda", d("Venda direta", "Visualizar estoque de todas as empresas", "true", "preferencias_venda_direta_empresa", "estoque_todas_empresas_venda", Tipo.BOOLEANO)),
        Map.entry("calculoRevisaoCrm", d("CRM", "Cálculo para envio de próximas revisões", "KM_VIDA_UTIL", "preferencias_crm_empresa", "calculo_revisao_crm", Tipo.TEXTO)),
        Map.entry("nomeRemetenteCrm", d("CRM", "Nome do remetente CRM", "", "preferencias_crm_empresa", "nome_remetente_crm", Tipo.TEXTO)),
        Map.entry("emailRespostaCrm", d("CRM", "E-mail de resposta CRM", "", "preferencias_crm_empresa", "email_resposta_crm", Tipo.TEXTO)),
        Map.entry("crmAniversario", d("CRM", "Enviar comunicação de aniversário", "false", "preferencias_crm_empresa", "crm_aniversario", Tipo.BOOLEANO)),
        Map.entry("crmServicosFuturos", d("CRM", "Enviar comunicação de serviços futuros", "false", "preferencias_crm_empresa", "crm_servicos_futuros", Tipo.BOOLEANO)),
        Map.entry("crmProximasRevisoes", d("CRM", "Enviar comunicação de próximas revisões", "false", "preferencias_crm_empresa", "crm_proximas_revisoes", Tipo.BOOLEANO)),
        Map.entry("crmClientesSemMovimento", d("CRM", "Enviar comunicação a clientes sem movimento", "false", "preferencias_crm_empresa", "crm_clientes_sem_movimento", Tipo.BOOLEANO)),
        Map.entry("crmPesquisaSatisfacao", d("CRM", "Enviar pesquisa de satisfação", "false", "preferencias_crm_empresa", "crm_pesquisa_satisfacao", Tipo.BOOLEANO)),
        Map.entry("crmCobrancaDividas", d("CRM", "Enviar cobrança de dívidas", "false", "preferencias_crm_empresa", "crm_cobranca_dividas", Tipo.BOOLEANO)),
        Map.entry("crmViaSmtp", d("CRM", "Enviar e-mails CRM via SMTP", "false", "preferencias_crm_empresa", "crm_via_smtp", Tipo.BOOLEANO)),
        Map.entry("relatorioQuadroAnotacoes", d("Relatório", "Exibir quadro de anotações", "false", "preferencias_relatorios_empresa", "relatorio_quadro_anotacoes", Tipo.BOOLEANO)),
        Map.entry("relatorioSepararAreas", d("Relatório", "Separar itens por área no relatório", "false", "preferencias_relatorios_empresa", "relatorio_separar_areas", Tipo.BOOLEANO)),
        Map.entry("relatorioDadosCliente", d("Relatório", "Exibir dados do cliente no relatório", "false", "preferencias_relatorios_empresa", "relatorio_dados_cliente", Tipo.BOOLEANO)),
        Map.entry("ordenacaoRelatorio", d("Relatório", "Ordenação de relatórios administrativos", "DESCRICAO", "preferencias_relatorios_empresa", "ordenacao_relatorio", Tipo.TEXTO)),
        Map.entry("baseCalculoComissao", d("Relatório", "Base de cálculo da comissão", "LIQUIDO", "preferencias_relatorios_empresa", "base_calculo_comissao", Tipo.TEXTO)),
        Map.entry("notaFiscalOutraEmpresa", d("Fiscal", "Permitir emitir nota por outra empresa", "false", "preferencias_fiscais_empresa", "nota_fiscal_outra_empresa", Tipo.BOOLEANO))
    );

    public record Alteracao(String campo, String anterior, String novo, Long autor, LocalDateTime data, Integer versao) {}
    public record Politica(String chave, String secao, String titulo, String valorAplicado, String valorSalvo,
                           String origem, boolean ativo, Integer versao, LocalDateTime alteradoEm, Long responsavel) {}
    public record Alterar(String chave, String valor) {}
    public record Estado(Boolean ativo) {}
    public record Integracao(String canal, String estado) {}

    private final JdbcTemplate jdbc;
    private final UsuarioRepository usuarios;
    private final ObjectMapper json;

    public PoliticasGeraisService(JdbcTemplate jdbc, UsuarioRepository usuarios, ObjectMapper json) {
        this.jdbc = jdbc;
        this.usuarios = usuarios;
        this.json = json;
    }

    @Transactional(readOnly = true)
    public Page<Politica> listar(String busca, String secao, Boolean ativo, Pageable pageable) {
        Long empresaId = TenantAccess.requireCurrent();
        validarPagina(pageable);
        List<Politica> politicas = PERMITIDAS.entrySet().stream()
            .map(e -> obterSalva(empresaId, e.getKey(), e.getValue()))
            .filter(p -> secao == null || secao.isBlank() || p.secao().equalsIgnoreCase(secao.trim()))
            .filter(p -> busca == null || busca.isBlank()
                || p.chave().toLowerCase(Locale.ROOT).contains(busca.trim().toLowerCase(Locale.ROOT))
                || p.titulo().toLowerCase(Locale.ROOT).contains(busca.trim().toLowerCase(Locale.ROOT)))
            .filter(p -> ativo == null || p.ativo() == ativo)
            .sorted(Comparator.comparing(Politica::secao).thenComparing(Politica::chave)).toList();
        int inicio = Math.min((int) Math.min(Integer.MAX_VALUE, pageable.getOffset()), politicas.size());
        int fim = Math.min(inicio + pageable.getPageSize(), politicas.size());
        return new PageImpl<>(politicas.subList(inicio, fim), pageable, politicas.size());
    }

    @Transactional(readOnly = true)
    public List<Integracao> integracoes() {
        Long empresaId = TenantAccess.requireCurrent();
        return List.of(new Integracao("E-mail", estadoEmail(empresaId)),
            new Integracao("SMS", estadoSms(empresaId)), new Integracao("WhatsApp", estadoWhatsapp(empresaId)));
    }

    @Transactional(readOnly = true)
    public Politica obter(String chave) {
        validarChave(chave);
        return obterSalva(TenantAccess.requireCurrent(), chave, PERMITIDAS.get(chave));
    }

    @Transactional
    public Politica criar(Alterar request) {
        validarChave(request.chave());
        Definicao definicao = PERMITIDAS.get(request.chave());
        Object valor = validarValor(request.chave(), request.valor());
        Long empresaId = TenantAccess.requireCurrent();
        Long autor = usuario(empresaId);
        jdbc.update("INSERT INTO " + definicao.tabela() + " (empresa_id, versao) VALUES (?, -1) ON CONFLICT (empresa_id) DO NOTHING", empresaId);
        int linhas = jdbc.update("UPDATE " + definicao.tabela() + " SET " + definicao.coluna() + " = ?, "
                + definicao.colunaAtiva() + " = TRUE, versao = versao + 1, atualizado_por = ?, atualizado_em = CURRENT_TIMESTAMP "
                + "WHERE empresa_id = ? AND " + definicao.colunaAtiva() + " IS NULL",
            valor, autor, empresaId);
        if (linhas == 0) throw new ResponseStatusException(HttpStatus.CONFLICT, "Preferência já personalizada para esta empresa");
        Politica salva = obterSalva(empresaId, request.chave(), definicao);
        registrar(empresaId, definicao, request.chave(), null, valor, salva.versao(), autor, "INSERT");
        return obter(request.chave());
    }

    @Transactional
    public Politica editar(String chave, String valorBruto, Integer versao) {
        validarChave(chave);
        Definicao definicao = PERMITIDAS.get(chave);
        Object valor = validarValor(chave, valorBruto);
        requireVersion(versao);
        Long empresaId = TenantAccess.requireCurrent();
        Politica anterior = obterSalva(empresaId, chave, definicao);
        if (anterior.versao() == null) throw new ResponseStatusException(HttpStatus.CONFLICT, "Crie a preferência antes de editá-la");
        if (!Objects.equals(anterior.versao(), versao)) throw conflito();
        if (Objects.equals(anterior.valorSalvo(), valor.toString())) return anterior;
        Long autor = usuario(empresaId);
        int linhas = jdbc.update("UPDATE " + definicao.tabela() + " SET " + definicao.coluna() + " = ?, versao = versao + 1, "
                + "atualizado_por = ?, atualizado_em = CURRENT_TIMESTAMP WHERE empresa_id = ? AND " + definicao.colunaAtiva()
                + " IS NOT NULL AND versao = ?", valor, autor, empresaId, versao);
        if (linhas == 0) throw conflito();
        registrar(empresaId, definicao, chave, anterior.valorSalvo(), valor, versao + 1, autor, "UPDATE");
        return obter(chave);
    }

    @Transactional
    public Politica situacao(String chave, boolean ativo, Integer versao) {
        validarChave(chave);
        Definicao definicao = PERMITIDAS.get(chave);
        requireVersion(versao);
        Long empresaId = TenantAccess.requireCurrent();
        Politica anterior = obterSalva(empresaId, chave, definicao);
        if (anterior.versao() == null || !Objects.equals(anterior.versao(), versao)) throw conflito();
        if (anterior.ativo() == ativo) return anterior;
        Long autor = usuario(empresaId);
        int linhas = jdbc.update("UPDATE " + definicao.tabela() + " SET " + definicao.colunaAtiva()
                + " = ?, versao = versao + 1, atualizado_por = ?, atualizado_em = CURRENT_TIMESTAMP "
                + "WHERE empresa_id = ? AND " + definicao.colunaAtiva() + " IS NOT NULL AND versao = ?",
            ativo, autor, empresaId, versao);
        if (linhas == 0) throw conflito();
        registrar(empresaId, definicao, chave + ".ativo", anterior.ativo(), ativo, versao + 1, autor, "UPDATE");
        return obter(chave);
    }

    @Transactional(readOnly = true)
    public Page<Alteracao> historico(String chave, Pageable pageable) {
        validarChave(chave);
        validarPagina(pageable);
        Long empresaId = TenantAccess.requireCurrent();
        Definicao definicao = PERMITIDAS.get(chave);
        String campo = serializar(List.of(chave));
        String campoAtivo = serializar(List.of(chave + ".ativo"));
        Long total = jdbc.queryForObject("SELECT COUNT(*) FROM logs_alteracoes WHERE empresa_id = ? "
                + "AND tabela_afetada IN ('politicas_gerais_empresa', ?) AND contexto_operacao = 'ADM-011' "
                + "AND (campos_alterados = ? OR campos_alterados = ?)", Long.class,
            empresaId, definicao.tabela(), campo, campoAtivo);
        List<Alteracao> dados = jdbc.query("SELECT campos_alterados, valores_antigos, valores_novos, usuario_responsavel, "
                + "data_alteracao, dados_adicionais FROM logs_alteracoes WHERE empresa_id = ? "
                + "AND tabela_afetada IN ('politicas_gerais_empresa', ?) AND contexto_operacao = 'ADM-011' "
                + "AND (campos_alterados = ? OR campos_alterados = ?) ORDER BY data_alteracao DESC, id DESC LIMIT ? OFFSET ?",
            (rs, i) -> lerAlteracao(rs), empresaId, definicao.tabela(), campo, campoAtivo,
            pageable.getPageSize(), pageable.getOffset());
        return new PageImpl<>(dados, pageable, total == null ? 0 : total);
    }

    private Alteracao lerAlteracao(ResultSet rs) throws SQLException {
        try {
            String campo = json.readTree(rs.getString(1)).path(0).asText();
            var anterior = json.readTree(rs.getString(2)).path(campo);
            var novo = json.readTree(rs.getString(3)).path(campo);
            return new Alteracao(campo, anterior.isMissingNode() || anterior.isNull() ? "—" : anterior.asText(),
                novo.isMissingNode() || novo.isNull() ? "—" : novo.asText(), rs.getLong(4),
                rs.getTimestamp(5).toLocalDateTime(), json.readTree(rs.getString(6)).path("versao").asInt());
        } catch (JsonProcessingException ex) { throw new IllegalStateException("Log inválido", ex); }
    }

    private Politica obterSalva(Long empresaId, String chave, Definicao d) {
        return jdbc.query("SELECT " + d.coluna() + " AS valor, " + d.colunaAtiva() + " AS ativo, versao, atualizado_em, atualizado_por "
                + "FROM " + d.tabela() + " WHERE empresa_id = ?", rs -> rs.next() ? montar(rs, chave, d) : padrao(chave, d), empresaId);
    }

    private Politica montar(ResultSet rs, String chave, Definicao d) throws SQLException {
        Object salvo = rs.getObject("valor");
        Boolean ativoSalvo = rs.getObject("ativo", Boolean.class);
        if (ativoSalvo == null) return padrao(chave, d);
        Integer versao = rs.getInt("versao");
        LocalDateTime atualizadoEm = rs.getTimestamp("atualizado_em") == null ? null : rs.getTimestamp("atualizado_em").toLocalDateTime();
        String textoSalvo = salvo == null ? null : salvo.toString();
        return new Politica(chave, d.secao(), d.titulo(), ativoSalvo && textoSalvo != null ? textoSalvo : d.padrao(),
            textoSalvo, ativoSalvo ? "EMPRESA" : "PADRAO_INATIVA", ativoSalvo, versao, atualizadoEm,
            rs.getObject("atualizado_por", Long.class));
    }

    private Politica padrao(String chave, Definicao d) {
        return new Politica(chave, d.secao(), d.titulo(), d.padrao(), null, "PADRAO", true, null, null, null);
    }

    private Object validarValor(String chave, String valorBruto) {
        validarChave(chave);
        String valor = valorBruto;
        if (valor != null && List.of("nomeRemetenteCrm", "emailRespostaCrm").contains(chave)) valor = valor.trim();
        boolean textoCrm = List.of("nomeRemetenteCrm", "emailRespostaCrm").contains(chave);
        if (valor == null || (!textoCrm && valor.isBlank()) || valor.length() > ("emailRespostaCrm".equals(chave) ? 254 : 160)
                || valor.toLowerCase(Locale.ROOT).matches(".*(token|password|senha|secret|apikey).*")) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Valor inválido ou credencial proibida");
        }
        boolean valido = switch (chave) {
            case "locale" -> List.of("pt-BR", "en-US").contains(valor);
            case "precisaoQuantidades" -> valor.matches("[0-4]");
            case "validadeOrcamentoDias" -> entre(valor, 1, 365);
            case "retencaoHistoricoDias" -> entre(valor, 30, 3650);
            case "avisosAtivos", "kmObrigatorio", "atualizarKmAutomaticamente", "categoriaEmContasReceber",
                "categoriaEmVendaDireta", "categoriaEmNotaFiscal", "categoriaEmOrdemServico", "categoriaEmListagem",
                "enviarCustoCompraOs", "atualizarFabricanteCompra", "atualizarOrigemCompra", "atualizarNcmImportacao",
                "liquidarBoletoNoVencimento", "liquidacaoAutomaticaBoleto", "estoqueTodasEmpresasVenda", "crmAniversario",
                "crmServicosFuturos", "crmProximasRevisoes", "crmClientesSemMovimento", "crmPesquisaSatisfacao",
                "crmCobrancaDividas", "crmViaSmtp", "relatorioQuadroAnotacoes", "relatorioSepararAreas",
                "relatorioDadosCliente", "notaFiscalOutraEmpresa" -> List.of("true", "false").contains(valor);
            case "codigoPadraoPecas" -> List.of("FABRICANTE", "BARRAS", "INTERNO", "EMPRESA").contains(valor);
            case "ordemSelecaoAvaria" -> List.of("LOCAL_AVARIA", "AVARIA_LOCAL").contains(valor);
            case "periodoExpiracaoSenhas" -> List.of("NAO_EXPIRA", "3_MESES", "6_MESES", "12_MESES").contains(valor);
            case "alterarPrecoCompra" -> List.of("NUNCA", "PERGUNTAR", "SEMPRE").contains(valor);
            case "orientacaoImpressaoBoleto" -> List.of("PAISAGEM", "RETRATO").contains(valor);
            case "calculoRevisaoCrm" -> List.of("KM_VIDA_UTIL", "MESES").contains(valor);
            case "ordenacaoRelatorio" -> List.of("CODIGO", "DESCRICAO", "INSERCAO", "PRECO").contains(valor);
            case "baseCalculoComissao" -> List.of("BASE", "LIQUIDO").contains(valor);
            case "nomeRemetenteCrm" -> valor.isBlank() || valor.length() <= 120;
            case "emailRespostaCrm" -> valor.isBlank() || valor.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
            default -> false;
        };
        if (!valido) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
            "Valor fora dos limites da política para a chave '" + chave + "'.");
        return switch (PERMITIDAS.get(chave).tipo()) {
            case BOOLEANO -> Boolean.valueOf(valor);
            case INTEIRO -> Short.valueOf(valor);
            case TEXTO -> valor;
        };
    }

    private static boolean entre(String valor, int minimo, int maximo) {
        try { int n = Integer.parseInt(valor); return n >= minimo && n <= maximo && valor.matches("[0-9]+"); }
        catch (NumberFormatException ex) { return false; }
    }

    private String estadoEmail(Long empresaId) {
        return jdbc.queryForObject("SELECT CASE WHEN EXISTS (SELECT 1 FROM com_configuracoes_email WHERE empresa_id = ? "
            + "AND ativo IS TRUE AND testado IS TRUE AND (NULLIF(BTRIM(api_key), '') IS NOT NULL OR NULLIF(BTRIM(senha_smtp), '') IS NOT NULL)) "
            + "THEN 'TESTADA' WHEN EXISTS (SELECT 1 FROM com_configuracoes_email WHERE empresa_id = ? AND ativo IS TRUE "
            + "AND (NULLIF(BTRIM(api_key), '') IS NOT NULL OR NULLIF(BTRIM(senha_smtp), '') IS NOT NULL)) "
            + "THEN 'CADASTRADA' ELSE 'NAO_CONFIGURADA' END", String.class, empresaId, empresaId);
    }

    private String estadoSms(Long empresaId) {
        return jdbc.queryForObject("SELECT CASE WHEN EXISTS (SELECT 1 FROM com_configuracoes_sms WHERE empresa_id = ? AND ativo IS TRUE "
            + "AND (NULLIF(BTRIM(api_key), '') IS NOT NULL OR (NULLIF(BTRIM(usuario_api), '') IS NOT NULL AND NULLIF(BTRIM(senha_api), '') IS NOT NULL))) "
            + "THEN 'CADASTRADA' ELSE 'NAO_CONFIGURADA' END", String.class, empresaId);
    }

    private String estadoWhatsapp(Long empresaId) {
        return jdbc.queryForObject("SELECT CASE WHEN EXISTS (SELECT 1 FROM com_configuracoes_whatsapp WHERE empresa_id = ? "
            + "AND integracao_ativa IS TRUE AND (NULLIF(BTRIM(token_acesso), '') IS NOT NULL OR NULLIF(BTRIM(webhook_token), '') IS NOT NULL)) "
            + "THEN 'CADASTRADA' ELSE 'NAO_CONFIGURADA' END", String.class, empresaId);
    }

    private void validarChave(String chave) {
        if (chave == null || !PERMITIDAS.containsKey(chave))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Política não permitida");
    }
    private static void requireVersion(Integer versao) {
        if (versao == null || versao < 0) throw new ResponseStatusException(HttpStatus.CONFLICT, "Versão obrigatória");
    }
    private static ResponseStatusException conflito() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Configuração alterada em outra sessão");
    }
    private static void validarPagina(Pageable pageable) {
        if (!List.of(20, 50, 100).contains(pageable.getPageSize()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
    }
    private Long usuario(Long empresaId) {
        var autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null || !autenticacao.isAuthenticated()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return usuarios.findByEmailIgnoreCaseAndEmpresaId(autenticacao.getName(), empresaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário fora da empresa")).getId();
    }
    private void registrar(Long empresaId, Definicao d, String campo, Object anterior, Object novo,
                           int versao, Long autor, String operacao) {
        jdbc.update("INSERT INTO logs_alteracoes (empresa_id, tabela_afetada, registro_id, operacao, campos_alterados, "
                + "valores_antigos, valores_novos, usuario_responsavel, contexto_operacao, dados_adicionais, data_alteracao) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'ADM-011', ?, CURRENT_TIMESTAMP)",
            empresaId, d.tabela(), empresaId, operacao, serializar(List.of(campo)),
            serializar(Collections.singletonMap(campo, anterior)), serializar(Collections.singletonMap(campo, novo)),
            autor, serializar(Map.of("versao", versao)));
    }
    private String serializar(Object objeto) {
        try { return json.writeValueAsString(objeto); }
        catch (JsonProcessingException ex) { throw new IllegalStateException("Falha na auditoria", ex); }
    }
}
