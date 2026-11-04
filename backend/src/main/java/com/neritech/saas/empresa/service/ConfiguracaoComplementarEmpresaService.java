package com.neritech.saas.empresa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class ConfiguracaoComplementarEmpresaService {
    private static final Map<String, String> TABELAS = Map.ofEntries(
            Map.entry("ATENDIMENTO", "configuracoes_atendimento_empresa"), Map.entry("COMERCIAL", "configuracoes_comerciais_empresa"),
            Map.entry("ESTOQUE_FINANCEIRO", "configuracoes_estoque_financeiro_empresa"), Map.entry("INTEGRACOES", "configuracoes_integracoes_empresa"),
            Map.entry("GESTAO_PATIO", "configuracoes_gestao_patio_empresa"), Map.entry("FERIADOS_LOCAIS", "configuracoes_feriados_locais_empresa"),
            Map.entry("PRODUTO_COMPOSICAO", "configuracoes_composicao_produto_empresa"), Map.entry("PRODUTO_CUSTO_LUCRO", "configuracoes_custo_lucro_produto_empresa"),
            Map.entry("COMPRAS", "configuracoes_compras_empresa"), Map.entry("AGENDAMENTO", "configuracoes_agendamento_empresa"),
            Map.entry("FINANCEIRO", "configuracoes_financeiras_empresa"), Map.entry("TERMOS", "termos_empresa"),
            Map.entry("FISCAL_GERAL", "configuracoes_fiscais_gerais_empresa"), Map.entry("FISCAL_NFE", "configuracoes_nfe_empresa"),
            Map.entry("FISCAL_NFCE", "configuracoes_nfce_empresa"), Map.entry("FISCAL_NFSE", "configuracoes_nfse_empresa"),
            Map.entry("FISCAL_FCP", "aliquotas_fcp_empresa"), Map.entry("FISCAL_SINTEGRA", "configuracoes_sintegra_empresa"),
            Map.entry("FISCAL_SPED", "configuracoes_sped_empresa")
    );
    private static final Set<String> CAMPOS_LISTA = Set.of("feriados", "indisponibilidades", "contatos", "autorizacoes", "series");
    private static final Map<String, Set<String>> CAMPOS = Map.ofEntries(
            Map.entry("ATENDIMENTO", Set.of("campoKmObrigatorio", "campoPrismaObrigatorio", "campoNivelTanqueObrigatorio", "exibirCamposIndicacaoCorretor", "vincularObservacoesOrdensAtividades", "preencherFolhaMecanicoAutomaticamente")),
            Map.entry("COMERCIAL", Set.of("margemReferencia", "descontoMaximoPeca", "descontoMaximoServico", "aplicarAcrescimoOs")),
            Map.entry("ESTOQUE_FINANCEIRO", Set.of("baixaAutomaticaEstoque", "notificarEstoqueMinimo", "modoCondicaoPagamento")),
            Map.entry("INTEGRACOES", Set.of("valorHoraPintura", "areaPintura", "valorHoraTroca", "areaTroca", "valorHoraReparacao", "areaReparacao", "utilizarChecklistBosch", "enviarOsMyBcsAutomaticamente", "receberEventosMyBcs")),
            Map.entry("GESTAO_PATIO", Set.of("criarAtividadeAoGerarOs", "moverCartaoAoFecharOs")),
            Map.entry("FERIADOS_LOCAIS", Set.of("feriados")),
            Map.entry("PRODUTO_COMPOSICAO", Set.of("percentualIpi", "frete", "custoFixo", "despesasOperacionais", "substituicaoTributaria", "outrasDespesas", "icmsDesoneracao", "calcularMargemLucro")),
            Map.entry("PRODUTO_CUSTO_LUCRO", Set.of("percentualCustoFixo", "percentualLucroFixo")),
            Map.entry("COMPRAS", Set.of("bloquearCfopSaida", "permitirExcluirItensImportados", "permitirPrecoVendaNaCompra", "exigirMotivoCancelamento")),
            Map.entry("AGENDAMENTO", Set.of("indisponibilidades")),
            Map.entry("FINANCEIRO", Set.of("parcelasLivresVendaDireta", "dataVendaReferenciaVencimento", "permitirLiquidoNegativoPagar")),
            Map.entry("TERMOS", Set.of("termoLgpd", "termoGarantia", "termoQuitacao", "termoPecasFornecidas", "termoResponsabilidade", "declaracaoPermanencia", "termoCorteSeloPlaca", "declaracaoAssinaturaOs")),
            Map.entry("FISCAL_GERAL", camposFiscaisGerais()),
            Map.entry("FISCAL_NFE", camposNotas()),
            Map.entry("FISCAL_NFCE", camposNotas()),
            Map.entry("FISCAL_NFSE", camposNotasServico()),
            Map.entry("FISCAL_FCP", camposFcp()),
            Map.entry("FISCAL_SINTEGRA", Set.of("codigoEstrutura", "codigoNaturezaOperacoes", "nomeResponsavel", "telefoneResponsavel", "gerarRegistro51", "gerarRegistro53")),
            Map.entry("FISCAL_SPED", Set.of("nomeContador", "cpfContador", "crcContador", "telefoneContador", "emailContador", "regimePisCofins"))
    );
    private static final Set<String> CAMPOS_BOOLEANOS = Set.of(
            "campoKmObrigatorio", "campoPrismaObrigatorio", "campoNivelTanqueObrigatorio", "exibirCamposIndicacaoCorretor", "vincularObservacoesOrdensAtividades", "preencherFolhaMecanicoAutomaticamente", "aplicarAcrescimoOs", "baixaAutomaticaEstoque", "notificarEstoqueMinimo", "utilizarChecklistBosch", "enviarOsMyBcsAutomaticamente", "receberEventosMyBcs", "criarAtividadeAoGerarOs", "moverCartaoAoFecharOs", "percentualIpi", "frete", "custoFixo", "despesasOperacionais", "substituicaoTributaria", "outrasDespesas", "icmsDesoneracao", "calcularMargemLucro", "bloquearCfopSaida", "permitirExcluirItensImportados", "permitirPrecoVendaNaCompra", "exigirMotivoCancelamento", "parcelasLivresVendaDireta", "dataVendaReferenciaVencimento", "permitirLiquidoNegativoPagar",
            "optanteSimplesNacional", "habilitarNfe", "habilitarNfce", "habilitarNfse", "emitePropriasNotas", "mostrarValorTotalNotas", "recuperarPesoPeca", "incentivoCultural", "incentivoFiscal", "deduzirIcmsPisCofins", "calcularIbptAutomaticamente", "impressaoFcp", "partilhaImpressao", "consultarDfe", "enviarEmail", "utilizarQrCodeVersao3", "enviarEmailEntrega", "agruparServicosPorArea", "ocultarIbptDescricao", "emitirNfsNacional", "numeracaoAutomaticaRps", "quebrarLinhaPadrao", "gerarRegistro51", "gerarRegistro53"
    );
    private static final Set<String> CAMPOS_NUMERICOS = Set.of("margemReferencia", "descontoMaximoPeca", "descontoMaximoServico", "valorHoraPintura", "valorHoraTroca", "valorHoraReparacao", "percentualCustoFixo", "percentualLucroFixo", "aliquotaIcms", "aliquotaIpi", "aliquotaPis", "aliquotaCofins", "aliquotaCsll", "aliquotaIrrf", "aliquotaIbsUf", "aliquotaIbsMunicipal", "aliquotaCbs", "diferimentoIbsUf", "diferimentoIbsMunicipal", "diferimentoCbs", "devolvidoIbsUf", "devolvidoIbsMunicipal", "devolvidoCbs", "receitaBruta", "aliquotaIbptSimples", "aliquotaIbptFederal", "aliquotaIbptEstadual", "aliquotaIbptMunicipal", "aliquotaAproximadaTributos", "loteRps", "fcpAC", "fcpAL", "fcpAM", "fcpAP", "fcpBA", "fcpCE", "fcpDF", "fcpES", "fcpGO", "fcpMA", "fcpMG", "fcpMS", "fcpMT", "fcpPA", "fcpPB", "fcpPE", "fcpPI", "fcpPR", "fcpRJ", "fcpRN", "fcpRO", "fcpRR", "fcpRS", "fcpSC", "fcpSE", "fcpSP", "fcpTO");
    private static final Set<String> CAMPOS_TEXTO = Set.of("areaPintura", "areaTroca", "areaReparacao", "modoCondicaoPagamento", "termoLgpd", "termoGarantia", "termoQuitacao", "termoPecasFornecidas", "termoResponsabilidade", "declaracaoPermanencia", "termoCorteSeloPlaca", "declaracaoAssinaturaOs", "ambienteEmissao", "regimeTributarioEspecial", "codigoPecaDanfe", "inscricaoEstadual", "inscricaoMunicipal", "regimeTributario", "cnae", "cep", "estado", "cidade", "bairro", "logradouro", "numero", "complemento", "cfopPadrao", "naturezaOperacao", "unidade", "codigoBeneficioFiscal", "cstIcms", "cstIpi", "codigoEnquadramentoIpi", "cstPis", "cstCofins", "cstIbsCbs", "classificacaoTributaria", "tipoEmissao", "csrtEstado", "informacaoInteresseFisco", "informacaoInteresseContribuinte", "informacaoDevolucao", "codigoServicoIssqn", "codigoNbsPadrao", "tipoOperacao", "naturezaTributacao", "codigoIndicadorOperacao", "regimeApuracao", "leiAnexoTabela", "dataInicioAtividade", "loginPrefeitura", "codigoEstrutura", "codigoNaturezaOperacoes", "nomeResponsavel", "telefoneResponsavel", "nomeContador", "cpfContador", "crcContador", "telefoneContador", "emailContador", "regimePisCofins");

    private static Set<String> camposFiscaisGerais() { return Set.of("ambienteEmissao", "regimeTributarioEspecial", "optanteSimplesNacional", "habilitarNfe", "habilitarNfce", "habilitarNfse", "emitePropriasNotas", "mostrarValorTotalNotas", "recuperarPesoPeca", "incentivoCultural", "incentivoFiscal", "deduzirIcmsPisCofins", "codigoPecaDanfe", "contatos", "autorizacoes", "inscricaoEstadual", "inscricaoMunicipal", "regimeTributario", "cnae", "cep", "estado", "cidade", "bairro", "logradouro", "numero", "complemento"); }
    private static Set<String> camposNotas() { return Set.of("cfopPadrao", "naturezaOperacao", "unidade", "codigoBeneficioFiscal", "calcularIbptAutomaticamente", "cstIcms", "aliquotaIcms", "cstIpi", "aliquotaIpi", "codigoEnquadramentoIpi", "cstPis", "aliquotaPis", "cstCofins", "aliquotaCofins", "aliquotaCsll", "aliquotaIrrf", "cstIbsCbs", "classificacaoTributaria", "aliquotaIbsUf", "aliquotaIbsMunicipal", "aliquotaCbs", "diferimentoIbsUf", "diferimentoIbsMunicipal", "diferimentoCbs", "devolvidoIbsUf", "devolvidoIbsMunicipal", "devolvidoCbs", "tipoEmissao", "csrtEstado", "impressaoFcp", "partilhaImpressao", "consultarDfe", "enviarEmail", "utilizarQrCodeVersao3", "enviarEmailEntrega", "informacaoInteresseFisco", "informacaoInteresseContribuinte", "informacaoDevolucao", "series"); }
    private static Set<String> camposNotasServico() { return Set.of("codigoServicoIssqn", "codigoNbsPadrao", "tipoOperacao", "naturezaTributacao", "codigoIndicadorOperacao", "unidade", "agruparServicosPorArea", "regimeApuracao", "leiAnexoTabela", "dataInicioAtividade", "receitaBruta", "aliquotaIbptSimples", "aliquotaIbptFederal", "aliquotaIbptEstadual", "aliquotaIbptMunicipal", "aliquotaAproximadaTributos", "ocultarIbptDescricao", "calcularIbptAutomaticamente", "emitirNfsNacional", "loginPrefeitura", "numeracaoAutomaticaRps", "loteRps", "quebrarLinhaPadrao", "enviarEmail", "cstIbsCbs", "classificacaoTributaria", "aliquotaIbsUf", "aliquotaIbsMunicipal", "aliquotaCbs", "diferimentoIbsUf", "diferimentoIbsMunicipal", "diferimentoCbs", "devolvidoIbsUf", "devolvidoIbsMunicipal", "devolvidoCbs", "informacaoInteresseFisco", "series"); }
    private static Set<String> camposFcp() { return Set.of("fcpAC", "fcpAL", "fcpAM", "fcpAP", "fcpBA", "fcpCE", "fcpDF", "fcpES", "fcpGO", "fcpMA", "fcpMG", "fcpMS", "fcpMT", "fcpPA", "fcpPB", "fcpPE", "fcpPI", "fcpPR", "fcpRJ", "fcpRN", "fcpRO", "fcpRR", "fcpRS", "fcpSC", "fcpSE", "fcpSP", "fcpTO"); }

    public record Configuracao(Long id, Long empresaId, String secao, Map<String, Object> dados,
                               Integer versao, LocalDateTime atualizadoEm) {}
    public record Alteracao(Map<String, Object> dados, Integer versao) {}

    private final JdbcTemplate jdbc;
    private final UsuarioRepository usuarios;
    private final ObjectMapper json;

    public ConfiguracaoComplementarEmpresaService(JdbcTemplate jdbc, UsuarioRepository usuarios, ObjectMapper json) {
        this.jdbc = jdbc;
        this.usuarios = usuarios;
        this.json = json;
    }

    @Transactional(readOnly = true)
    public List<Configuracao> listar() {
        Long empresa = TenantAccess.requireCurrent();
        List<Configuracao> configuracoes = new ArrayList<>();
        for (String secao : TABELAS.keySet()) {
            Configuracao configuracao = buscar(empresa, secao);
            if (configuracao != null) configuracoes.add(configuracao);
        }
        configuracoes.sort(Comparator.comparing(Configuracao::secao));
        return configuracoes;
    }

    @Transactional
    public Configuracao salvar(String secao, Alteracao alteracao) {
        Long empresa = TenantAccess.requireCurrent();
        Set<String> camposPermitidos = CAMPOS.get(secao);
        if (camposPermitidos == null || alteracao == null || alteracao.dados() == null
                || !camposPermitidos.containsAll(alteracao.dados().keySet())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Seção ou campo de configuração inválido.");
        }
        validarDados(secao, alteracao.dados());
        Long autor = usuario(empresa);
        Configuracao atual = buscar(empresa, secao);
        String tabela = TABELAS.get(secao);
        List<String> camposEscalares = camposPermitidos.stream().filter(campo -> !CAMPOS_LISTA.contains(campo)).sorted().toList();
        List<Object> parametros = new ArrayList<>();
        camposEscalares.forEach(campo -> parametros.add(alteracao.dados().get(campo)));
        if (atual == null) {
            List<String> colunas = new ArrayList<>(List.of("empresa_id", "versao", "atualizado_por"));
            colunas.addAll(camposEscalares.stream().map(ConfiguracaoComplementarEmpresaService::coluna).toList());
            String marcadores = String.join(",", Collections.nCopies(colunas.size(), "?"));
            parametros.add(0, 0);
            parametros.add(0, empresa);
            parametros.add(2, autor);
            jdbc.update("INSERT INTO " + tabela + " (" + String.join(",", colunas) + ") VALUES (" + marcadores + ")", parametros.toArray());
            sincronizarListas(empresa, secao, alteracao.dados());
            auditar(empresa, empresa, secao, Map.of(), alteracao.dados(), autor, "INSERT");
            return buscar(empresa, secao);
        }
        if (alteracao.versao() == null || !Objects.equals(atual.versao(), alteracao.versao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Configuração alterada em outra sessão. Recarregue e tente novamente.");
        }
        List<String> atribuicoes = camposEscalares.stream().map(campo -> coluna(campo) + "=?").toList();
        parametros.add(autor);
        parametros.add(empresa);
        parametros.add(atual.versao());
        int linhas = jdbc.update("UPDATE " + tabela + " SET " + String.join(",", atribuicoes)
                + (atribuicoes.isEmpty() ? "" : ",") + "versao=versao+1, atualizado_por=?, atualizado_em=CURRENT_TIMESTAMP WHERE empresa_id=? AND versao=?", parametros.toArray());
        if (linhas == 0) throw new ResponseStatusException(HttpStatus.CONFLICT, "Configuração alterada em outra sessão.");
        sincronizarListas(empresa, secao, alteracao.dados());
        auditar(empresa, empresa, secao, atual.dados(), alteracao.dados(), autor, "UPDATE");
        return buscar(empresa, secao);
    }

    private void validarDados(String secao, Map<String, Object> dados) {
        dados.forEach((campo, valor) -> {
            if (CAMPOS_BOOLEANOS.contains(campo)) {
                if (!(valor instanceof Boolean)) invalido(campo);
            } else if (CAMPOS_NUMERICOS.contains(campo)) {
                if (valor == null || String.valueOf(valor).isBlank()) return;
                BigDecimal numero;
                try { numero = new BigDecimal(String.valueOf(valor)); }
                catch (RuntimeException erro) { invalido(campo); return; }
                BigDecimal limite = Set.of("margemReferencia", "descontoMaximoPeca", "descontoMaximoServico", "percentualCustoFixo", "percentualLucroFixo").contains(campo)
                        || campo.startsWith("aliquota") || campo.startsWith("diferimento") || campo.startsWith("devolvido") || campo.startsWith("fcp")
                        ? new BigDecimal("100") : new BigDecimal("1000000");
                if (numero.compareTo(BigDecimal.ZERO) < 0 || numero.compareTo(limite) > 0) invalido(campo);
            } else if (CAMPOS_TEXTO.contains(campo)) {
                if (!(valor instanceof String texto) || texto.length() > (campo.startsWith("termo") || campo.startsWith("declaracao") || campo.startsWith("informacao") ? 5000 : 500)) invalido(campo);
                if (campo.equals("modoCondicaoPagamento") && !Set.of("PARCELAMENTO_LIVRE", "CONDICOES_PAGAMENTO").contains(valor)) invalido(campo);
            } else if (campo.equals("feriados")) {
                validarFeriados(valor);
            } else if (campo.equals("indisponibilidades")) {
                validarIndisponibilidades(valor);
            } else if (campo.equals("contatos")) {
                validarContatosFiscais(valor);
            } else if (campo.equals("autorizacoes")) {
                validarAutorizacoesFiscais(valor);
            } else if (campo.equals("series")) {
                validarSeriesFiscais(valor);
            }
        });
        if (secao.equals("INTEGRACOES")) {
            for (String campo : List.of("areaPintura", "areaTroca", "areaReparacao")) {
                Object valor = dados.get(campo);
                if (valor != null && !String.valueOf(valor).isBlank() && String.valueOf(valor).length() > 100) invalido(campo);
            }
        }
    }

    private void validarContatosFiscais(Object valor) {
        if (!(valor instanceof List<?> lista) || lista.size() > 30) invalido("contatos");
        for (Object item : (List<?>) valor) {
            if (!(item instanceof Map<?, ?> contato) || !(contato.get("tipo") instanceof String tipo)
                    || !Set.of("E-mail", "Celular", "Fax", "Telefone", "WhatsApp", "Estrangeiro").contains(tipo)
                    || !(contato.get("valor") instanceof String texto) || texto.length() > 254) invalido("contatos");
        }
    }

    private void validarAutorizacoesFiscais(Object valor) {
        if (!(valor instanceof List<?> lista) || lista.size() > 200) invalido("autorizacoes");
        for (Object item : (List<?>) valor) {
            if (!(item instanceof Map<?, ?> autorizacao) || !(autorizacao.get("documento") instanceof String documento)
                    || documento.replaceAll("\\D", "").length() < 11 || documento.replaceAll("\\D", "").length() > 14
                    || !(autorizacao.get("email") instanceof String email) || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) invalido("autorizacoes");
        }
    }

    private void validarSeriesFiscais(Object valor) {
        if (!(valor instanceof List<?>) || ((List<?>) valor).size() > 100) invalido("series");
        List<?> lista = (List<?>) valor;
        int principais = 0;
        for (Object item : lista) {
            if (!(item instanceof Map<?, ?> serie) || !(serie.get("serie") instanceof String texto) || texto.isBlank() || texto.length() > 20
                    || !(serie.get("numeroInicial") instanceof Number numero) || numero.longValue() < 1
                    || !(serie.get("principal") instanceof Boolean principal)) invalido("series");
            if (Boolean.TRUE.equals(((Map<?, ?>) item).get("principal"))) principais++;
        }
        if (!lista.isEmpty() && principais != 1) invalido("series");
    }

    private void validarFeriados(Object valor) {
        if (!(valor instanceof List<?>)) invalido("feriados");
        List<?> lista = (List<?>) valor;
        if (lista.size() > 200) invalido("feriados");
        for (Object item : lista) {
            if (!(item instanceof String data) || !(validarData(data, "dd/MM") || validarData(data, "dd/MM/yyyy"))) invalido("feriados");
        }
    }

    private void validarIndisponibilidades(Object valor) {
        if (!(valor instanceof List<?>)) invalido("indisponibilidades");
        List<?> lista = (List<?>) valor;
        if (lista.size() > 1000) invalido("indisponibilidades");
        for (Object item : lista) {
            if (!(item instanceof Map<?, ?>)) invalido("indisponibilidades");
            Map<?, ?> registro = (Map<?, ?>) item;
            Object descricao = registro.get("descricao"), data = registro.get("data"), inicio = registro.get("inicio"), fim = registro.get("fim");
            if (!(descricao instanceof String texto) || texto.isBlank() || texto.length() > 120
                    || !(data instanceof String dia) || !validarData(dia, "yyyy-MM-dd")
                    || !(inicio instanceof String horaInicio) || !validarHora(horaInicio)
                    || !(fim instanceof String horaFim) || !validarHora(horaFim)
                    || !horaFim.isEmpty() && !horaInicio.isEmpty() && horaFim.compareTo(horaInicio) <= 0) invalido("indisponibilidades");
            Object todos = registro.get("todosRecursos");
            Object recursos = registro.get("recursos");
            if (!(todos instanceof Boolean) || !(recursos instanceof Number quantidade) || quantidade.intValue() < 1 || quantidade.intValue() > 10000) invalido("indisponibilidades");
        }
    }

    private boolean validarData(String valor, String padrao) {
        if ("dd/MM".equals(padrao)) {
            try {
                String[] partes = valor.split("/");
                LocalDate.of(2000, Integer.parseInt(partes[1]), Integer.parseInt(partes[0]));
                return true;
            } catch (RuntimeException erro) { return false; }
        }
        try { LocalDate.parse(valor, DateTimeFormatter.ofPattern(padrao)); return true; }
        catch (DateTimeParseException erro) { return false; }
    }
    private boolean validarHora(String valor) {
        try { LocalTime.parse(valor); return true; }
        catch (DateTimeParseException erro) { return false; }
    }
    private static void invalido(String campo) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Valor inválido para o campo '" + campo + "'.");
    }

    private Configuracao buscar(Long empresa, String secao) {
        String tabela = TABELAS.get(secao);
        if (tabela == null) return null;
        return jdbc.query("SELECT * FROM " + tabela + " WHERE empresa_id=?", rs -> {
            if (!rs.next()) return null;
            Map<String, Object> dados = new LinkedHashMap<>();
            var metadados = rs.getMetaData();
            for (int indice = 1; indice <= metadados.getColumnCount(); indice++) {
                String campo = metadados.getColumnLabel(indice);
                if (Set.of("empresa_id", "versao", "atualizado_por", "atualizado_em").contains(campo)) continue;
                Object valor = rs.getObject(indice);
                if (valor != null) dados.put(camel(campo), valor);
            }
            carregarListas(empresa, secao, dados);
            return new Configuracao(empresa, empresa, secao, dados, rs.getInt("versao"), rs.getTimestamp("atualizado_em").toLocalDateTime());
        }, empresa);
    }

    private void carregarListas(Long empresa, String secao, Map<String, Object> dados) {
        switch (secao) {
            case "FERIADOS_LOCAIS" -> dados.put("feriados", jdbc.query("SELECT data_referencia FROM configuracoes_feriados_locais_itens WHERE empresa_id=? ORDER BY ordem", (rs,n) -> rs.getString(1), empresa));
            case "AGENDAMENTO" -> dados.put("indisponibilidades", jdbc.query("SELECT descricao,data_indisponibilidade,horario_inicio,horario_fim,todos_recursos,quantidade_recursos FROM configuracoes_agendamento_indisponibilidades WHERE empresa_id=? ORDER BY ordem,id", (rs,n) -> Map.of("descricao",rs.getString(1),"data",rs.getDate(2).toLocalDate().toString(),"inicio",rs.getTime(3).toLocalTime().toString(),"fim",rs.getTime(4).toLocalTime().toString(),"todosRecursos",rs.getBoolean(5),"recursos",rs.getInt(6)), empresa));
            case "FISCAL_GERAL" -> {
                dados.put("contatos", jdbc.query("SELECT tipo_contato,valor FROM contatos_fiscais_empresa WHERE empresa_id=? ORDER BY ordem,id", (rs,n) -> Map.of("tipo",rs.getString(1),"valor",rs.getString(2)), empresa));
                dados.put("autorizacoes", jdbc.query("SELECT documento,email FROM autorizacoes_fiscais_empresa WHERE empresa_id=? ORDER BY ordem,id", (rs,n) -> Map.of("documento",rs.getString(1),"email",rs.getString(2)), empresa));
            }
            case "FISCAL_NFE", "FISCAL_NFCE", "FISCAL_NFSE" -> {
                String tipo = secao.substring("FISCAL_".length());
                dados.put("series", jdbc.query("SELECT serie,numero_inicial,principal FROM series_documentos_fiscais_empresa WHERE empresa_id=? AND tipo_documento=? ORDER BY ordem,id", (rs,n) -> Map.of("serie",rs.getString(1),"numeroInicial",rs.getLong(2),"principal",rs.getBoolean(3)), empresa, tipo));
            }
        }
    }

    private void sincronizarListas(Long empresa, String secao, Map<String, Object> dados) {
        if (secao.equals("FERIADOS_LOCAIS") && dados.containsKey("feriados")) {
            jdbc.update("DELETE FROM configuracoes_feriados_locais_itens WHERE empresa_id=?", empresa);
            int ordem=0; for (Object data : (List<?>)dados.get("feriados")) jdbc.update("INSERT INTO configuracoes_feriados_locais_itens(empresa_id,data_referencia,ordem) VALUES(?,?,?)",empresa,data,ordem++);
        } else if (secao.equals("AGENDAMENTO") && dados.containsKey("indisponibilidades")) {
            jdbc.update("DELETE FROM configuracoes_agendamento_indisponibilidades WHERE empresa_id=?", empresa);
            int ordem=0; for (Object item : (List<?>)dados.get("indisponibilidades")) { Map<?,?> v=(Map<?,?>)item; jdbc.update("INSERT INTO configuracoes_agendamento_indisponibilidades(empresa_id,descricao,data_indisponibilidade,horario_inicio,horario_fim,todos_recursos,quantidade_recursos,ordem) VALUES(?,?,?,?,?,?,?,?)",empresa,v.get("descricao"),LocalDate.parse(String.valueOf(v.get("data"))),LocalTime.parse(String.valueOf(v.get("inicio"))),LocalTime.parse(String.valueOf(v.get("fim"))),v.get("todosRecursos"),v.get("recursos"),ordem++); }
        } else if (secao.equals("FISCAL_GERAL")) {
            if (dados.containsKey("contatos")) { jdbc.update("DELETE FROM contatos_fiscais_empresa WHERE empresa_id=?",empresa); int ordem=0; for(Object item:(List<?>)dados.get("contatos")){Map<?,?>v=(Map<?,?>)item;jdbc.update("INSERT INTO contatos_fiscais_empresa(empresa_id,tipo_contato,valor,ordem) VALUES(?,?,?,?)",empresa,v.get("tipo"),v.get("valor"),ordem++);} }
            if (dados.containsKey("autorizacoes")) { jdbc.update("DELETE FROM autorizacoes_fiscais_empresa WHERE empresa_id=?",empresa); int ordem=0; for(Object item:(List<?>)dados.get("autorizacoes")){Map<?,?>v=(Map<?,?>)item;jdbc.update("INSERT INTO autorizacoes_fiscais_empresa(empresa_id,documento,email,ordem) VALUES(?,?,?,?)",empresa,v.get("documento"),v.get("email"),ordem++);} }
        } else if (Set.of("FISCAL_NFE","FISCAL_NFCE","FISCAL_NFSE").contains(secao) && dados.containsKey("series")) {
            String tipo=secao.substring("FISCAL_".length()); jdbc.update("DELETE FROM series_documentos_fiscais_empresa WHERE empresa_id=? AND tipo_documento=?",empresa,tipo); int ordem=0; for(Object item:(List<?>)dados.get("series")){Map<?,?>v=(Map<?,?>)item;jdbc.update("INSERT INTO series_documentos_fiscais_empresa(empresa_id,tipo_documento,serie,numero_inicial,principal,ordem) VALUES(?,?,?,?,?,?)",empresa,tipo,v.get("serie"),v.get("numeroInicial"),v.get("principal"),ordem++);}
        }
    }

    private static String coluna(String campo) { return campo.replaceAll("([A-Z])", "_$1").toLowerCase(Locale.ROOT); }
    private static String camel(String coluna) { StringBuilder resultado=new StringBuilder(); boolean proximaMaiuscula=false; for(char caractere:coluna.toCharArray()){if(caractere=='_'){proximaMaiuscula=true;}else{resultado.append(proximaMaiuscula?Character.toUpperCase(caractere):caractere);proximaMaiuscula=false;}} return resultado.toString(); }
    private Long usuario(Long empresa) {
        var autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null || !autenticacao.isAuthenticated()) throw new AccessDeniedException("Sessão não autenticada.");
        return usuarios.findByEmailIgnoreCaseAndEmpresaId(autenticacao.getName(), empresa)
                .orElseThrow(() -> new AccessDeniedException("Usuário não pertence à empresa ativa.")).getId();
    }
    private String serializar(Object valor) {
        try { return json.writeValueAsString(valor); }
        catch (JsonProcessingException erro) { throw new IllegalStateException("Falha ao serializar configuração.", erro); }
    }
    private void auditar(Long empresa, Long id, String secao, Object anterior, Object novo, Long autor, String operacao) {
        jdbc.update("""
                INSERT INTO logs_alteracoes (empresa_id, tabela_afetada, registro_id, operacao, campos_alterados,
                    valores_antigos, valores_novos, usuario_responsavel, contexto_operacao, dados_adicionais, data_alteracao)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'ADM-013', ?::jsonb, CURRENT_TIMESTAMP)
                """, empresa, TABELAS.get(secao), id, operacao, serializar(List.of(secao)), serializar(Collections.singletonMap(secao, anterior)),
                serializar(Collections.singletonMap(secao, novo)), autor, serializar(Map.of("secao", secao)));
    }
}
