package com.neritech.saas.empresa.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.empresa.domain.Empresa;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final AssinaturaEmpresaRepository assinaturaEmpresaRepository;
    private final PlanoAssinaturaRepository planoAssinaturaRepository;
    private final UnidadeMedidaRepository unidadeMedidaRepository;
    private final CategoriaProdutoRepository categoriaProdutoRepository;

    public EmpresaService(EmpresaRepository empresaRepository,
                          AssinaturaEmpresaRepository assinaturaEmpresaRepository,
                          PlanoAssinaturaRepository planoAssinaturaRepository,
                          UnidadeMedidaRepository unidadeMedidaRepository,
                          CategoriaProdutoRepository categoriaProdutoRepository) {
        this.empresaRepository = empresaRepository;
        this.assinaturaEmpresaRepository = assinaturaEmpresaRepository;
        this.planoAssinaturaRepository = planoAssinaturaRepository;
        this.unidadeMedidaRepository = unidadeMedidaRepository;
        this.categoriaProdutoRepository = categoriaProdutoRepository;
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
        validarEmpresa(empresa, null);
        Empresa saved = empresaRepository.save(empresa);
        createTrialSubscription(saved);
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

    private void createTrialSubscription(Empresa empresa) {
        PlanoAssinatura plano = planoAssinaturaRepository.findAll().stream()
                .filter(p -> p.getNivel() != null && p.getNivel() == 1)
                .findFirst()
                .orElse(null);

        if (plano != null) {
            AssinaturaEmpresa assinatura = new AssinaturaEmpresa();
            assinatura.setEmpresa(empresa);
            assinatura.setPlano(plano);
            assinatura.setDataInicio(LocalDate.now());
            assinatura.setDataFim(LocalDate.now().plusDays(plano.getPeriodoTesteDias() != null ? plano.getPeriodoTesteDias() : 15));
            assinatura.setValorMensal(plano.getPrecoMensal());
            assinatura.setStatus(StatusAssinatura.ATIVO);
            assinaturaEmpresaRepository.save(assinatura);
        }
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
        Empresa current = findById(id);
        current.setLogoPath(logoPath);
        return empresaRepository.save(current);
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
