package com.neritech.saas.veiculo.service;

import com.neritech.saas.veiculo.domain.AnoModelo;
import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import com.neritech.saas.veiculo.domain.TipoCombustivel;
import com.neritech.saas.veiculo.domain.enums.CategoriaVeiculo;
import com.neritech.saas.veiculo.repository.AnoModeloRepository;
import com.neritech.saas.veiculo.repository.MarcaVeiculoRepository;
import com.neritech.saas.veiculo.repository.ModeloVeiculoRepository;
import com.neritech.saas.veiculo.repository.TipoCombustivelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.neritech.saas.common.tenancy.TenantContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Carrega dados da tabela FIPE nas tabelas locais.
 * A carga é feita de forma assíncrona e com transações fracionadas por marca
 * para evitar estouro de memória ou timeout num único contexto JPA.
 *
 * API: https://parallelum.com.br/fipe/api/v2/
 *   GET /cars/brands                         → lista marcas de carros
 *   GET /cars/brands/{code}/models           → lista modelos de uma marca
 *   GET /cars/brands/{b}/models/{m}/years    → lista anos/modelos de um modelo
 */
@Component
public class FipeDataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(FipeDataLoader.class);
    private static final String FIPE_BASE = "https://parallelum.com.br/fipe/api/v2/cars";

    /** Impede que duas cargas rodem simultaneamente. */
    private final AtomicBoolean em_execucao = new AtomicBoolean(false);

    private final MarcaVeiculoRepository marcaRepo;
    private final ModeloVeiculoRepository modeloRepo;
    private final AnoModeloRepository anoModeloRepo;
    private final TipoCombustivelRepository combustivelRepo;
    private final RestTemplate restTemplate;

    public FipeDataLoader(MarcaVeiculoRepository marcaRepo,
                          ModeloVeiculoRepository modeloRepo,
                          AnoModeloRepository anoModeloRepo,
                          TipoCombustivelRepository combustivelRepo) {
        this.marcaRepo = marcaRepo;
        this.modeloRepo = modeloRepo;
        this.anoModeloRepo = anoModeloRepo;
        this.combustivelRepo = combustivelRepo;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void run(ApplicationArguments args) {
        // Carga automática desativada.
        // Use o endpoint POST /api/v1/admin/fipe/carregar para acionar manualmente.
    }

    /**
     * Inicia a carga FIPE em background.
     * Retorna false se uma carga já estiver em andamento.
     */
    public boolean iniciarCarga() {
        if (!em_execucao.compareAndSet(false, true)) {
            log.warn("[FIPE] Carga já em andamento, ignorando nova solicitação.");
            return false;
        }
        // Captura o tenantId da thread HTTP atual antes de entrar no contexto async
        Long tenantId = TenantContext.getCurrentTenant();
        carregarTudoAsync(tenantId);
        return true;
    }

    // ──────────────────────────────────────────────────────────────────────
    // Carga assíncrona principal
    // ──────────────────────────────────────────────────────────────────────

    @Async
    protected void carregarTudoAsync(Long tenantId) {
        try {
            // Propaga o tenant para a thread de background
            if (tenantId != null) {
                TenantContext.setCurrentTenant(tenantId);
            }
            log.info("[FIPE] Iniciando carga assíncrona (tenant={})...", tenantId);
            carregarCombustiveis();
            carregarMarcasEModelos();
            log.info("[FIPE] Carga concluída com sucesso.");
        } catch (Exception e) {
            log.error("[FIPE] Erro durante a carga: {}", e.getMessage(), e);
        } finally {
            TenantContext.clear();
            em_execucao.set(false);
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    // Combustíveis (dados estáticos – não vêm da FIPE)
    // ──────────────────────────────────────────────────────────────────────

    @Transactional
    protected void carregarCombustiveis() {
        for (String nome : List.of("Gasolina", "Álcool (Etanol)", "Flex (Gasolina/Etanol)",
                "Diesel", "GNV (Gás Natural)", "Elétrico", "Híbrido")) {
            combustivelRepo.findByNome(nome).orElseGet(() -> {
                TipoCombustivel tc = new TipoCombustivel();
                tc.setNome(nome);
                return combustivelRepo.save(tc);
            });
        }
        log.info("[FIPE] Tipos de combustível sincronizados.");
    }

    // ──────────────────────────────────────────────────────────────────────
    // Marcas → Modelos → Anos/Modelo  (1 transação por marca)
    // ──────────────────────────────────────────────────────────────────────

    private void carregarMarcasEModelos() {
        List<Map<String, String>> marcasFipe = fetchList(FIPE_BASE + "/brands");
        if (marcasFipe == null) return;

        log.info("[FIPE] {} marcas encontradas na FIPE.", marcasFipe.size());

        for (Map<String, String> marcaFipe : marcasFipe) {
            String codigoMarca = marcaFipe.get("code");
            String nomeMarca   = marcaFipe.get("name");
            try {
                carregarMarcaComTransacao(nomeMarca, codigoMarca);
            } catch (Exception e) {
                log.warn("[FIPE] Erro ao carregar marca '{}': {}", nomeMarca, e.getMessage());
            }
        }
    }

    /**
     * Cada marca tem sua própria transação para não sobrecarregar o contexto JPA.
     */
    @Transactional
    protected void carregarMarcaComTransacao(String nomeMarca, String codigoMarca) {
        MarcaVeiculo marca = marcaRepo.findByNomeIgnoreCase(nomeMarca).orElseGet(() -> {
            MarcaVeiculo m = new MarcaVeiculo();
            m.setNome(nomeMarca);
            return marcaRepo.save(m);
        });

        List<Map<String, String>> modelosFipe = fetchList(FIPE_BASE + "/brands/" + codigoMarca + "/models");
        if (modelosFipe == null) return;

        for (Map<String, String> mf : modelosFipe) {
            String codigoModelo = mf.get("code");
            String nomeModelo   = mf.get("name");

            ModeloVeiculo modelo = modeloRepo.findByMarcaIdAndNome(marca.getId(), nomeModelo).orElseGet(() -> {
                ModeloVeiculo mv = new ModeloVeiculo();
                mv.setMarca(marca);
                mv.setNome(nomeModelo);
                mv.setCategoria(CategoriaVeiculo.OUTROS);
                return modeloRepo.save(mv);
            });

            carregarAnosModelo(modelo, codigoMarca, codigoModelo);
        }

        log.debug("[FIPE] Marca '{}' sincronizada.", nomeMarca);
    }

    private void carregarAnosModelo(ModeloVeiculo modelo, String codigoMarca, String codigoModelo) {
        String url = FIPE_BASE + "/brands/" + codigoMarca + "/models/" + codigoModelo + "/years";
        List<Map<String, String>> anosFipe = fetchList(url);
        if (anosFipe == null) return;

        for (Map<String, String> af : anosFipe) {
            String code = af.get("code"); // ex: "2023-1"
            String[] partes = code.split("-");
            if (partes.length < 1) continue;

            int anoModelo;
            try {
                anoModelo = Integer.parseInt(partes[0]);
            } catch (NumberFormatException e) {
                continue;
            }
            int anoFabricacao = anoModelo;

            boolean existe = anoModeloRepo
                    .findByModeloIdAndAnoFabricacaoAndAnoModelo(modelo.getId(), anoFabricacao, anoModelo)
                    .isPresent();

            if (!existe) {
                AnoModelo am = new AnoModelo();
                am.setModelo(modelo);
                am.setAnoFabricacao(anoFabricacao);
                am.setAnoModelo(anoModelo);
                am.setCodigoFipe(code);
                anoModeloRepo.save(am);
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    // Helper HTTP
    // ──────────────────────────────────────────────────────────────────────

    private List<Map<String, String>> fetchList(String url) {
        try {
            ResponseEntity<List<Map<String, String>>> resp = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {});
            return resp.getBody();
        } catch (Exception e) {
            log.warn("[FIPE] Erro ao chamar {}: {}", url, e.getMessage());
            return null;
        }
    }
}
