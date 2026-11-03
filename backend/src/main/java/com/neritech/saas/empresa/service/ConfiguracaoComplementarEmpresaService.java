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
        return jdbc.query("SELECT id, empresa_id, secao, dados::text, versao, atualizado_em FROM configuracoes_complementares_empresa WHERE empresa_id=? ORDER BY secao",
                (rs, linha) -> montar(rs), empresa);
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
        if (atual == null) {
            Long id = jdbc.queryForObject("INSERT INTO configuracoes_complementares_empresa (empresa_id, secao, dados, atualizado_por) VALUES (?, ?, ?::jsonb, ?) RETURNING id",
                    Long.class, empresa, secao, serializar(alteracao.dados()), autor);
            auditar(empresa, id, secao, Map.of(), alteracao.dados(), autor, "INSERT");
            return buscar(empresa, secao);
        }
        if (alteracao.versao() == null || !Objects.equals(atual.versao(), alteracao.versao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Configuração alterada em outra sessão. Recarregue e tente novamente.");
        }
        int linhas = jdbc.update("UPDATE configuracoes_complementares_empresa SET dados=?::jsonb, versao=versao+1, atualizado_por=?, atualizado_em=CURRENT_TIMESTAMP WHERE id=? AND empresa_id=? AND versao=?",
                serializar(alteracao.dados()), autor, atual.id(), empresa, atual.versao());
        if (linhas == 0) throw new ResponseStatusException(HttpStatus.CONFLICT, "Configuração alterada em outra sessão.");
        auditar(empresa, atual.id(), secao, atual.dados(), alteracao.dados(), autor, "UPDATE");
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
        return jdbc.query("SELECT id, empresa_id, secao, dados::text, versao, atualizado_em FROM configuracoes_complementares_empresa WHERE empresa_id=? AND secao=?",
                rs -> rs.next() ? montar(rs) : null, empresa, secao);
    }
    private Configuracao montar(ResultSet rs) throws SQLException {
        try {
            return new Configuracao(rs.getLong("id"), rs.getLong("empresa_id"), rs.getString("secao"),
                    json.readValue(rs.getString("dados"), new TypeReference<>() {}), rs.getInt("versao"),
                    rs.getTimestamp("atualizado_em").toLocalDateTime());
        } catch (JsonProcessingException erro) { throw new IllegalStateException("Configuração persistida inválida", erro); }
    }
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
                VALUES (?, 'configuracoes_complementares_empresa', ?, ?, ?, ?, ?, ?, 'ADM-013', ?::jsonb, CURRENT_TIMESTAMP)
                """, empresa, id, operacao, serializar(List.of(secao)), serializar(Collections.singletonMap(secao, anterior)),
                serializar(Collections.singletonMap(secao, novo)), autor, serializar(Map.of("secao", secao)));
    }
}
