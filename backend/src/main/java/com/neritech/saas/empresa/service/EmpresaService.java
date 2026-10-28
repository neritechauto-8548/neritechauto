package com.neritech.saas.empresa.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.dto.EmpresaRequest;
import com.neritech.saas.empresa.mapper.EmpresaMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.util.DocumentoValidator;
import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import com.neritech.saas.empresa.repository.PlanoAssinaturaRepository;
import com.neritech.saas.produtoServico.domain.UnidadeMedida;
import com.neritech.saas.produtoServico.repository.UnidadeMedidaRepository;
import com.neritech.saas.produtoServico.domain.CategoriaProduto;
import com.neritech.saas.produtoServico.repository.CategoriaProdutoRepository;
import com.neritech.saas.trial.service.TrialPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final AssinaturaEmpresaRepository assinaturaEmpresaRepository;
    private final PlanoAssinaturaRepository planoAssinaturaRepository;
    private final UnidadeMedidaRepository unidadeMedidaRepository;
    private final CategoriaProdutoRepository categoriaProdutoRepository;
    private final EmpresaAlteracaoService auditoria;

    public EmpresaService(EmpresaRepository empresaRepository,
                          AssinaturaEmpresaRepository assinaturaEmpresaRepository,
                          PlanoAssinaturaRepository planoAssinaturaRepository,
                          UnidadeMedidaRepository unidadeMedidaRepository,
                          CategoriaProdutoRepository categoriaProdutoRepository,
                          EmpresaAlteracaoService auditoria) {
        this.empresaRepository = empresaRepository;
        this.assinaturaEmpresaRepository = assinaturaEmpresaRepository;
        this.planoAssinaturaRepository = planoAssinaturaRepository;
        this.unidadeMedidaRepository = unidadeMedidaRepository;
        this.categoriaProdutoRepository = categoriaProdutoRepository;
        this.auditoria = auditoria;
    }

    @Transactional(readOnly = true)
    public Empresa findById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", "ID", id));
    }

    @Transactional(readOnly = true)
    public Optional<Empresa> findByCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj);
    }

    @Transactional(readOnly = true)
    public Page<Empresa> search(String cnpj, String razaoSocial, Pageable pageable) {
        if (cnpj != null && !cnpj.isBlank()) {
            Optional<Empresa> opt = empresaRepository.findByCnpj(cnpj);
            return opt.map(e -> new org.springframework.data.domain.PageImpl<>(java.util.List.of(e), pageable, 1))
                    .orElse(new org.springframework.data.domain.PageImpl<>(java.util.List.of(), pageable, 0));
        }
        if (razaoSocial != null && !razaoSocial.isBlank()) {
            return empresaRepository.findByRazaoSocialContainingIgnoreCase(razaoSocial, pageable);
        }
        return empresaRepository.findAll(pageable);
    }

    @Transactional
    public Empresa create(Empresa empresa) {
        return createTrial(empresa, null);
    }

    @Transactional
    public Empresa createTrial(Empresa empresa, Long planoId) {
        validarEmpresa(empresa, null);
        PlanoAssinatura plano = planoId == null
                ? planoAssinaturaRepository.findByAtivoTrue().stream()
                    .filter(p -> p.getNivel() != null && p.getNivel() == 1).findFirst()
                    .orElseThrow(() -> new IllegalStateException("Plano de teste não configurado."))
                : planoAssinaturaRepository.findById(planoId)
                    .filter(p -> Boolean.TRUE.equals(p.getAtivo()) && p.getNivel() != null
                            && (p.getNivel() == 1 || p.getNivel() == 2))
                    .orElseThrow(() -> new IllegalArgumentException("Plano comercial indisponível."));
        Empresa saved = empresaRepository.save(empresa);
        createTrialSubscription(saved, plano);
        preCarregarUnidadesMedida(saved);
        preCarregarCategorias(saved);
        return saved;
    }

    private void preCarregarUnidadesMedida(Empresa empresa) {
        record UnidadeSeed(String sigla, String nome) {}
        List<UnidadeSeed> seeds = List.of(
            new UnidadeSeed("PC", "Peça"),
            new UnidadeSeed("CX", "Caixa"),
            new UnidadeSeed("JG", "Jogo"),
            new UnidadeSeed("KT", "Kit"),
            new UnidadeSeed("MT", "Metro"),
            new UnidadeSeed("LT", "Litro"),
            new UnidadeSeed("KG", "Quilograma"),
            new UnidadeSeed("FR", "Frasco"),
            new UnidadeSeed("UN", "Unidade"),
            new UnidadeSeed("AMPOLA", "Ampola"),
            new UnidadeSeed("BALDE", "Balde"),
            new UnidadeSeed("BANDEJ", "Bandeja"),
            new UnidadeSeed("BARRA", "Barra"),
            new UnidadeSeed("BISNAG", "Bisnaga"),
            new UnidadeSeed("BLOCO", "Bloco"),
            new UnidadeSeed("BOBINA", "Bobina"),
            new UnidadeSeed("BOMB", "Bombona"),
            new UnidadeSeed("CAPS", "Cápsula"),
            new UnidadeSeed("CART", "Cartela"),
            new UnidadeSeed("CENTO", "Cento"),
            new UnidadeSeed("CJ", "Conjunto"),
            new UnidadeSeed("CM", "Centímetro"),
            new UnidadeSeed("CM2", "Centímetro Quadrado"),
            new UnidadeSeed("CX2", "Caixa com 2 Unidades"),
            new UnidadeSeed("CX3", "Caixa com 3 Unidades"),
            new UnidadeSeed("CX5", "Caixa com 5 Unidades"),
            new UnidadeSeed("CX10", "Caixa com 10 Unidades"),
            new UnidadeSeed("CX15", "Caixa com 15 Unidades"),
            new UnidadeSeed("CX20", "Caixa com 20 Unidades"),
            new UnidadeSeed("CX25", "Caixa com 25 Unidades"),
            new UnidadeSeed("CX50", "Caixa com 50 Unidades"),
            new UnidadeSeed("CX100", "Caixa com 100 Unidades"),
            new UnidadeSeed("DISP", "Display"),
            new UnidadeSeed("DUZIA", "Dúzia"),
            new UnidadeSeed("EMBAL", "Embalagem"),
            new UnidadeSeed("FARDO", "Fardo"),
            new UnidadeSeed("FOLHA", "Folha"),
            new UnidadeSeed("GALAO", "Galão"),
            new UnidadeSeed("GF", "Garrafa"),
            new UnidadeSeed("GRAMAS", "Gramas"),
            new UnidadeSeed("LATA", "Lata"),
            new UnidadeSeed("M2", "Metro Quadrado"),
            new UnidadeSeed("M3", "Metro Cúbico"),
            new UnidadeSeed("MILHEI", "Milheiro"),
            new UnidadeSeed("ML", "Mililitro"),
            new UnidadeSeed("MWH", "Megawatt Hora"),
            new UnidadeSeed("PACOTE", "Pacote"),
            new UnidadeSeed("PALETE", "Palete"),
            new UnidadeSeed("PARES", "Pares"),
            new UnidadeSeed("POTE", "Pote"),
            new UnidadeSeed("K", "Quilate"),
            new UnidadeSeed("RESMA", "Resma"),
            new UnidadeSeed("ROLO", "Rolo"),
            new UnidadeSeed("SACO", "Saco"),
            new UnidadeSeed("SACOLA", "Sacola"),
            new UnidadeSeed("TAMBOR", "Tambor"),
            new UnidadeSeed("TANQUE", "Tanque"),
            new UnidadeSeed("TON", "Tonelada"),
            new UnidadeSeed("TUBO", "Tubo"),
            new UnidadeSeed("VASILHAME", "Vasil"),
            new UnidadeSeed("VIDRO", "Vidro")
        );

        List<UnidadeMedida> unidades = new ArrayList<>();
        for (UnidadeSeed seed : seeds) {
            UnidadeMedida u = new UnidadeMedida();
            u.setEmpresaId(empresa.getId());
            u.setNome(seed.nome());
            u.setSigla(seed.sigla());
            u.setAtivo(true);
            unidades.add(u);
        }
        unidadeMedidaRepository.saveAll(unidades);
    }

    private void preCarregarCategorias(Empresa empresa) {
        List<String> nomes = List.of(
            "ABRAÇADEIRAS",
            "AR CONDICIONADO",
            "ACESSÓRIOS",
            "CÂMBIO",
            "CORREIAS",
            "COXIM",
            "DIESEL",
            "DIREÇÃO HIDRÁULICA",
            "ELÉTRICA",
            "FIAT",
            "FILTROS",
            "FREIO",
            "LATARIA",
            "LUBRIFICANTES E ADITIVOS",
            "MOTOR",
            "ÓLEO",
            "PARAFUSO / ARRUELAS / ABRAÇADEIRA",
            "PERSONALIZADOS",
            "PROTEÇÃO MOTOR",
            "REPAROS",
            "RETENTORES",
            "SUSPENSÃO",
            "TAMPA"
        );

        List<CategoriaProduto> categorias = new ArrayList<>();
        for (String nome : nomes) {
            CategoriaProduto c = new CategoriaProduto();
            c.setEmpresaId(empresa.getId());
            c.setNome(nome);
            c.setAtivo(true);
            categorias.add(c);
        }
        categoriaProdutoRepository.saveAll(categorias);
    }

    private void createTrialSubscription(Empresa empresa, PlanoAssinatura plano) {
        java.time.LocalDateTime inicioTeste = TrialPolicy.now();
        AssinaturaEmpresa assinatura = new AssinaturaEmpresa();
        assinatura.setEmpresa(empresa);
        assinatura.setPlano(plano);
        assinatura.setDataInicio(inicioTeste.toLocalDate());
        assinatura.setDataFim(inicioTeste.toLocalDate().plusDays(TrialPolicy.DURATION_DAYS));
        assinatura.setTrialEndsAt(inicioTeste.plusDays(TrialPolicy.DURATION_DAYS));
        assinatura.setValorMensal(plano.getPrecoMensal());
        assinatura.setStatus(StatusAssinatura.TESTE);
        assinaturaEmpresaRepository.save(assinatura);
    }

    /** Modifica somente a empresa da sessão, com controle de versão e auditoria atômica. */
    @Transactional
    public Empresa updateCadastro(Long id, EmpresaRequest request) {
        TenantAccess.require(id);
        Empresa current = findById(id);
        if (request.getVersao() != null && !Objects.equals(request.getVersao(), current.getVersao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Os dados foram alterados. Recarregue antes de salvar.");
        }
        Map<String, Object> antes = camposAuditaveis(current);
        EmpresaMapper.updateEntity(current, request);
        validarEmpresa(current, current);
        Empresa saved = empresaRepository.saveAndFlush(current);
        auditoria.registrar(id, "empresa", id, saved.getVersao(), antes, camposAuditaveis(saved));
        return saved;
    }

    private static Map<String, Object> camposAuditaveis(Empresa e) {
        Map<String, Object> campos = new LinkedHashMap<>();
        campos.put("nomeFantasia", e.getNomeFantasia());
        campos.put("razaoSocial", e.getRazaoSocial());
        campos.put("cnpj", e.getCnpj());
        campos.put("inscricaoEstadual", e.getInscricaoEstadual());
        campos.put("inscricaoMunicipal", e.getInscricaoMunicipal());
        campos.put("email", e.getEmail());
        campos.put("telefone", e.getTelefone());
        campos.put("site", e.getSite());
        campos.put("ativo", e.getAtivo());
        return campos;
    }

    @Transactional
    public Empresa update(Long id, Empresa empresa) {
        Empresa current = findById(id);
        validarEmpresa(empresa, current);
        empresa.setId(current.getId());
        return empresaRepository.save(empresa);
    }

    @Transactional
    public Empresa updateLogoPath(Long id, String logoPath) {
        TenantAccess.require(id);
        Empresa current = findById(id);
        String previous = current.getLogoPath();
        if (Objects.equals(previous, logoPath)) return current;
        String before = previous == null || previous.isBlank()
                ? "Sem logomarca" : "Versão " + current.getVersao();
        current.setLogoPath(logoPath);
        Empresa saved = empresaRepository.saveAndFlush(current);
        String after = logoPath == null || logoPath.isBlank()
                ? "Sem logomarca" : "Versão " + saved.getVersao();
        // Nunca colocar o caminho físico do arquivo no histórico.
        auditoria.registrar(id, "empresa", id, saved.getVersao(),
                Map.of("marca", before), Map.of("marca", after));
        return saved;
    }

    /**
     * Valida todas as regras de negócio da Empresa.
     *
     * @param empresa Dados novos a serem salvos
     * @param current Entidade atual (null no caso de criação)
     */
    private void validarEmpresa(Empresa empresa, Empresa current) {
        // ── RN-EMPRESA-01: Nome Fantasia obrigatório ──────────────────────────
        if (empresa.getNomeFantasia() == null || empresa.getNomeFantasia().isBlank()) {
            throw new BusinessException("O Nome Fantasia é obrigatório.");
        }
        if (empresa.getNomeFantasia().trim().length() < 2) {
            throw new BusinessException("O Nome Fantasia deve ter pelo menos 2 caracteres.");
        }

        // ── RN-EMPRESA-02: Razão Social obrigatória ───────────────────────────
        if (empresa.getRazaoSocial() == null || empresa.getRazaoSocial().isBlank()) {
            throw new BusinessException("A Razão Social é obrigatória.");
        }
        if (empresa.getRazaoSocial().trim().length() < 2) {
            throw new BusinessException("A Razão Social deve ter pelo menos 2 caracteres.");
        }

        // ── RN-EMPRESA-03: CPF/CNPJ obrigatório e válido ─────────────────────
        if (empresa.getCnpj() == null || empresa.getCnpj().isBlank()) {
            throw new BusinessException("O CPF ou CNPJ é obrigatório.");
        }
        String documentoNormalizado = empresa.getCnpj().replaceAll("[^a-zA-Z0-9]", "").toUpperCase(java.util.Locale.ROOT);
        // O validador de DV pode aceitar sequências uniformes (ex.: 00000000000000).
        // Bloqueie-as na entrada da Empresa sem alterar o contrato de outros módulos.
        if (documentoNormalizado.length() == 14
                && documentoNormalizado.chars().distinct().count() == 1) {
            throw new BusinessException("CPF ou CNPJ inválido: sequência repetida.");
        }
        if (!DocumentoValidator.isValidCpf(empresa.getCnpj()) && !DocumentoValidator.isValidCnpj(empresa.getCnpj())) {
            throw new BusinessException("CPF ou CNPJ inválido. Verifique os dígitos informados.");
        }

        // ── RN-EMPRESA-04: CNPJ único por empresa (exceto ela mesma na atualização) ──
        String docLimpo = empresa.getCnpj().replaceAll("[^A-Z0-9]", "");
        Optional<Empresa> existente = empresaRepository.findByCnpj(empresa.getCnpj());
        if (existente.isPresent()) {
            boolean ehAMesma = current != null && existente.get().getId().equals(current.getId());
            if (!ehAMesma) {
                throw new BusinessException("Já existe uma empresa cadastrada com este CPF/CNPJ.");
            }
        }

        // ── RN-EMPRESA-05: E-mail deve ser válido quando preenchido ───────────
        if (empresa.getEmail() != null && !empresa.getEmail().isBlank()) {
            String email = empresa.getEmail().trim();
            if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                throw new BusinessException("O e-mail informado não é válido.");
            }
        }

        // ── RN-EMPRESA-06: Site deve começar com http(s) quando preenchido ────
        if (empresa.getSite() != null && !empresa.getSite().isBlank()) {
            String site = empresa.getSite().trim();
            if (!site.matches("^https?://.+\\..+")) {
                throw new BusinessException("O site deve começar com http:// ou https://.");
            }
        }

        // ── RN-EMPRESA-07: Data de abertura não pode ser no futuro ────────────
        if (empresa.getDataAbertura() != null && empresa.getDataAbertura().isAfter(LocalDate.now())) {
            throw new BusinessException("A data de abertura da empresa não pode ser uma data futura.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa", "ID", id);
        }
        empresaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<AssinaturaEmpresa> findActiveSubscriptionByEmpresa(Long empresaId) {
        return assinaturaEmpresaRepository.findFirstByEmpresaIdOrderByDataFimDesc(empresaId);
    }
}
