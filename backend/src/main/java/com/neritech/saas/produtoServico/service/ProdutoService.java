package com.neritech.saas.produtoServico.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neritech.saas.produtoServico.domain.CategoriaProduto;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.domain.UnidadeMedida;
import com.neritech.saas.produtoServico.domain.ProdutoFiscal;
import com.neritech.saas.estoque.domain.Estoque;
import com.neritech.saas.estoque.repository.EstoqueRepository;
import com.neritech.saas.estoque.repository.ItemInventarioRepository;
import com.neritech.saas.produtoServico.dto.ProdutoRequest;
import com.neritech.saas.produtoServico.dto.ProdutoResponse;
import com.neritech.saas.produtoServico.mapper.ProdutoMapper;
import com.neritech.saas.produtoServico.repository.CategoriaProdutoRepository;
import com.neritech.saas.produtoServico.repository.ProdutoRepository;
import com.neritech.saas.produtoServico.repository.UnidadeMedidaRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaProdutoRepository categoriaRepository;
    private final UnidadeMedidaRepository unidadeMedidaRepository;
    private final EstoqueRepository estoqueRepository;
    private final ItemInventarioRepository itemInventarioRepository;
    private final ProdutoMapper mapper;
    private final com.neritech.saas.relatorios.repository.LogAlteracaoRepository logAlteracaoRepository;
    private final com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository usuarioRepository;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public ProdutoService(ProdutoRepository repository,
            CategoriaProdutoRepository categoriaRepository,
            UnidadeMedidaRepository unidadeMedidaRepository,
            EstoqueRepository estoqueRepository,
            ItemInventarioRepository itemInventarioRepository,
            ProdutoMapper mapper,
            com.neritech.saas.relatorios.repository.LogAlteracaoRepository logAlteracaoRepository,
            com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository usuarioRepository,
            com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.unidadeMedidaRepository = unidadeMedidaRepository;
        this.estoqueRepository = estoqueRepository;
        this.itemInventarioRepository = itemInventarioRepository;
        this.mapper = mapper;
        this.logAlteracaoRepository = logAlteracaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ProdutoResponse create(ProdutoRequest request) {
        if (repository.existsByEmpresaIdAndCodigoInterno(request.empresaId(), request.codigoInterno())) {
            throw new IllegalArgumentException("JÃ¡ existe um produto com este cÃ³digo interno nesta empresa");
        }

        CategoriaProduto categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findById(request.categoriaId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Categoria nÃ£o encontrada com ID: " + request.categoriaId()));
        }

        UnidadeMedida unidadeMedida = null;
        if (request.unidadeMedidaId() != null) {
            unidadeMedida = unidadeMedidaRepository.findById(request.unidadeMedidaId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Unidade de medida nÃ£o encontrada com ID: " + request.unidadeMedidaId()));
        }

        Produto entity = mapper.toEntity(request);
        entity.setEmpresaId(request.empresaId());
        entity.setCategoria(categoria);
        entity.setUnidadeMedida(unidadeMedida);
        entity.setAtivo(true);

        Produto saved = repository.save(entity);

        if (request.quantidadeEstoque() != null) {
            Estoque estoque = new Estoque();
            estoque.setEmpresaId(saved.getEmpresaId());
            estoque.setProduto(saved);
            estoque.setQuantidadeAtual(request.quantidadeEstoque());
            estoque.setPrecoCustoLote(saved.getPrecoCusto() != null ? saved.getPrecoCusto() : java.math.BigDecimal.ZERO);
            estoqueRepository.save(estoque);
        }

        java.math.BigDecimal qty = request.quantidadeEstoque() != null ? request.quantidadeEstoque() : java.math.BigDecimal.ZERO;
        saveAuditLog(saved.getEmpresaId(), saved.getId(), "INSERT", "Produto criado",
            new ProdutoAuditState(java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO),
            new ProdutoAuditState(qty, saved.getPrecoVenda(), saved.getPrecoCusto())
        );

        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProdutoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> search(Long empresaId, String query, Pageable pageable) {
        return repository.search(empresaId, query, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public ProdutoResponse update(Long id, ProdutoRequest request) {
        Produto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));

        java.math.BigDecimal oldPrecoVenda = entity.getPrecoVenda();
        java.math.BigDecimal oldPrecoCusto = entity.getPrecoCusto();
        var oldEstoqueOpt = estoqueRepository.findFirstByEmpresaIdAndProdutoId(entity.getEmpresaId(), entity.getId());
        java.math.BigDecimal oldQty = oldEstoqueOpt.isPresent() ? oldEstoqueOpt.get().getQuantidadeAtual() : java.math.BigDecimal.ZERO;

        if (!entity.getEmpresaId().equals(request.empresaId())) {
            throw new IllegalArgumentException("Não é permitido alterar a empresa do produto");
        }

        if (!entity.getCodigoInterno().equals(request.codigoInterno()) &&
                repository.existsByEmpresaIdAndCodigoInterno(request.empresaId(), request.codigoInterno())) {
            throw new IllegalArgumentException("Já existe um produto com este código interno nesta empresa");
        }

        CategoriaProduto categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findById(request.categoriaId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Categoria não encontrada com ID: " + request.categoriaId()));
        }

        UnidadeMedida unidadeMedida = null;
        if (request.unidadeMedidaId() != null) {
            unidadeMedida = unidadeMedidaRepository.findById(request.unidadeMedidaId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Unidade de medida não encontrada com ID: " + request.unidadeMedidaId()));
        }

        mapper.updateEntityFromRequest(request, entity);
        entity.setCategoria(categoria);
        entity.setUnidadeMedida(unidadeMedida);
        entity.setAtivo(true);

        Produto saved = repository.save(entity);

        if (request.quantidadeEstoque() != null) {
            Estoque estoque = estoqueRepository.findFirstByEmpresaIdAndProdutoId(saved.getEmpresaId(), saved.getId())
                    .orElseGet(() -> {
                        Estoque novo = new Estoque();
                        novo.setEmpresaId(saved.getEmpresaId());
                        novo.setProduto(saved);
                        novo.setPrecoCustoLote(saved.getPrecoCusto() != null ? saved.getPrecoCusto() : java.math.BigDecimal.ZERO);
                        return novo;
                    });
            estoque.setQuantidadeAtual(request.quantidadeEstoque());
            estoqueRepository.save(estoque);
        }

        var newEstoqueOpt = estoqueRepository.findFirstByEmpresaIdAndProdutoId(saved.getEmpresaId(), saved.getId());
        java.math.BigDecimal newQty = newEstoqueOpt.isPresent() ? newEstoqueOpt.get().getQuantidadeAtual() : java.math.BigDecimal.ZERO;

        saveAuditLog(saved.getEmpresaId(), saved.getId(), "UPDATE", "Alteracao manual do produto",
            new ProdutoAuditState(oldQty, oldPrecoVenda, oldPrecoCusto),
            new ProdutoAuditState(newQty, saved.getPrecoVenda(), saved.getPrecoCusto())
        );

        return mapper.toResponse(saved);
    }

    private static class ProdutoAuditState {
        public java.math.BigDecimal quantidadeEstoque;
        public java.math.BigDecimal precoVenda;
        public java.math.BigDecimal precoCusto;

        public ProdutoAuditState(java.math.BigDecimal quantidadeEstoque, java.math.BigDecimal precoVenda, java.math.BigDecimal precoCusto) {
            this.quantidadeEstoque = quantidadeEstoque != null ? quantidadeEstoque : java.math.BigDecimal.ZERO;
            this.precoVenda = precoVenda != null ? precoVenda : java.math.BigDecimal.ZERO;
            this.precoCusto = precoCusto != null ? precoCusto : java.math.BigDecimal.ZERO;
        }
    }

    private Long getCurrentUserId() {
        try {
            org.springframework.security.core.Authentication auth = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                var userOpt = usuarioRepository.findByEmail(auth.getName());
                if (userOpt.isPresent()) {
                    return userOpt.get().getId();
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return 1L;
    }

    private void saveAuditLog(Long empresaId, Long produtoId, String operacao, String motivo, Object antigos, Object novos) {
        try {
            Long usuarioId = getCurrentUserId();
            String antigosJson = antigos != null ? objectMapper.writeValueAsString(antigos) : null;
            String novosJson = novos != null ? objectMapper.writeValueAsString(novos) : null;

            com.neritech.saas.relatorios.domain.LogAlteracao log = com.neritech.saas.relatorios.domain.LogAlteracao.builder()
                    .empresaId(empresaId)
                    .tabelaAfetada("produtos")
                    .registroId(produtoId)
                    .operacao(com.neritech.saas.relatorios.domain.enums.OperacaoAuditoria.valueOf(operacao))
                    .valoresAntigos(antigosJson)
                    .valoresNovos(novosJson)
                    .usuarioResponsavel(usuarioId)
                    .motivoAlteracao(motivo)
                    .dataAlteracao(java.time.LocalDateTime.now())
                    .build();

            logAlteracaoRepository.save(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public ProdutoResponse duplicar(Long id) {
        Produto original = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));

        Produto copia = new Produto();
        copia.setEmpresaId(original.getEmpresaId());
        copia.setCategoria(original.getCategoria());
        copia.setUnidadeMedida(original.getUnidadeMedida());
        
        // Gerar código interno único
        String novoCodigoInterno = original.getCodigoInterno() + "-copia";
        int contador = 1;
        while (repository.existsByEmpresaIdAndCodigoInterno(original.getEmpresaId(), novoCodigoInterno)) {
            novoCodigoInterno = original.getCodigoInterno() + "-copia" + contador;
            contador++;
        }
        copia.setCodigoInterno(novoCodigoInterno);
        
        // Código de barras deve ser nulo para evitar colisão de códigos exclusivos
        copia.setCodigoBarras(null);
        copia.setCodigoFabricante(original.getCodigoFabricante());
        copia.setNome(original.getNome() + " (Cópia)");
        copia.setDescricao(original.getDescricao());
        copia.setDescricaoDetalhada(original.getDescricaoDetalhada());
        copia.setMarca(original.getMarca());
        copia.setModelo(original.getModelo());
        copia.setAplicacao(original.getAplicacao());
        copia.setEspecificacoesTecnicas(original.getEspecificacoesTecnicas());
        copia.setPesoLiquido(original.getPesoLiquido());
        copia.setPesoBruto(original.getPesoBruto());
        copia.setDimensoesComprimento(original.getDimensoesComprimento());
        copia.setDimensoesLargura(original.getDimensoesLargura());
        copia.setDimensoesAltura(original.getDimensoesAltura());
        copia.setPrecoCompra(original.getPrecoCompra());
        copia.setPrecoCusto(original.getPrecoCusto());
        copia.setPrecoVenda(original.getPrecoVenda());
        copia.setMargemLucroPercentual(original.getMargemLucroPercentual());
        copia.setEstoqueMinimo(original.getEstoqueMinimo());
        copia.setEstoqueMaximo(original.getEstoqueMaximo());
        copia.setPontoReposicao(original.getPontoReposicao());
        copia.setControlaLote(original.getControlaLote());
        copia.setControlaValidade(original.getControlaValidade());
        copia.setDiasValidade(original.getDiasValidade());
        copia.setNcm(original.getNcm());
        copia.setCest(original.getCest());
        copia.setOrigemProduto(original.getOrigemProduto());
        copia.setFotoPrincipalUrl(original.getFotoPrincipalUrl());
        copia.setGarantiaMeses(original.getGarantiaMeses());
        copia.setObservacoes(original.getObservacoes());
        copia.setAtivo(true);
        copia.setDestaque(original.getDestaque());
        copia.setPromocional(original.getPromocional());
        copia.setPontosFidelidade(original.getPontosFidelidade());
        copia.setComissaoVendaPercentual(original.getComissaoVendaPercentual());
        copia.setEnderecoEstoque(original.getEnderecoEstoque());
        copia.setSetor(original.getSetor());
        copia.setDataVencimento(original.getDataVencimento());
        copia.setCodigoSubstituto1(original.getCodigoSubstituto1());
        copia.setCodigoSubstituto2(original.getCodigoSubstituto2());
        copia.setDescontoFornecedorPercentual(original.getDescontoFornecedorPercentual());

        // Dados Fiscais (ProdutoFiscal)
        if (original.getDadosFiscais() != null) {
            ProdutoFiscal originalFiscal = original.getDadosFiscais();
            ProdutoFiscal copiaFiscal = new ProdutoFiscal();
            copiaFiscal.setEmpresaId(originalFiscal.getEmpresaId());
            copiaFiscal.setCeanTrib(originalFiscal.getCeanTrib());
            copiaFiscal.setCfop(originalFiscal.getCfop());
            copiaFiscal.setNcm(originalFiscal.getNcm());
            copiaFiscal.setCest(originalFiscal.getCest());
            copiaFiscal.setUnComercial(originalFiscal.getUnComercial());
            copiaFiscal.setQtdComercial(originalFiscal.getQtdComercial());
            copiaFiscal.setValorUnitComercial(originalFiscal.getValorUnitComercial());
            copiaFiscal.setUnTrib(originalFiscal.getUnTrib());
            copiaFiscal.setQtdTrib(originalFiscal.getQtdTrib());
            copiaFiscal.setValorUnitTributavel(originalFiscal.getValorUnitTributavel());
            copiaFiscal.setTotalSeguro(originalFiscal.getTotalSeguro());
            copiaFiscal.setDesconto(originalFiscal.getDesconto());
            copiaFiscal.setTotalFrete(originalFiscal.getTotalFrete());
            copiaFiscal.setOutrasDespesas(originalFiscal.getOutrasDespesas());
            copiaFiscal.setValorTotalBruto(originalFiscal.getValorTotalBruto());
            copiaFiscal.setExTipi(originalFiscal.getExTipi());
            copiaFiscal.setIndicadorEscalaRelevante(originalFiscal.getIndicadorEscalaRelevante());
            copiaFiscal.setCnpjFabricante(originalFiscal.getCnpjFabricante());
            copiaFiscal.setCodigoBeneficioFiscal(originalFiscal.getCodigoBeneficioFiscal());
            copiaFiscal.setValorBrutoCompoeTotal(originalFiscal.getValorBrutoCompoeTotal());
            copiaFiscal.setPedidoCompra(originalFiscal.getPedidoCompra());
            copiaFiscal.setItemPedidoCompra(originalFiscal.getItemPedidoCompra());
            copiaFiscal.setNumeroFci(originalFiscal.getNumeroFci());
            copiaFiscal.setImpostoFederalAprox(originalFiscal.getImpostoFederalAprox());
            copiaFiscal.setImpostoEstadualAprox(originalFiscal.getImpostoEstadualAprox());

            // IPI
            copiaFiscal.setIpiSitTrib(originalFiscal.getIpiSitTrib());
            copiaFiscal.setIpiClasseEnquadramento(originalFiscal.getIpiClasseEnquadramento());
            copiaFiscal.setIpiCodEnquadramento(originalFiscal.getIpiCodEnquadramento());
            copiaFiscal.setIpiCnpjProdutor(originalFiscal.getIpiCnpjProdutor());
            copiaFiscal.setIpiCodSeloControle(originalFiscal.getIpiCodSeloControle());
            copiaFiscal.setIpiQtdSelo(originalFiscal.getIpiQtdSelo());
            copiaFiscal.setIpiTipoCalculo(originalFiscal.getIpiTipoCalculo());
            copiaFiscal.setIpiBaseCalc(originalFiscal.getIpiBaseCalc());
            copiaFiscal.setIpiAliquota(originalFiscal.getIpiAliquota());
            copiaFiscal.setIpiValorUnidTrib(originalFiscal.getIpiValorUnidTrib());
            copiaFiscal.setIpiValor(originalFiscal.getIpiValor());

            // ICMS
            copiaFiscal.setIcmsSitTrib(originalFiscal.getIcmsSitTrib());
            copiaFiscal.setIcmsOrigem(originalFiscal.getIcmsOrigem());
            copiaFiscal.setIcmsModBc(originalFiscal.getIcmsModBc());
            copiaFiscal.setIcmsRedBc(originalFiscal.getIcmsRedBc());
            copiaFiscal.setIcmsBaseCalc(originalFiscal.getIcmsBaseCalc());
            copiaFiscal.setIcmsAliquota(originalFiscal.getIcmsAliquota());
            copiaFiscal.setIcmsMotivoDesoneracao(originalFiscal.getIcmsMotivoDesoneracao());
            copiaFiscal.setIcmsValorDesoneracao(originalFiscal.getIcmsValorDesoneracao());
            copiaFiscal.setIcmsValor(originalFiscal.getIcmsValor());
            copiaFiscal.setIcmsBcStUfOrigem(originalFiscal.getIcmsBcStUfOrigem());
            copiaFiscal.setIcmsRetidoAntes(originalFiscal.getIcmsRetidoAntes());
            copiaFiscal.setIcmsBcStUfDest(originalFiscal.getIcmsBcStUfDest());
            copiaFiscal.setIcmsStUfDestino(originalFiscal.getIcmsStUfDestino());
            copiaFiscal.setIcmsAliqCalcCredito(originalFiscal.getIcmsAliqCalcCredito());
            copiaFiscal.setIcmsValorCredito(originalFiscal.getIcmsValorCredito());
            copiaFiscal.setIcmsModBcSt(originalFiscal.getIcmsModBcSt());
            copiaFiscal.setIcmsMargemValorAdicionado(originalFiscal.getIcmsMargemValorAdicionado());
            copiaFiscal.setIcmsPrecoUnitPautaSt(originalFiscal.getIcmsPrecoUnitPautaSt());
            copiaFiscal.setIcmsRedBcSt(originalFiscal.getIcmsRedBcSt());
            copiaFiscal.setIcmsBaseCalcSt(originalFiscal.getIcmsBaseCalcSt());
            copiaFiscal.setIcmsAliquotaSt(originalFiscal.getIcmsAliquotaSt());
            copiaFiscal.setIcmsValorSt(originalFiscal.getIcmsValorSt());
            copiaFiscal.setIcmsPercBcOpPropria(originalFiscal.getIcmsPercBcOpPropria());
            copiaFiscal.setIcmsUfPgtoSt(originalFiscal.getIcmsUfPgtoSt());
            copiaFiscal.setIcmsPercFcp(originalFiscal.getIcmsPercFcp());
            copiaFiscal.setIcmsValorFcp(originalFiscal.getIcmsValorFcp());
            copiaFiscal.setIcmsBcFcp(originalFiscal.getIcmsBcFcp());
            copiaFiscal.setIcmsValorBcRetSt(originalFiscal.getIcmsValorBcRetSt());
            copiaFiscal.setIcmsPercFcpRetSt(originalFiscal.getIcmsPercFcpRetSt());
            copiaFiscal.setIcmsVbCalcFcpRetAntSt(originalFiscal.getIcmsVbCalcFcpRetAntSt());
            copiaFiscal.setIcmsPercFcpRetAntSt(originalFiscal.getIcmsPercFcpRetAntSt());
            copiaFiscal.setIcmsValorBcFcpUfDest(originalFiscal.getIcmsValorBcFcpUfDest());

            // PIS
            copiaFiscal.setPisSitTrib(originalFiscal.getPisSitTrib());
            copiaFiscal.setPisTipoCalculo(originalFiscal.getPisTipoCalculo());
            copiaFiscal.setPisBaseCalc(originalFiscal.getPisBaseCalc());
            copiaFiscal.setPisAliquota(originalFiscal.getPisAliquota());
            copiaFiscal.setPisQv(originalFiscal.getPisQv());
            copiaFiscal.setPisRv(originalFiscal.getPisRv());
            copiaFiscal.setPisRvbc(originalFiscal.getPisRvbc());
            copiaFiscal.setPisValorUnidTrib(originalFiscal.getPisValorUnidTrib());
            copiaFiscal.setPisTipoCalculoSt(originalFiscal.getPisTipoCalculoSt());
            copiaFiscal.setPisAliquotaSt(originalFiscal.getPisAliquotaSt());
            copiaFiscal.setPisValorUnidTribSt(originalFiscal.getPisValorUnidTribSt());

            // COFINS
            copiaFiscal.setCofinsSitTrib(originalFiscal.getCofinsSitTrib());
            copiaFiscal.setCofinsBaseCalc(originalFiscal.getCofinsBaseCalc());
            copiaFiscal.setCofinsAliquota(originalFiscal.getCofinsAliquota());
            copiaFiscal.setCofinsValorUnidTrib(originalFiscal.getCofinsValorUnidTrib());
            copiaFiscal.setCofinsValor(originalFiscal.getCofinsValor());
            copiaFiscal.setCofinsTipoCalculoSt(originalFiscal.getCofinsTipoCalculoSt());
            copiaFiscal.setCofinsAliquotaSt(originalFiscal.getCofinsAliquotaSt());
            copiaFiscal.setCofinsValorUnidTribSt(originalFiscal.getCofinsValorUnidTribSt());

            // Importadas
            copiaFiscal.setImportValorBc(originalFiscal.getImportValorBc());
            copiaFiscal.setImportValorDespAduaneiras(originalFiscal.getImportValorDespAduaneiras());
            copiaFiscal.setImportValorImposto(originalFiscal.getImportValorImposto());
            copiaFiscal.setImportValorIof(originalFiscal.getImportValorIof());

            // Combustíveis
            copiaFiscal.setCombCodAnp(originalFiscal.getCombCodAnp());
            copiaFiscal.setCombPercGlp(originalFiscal.getCombPercGlp());
            copiaFiscal.setCombCodAutorizacaoCodif(originalFiscal.getCombCodAutorizacaoCodif());
            copiaFiscal.setCombUfConsumo(originalFiscal.getCombUfConsumo());
            copiaFiscal.setCombBcCide(originalFiscal.getCombBcCide());
            copiaFiscal.setCombAliquotaCide(originalFiscal.getCombAliquotaCide());
            copiaFiscal.setCombValorCide(originalFiscal.getCombValorCide());
            copiaFiscal.setCombDescAnp(originalFiscal.getCombDescAnp());
            copiaFiscal.setCombPercGlpDerivPetroleo(originalFiscal.getCombPercGlpDerivPetroleo());
            copiaFiscal.setCombPercGasNac(originalFiscal.getCombPercGasNac());
            copiaFiscal.setCombPercGasImp(originalFiscal.getCombPercGasImp());
            copiaFiscal.setCombValorPartida(originalFiscal.getCombValorPartida());

            // IBS e CBS
            copiaFiscal.setIbsSitTrib(originalFiscal.getIbsSitTrib());
            copiaFiscal.setIbsClassificacaoTrib(originalFiscal.getIbsClassificacaoTrib());
            copiaFiscal.setIbsAliquotaUf(originalFiscal.getIbsAliquotaUf());
            copiaFiscal.setIbsPercDiferimentoUf(originalFiscal.getIbsPercDiferimentoUf());
            copiaFiscal.setIbsPercRedAliquotaUf(originalFiscal.getIbsPercRedAliquotaUf());
            copiaFiscal.setIbsAliquotaMunicipio(originalFiscal.getIbsAliquotaMunicipio());
            copiaFiscal.setCbsAliquota(originalFiscal.getCbsAliquota());
            copiaFiscal.setCbsPercDiferimento(originalFiscal.getCbsPercDiferimento());
            copiaFiscal.setCbsPercRedAliquota(originalFiscal.getCbsPercRedAliquota());

            copia.setDadosFiscais(copiaFiscal);
        }

        Produto saved = repository.save(copia);

        // Inicializar estoque zerado
        Estoque estoque = new Estoque();
        estoque.setEmpresaId(saved.getEmpresaId());
        estoque.setProduto(saved);
        estoque.setQuantidadeAtual(java.math.BigDecimal.ZERO);
        estoque.setPrecoCustoLote(saved.getPrecoCusto() != null ? saved.getPrecoCusto() : java.math.BigDecimal.ZERO);
        estoqueRepository.save(estoque);

        // Registrar auditoria
        saveAuditLog(saved.getEmpresaId(), saved.getId(), "INSERT", "Produto duplicado a partir do ID " + id,
            new ProdutoAuditState(java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO),
            new ProdutoAuditState(java.math.BigDecimal.ZERO, saved.getPrecoVenda(), saved.getPrecoCusto())
        );

        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Produto nÃ£o encontrado com ID: " + id);
        }
        
        // Remove dependências no estoque para evitar violação de FK
        estoqueRepository.deleteByProdutoId(id);
        itemInventarioRepository.deleteByProdutoId(id);
        
        repository.deleteById(id);
    }

    @Transactional
    public ProdutoResponse updateFotoPath(Long id, String fotoPath) {
        Produto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
        entity.setFotoPrincipalUrl(fotoPath);
        Produto saved = repository.save(entity);
        return mapper.toResponse(saved);
    }
}
