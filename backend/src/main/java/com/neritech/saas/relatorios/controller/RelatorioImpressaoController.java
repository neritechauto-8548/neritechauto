package com.neritech.saas.relatorios.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.ordemservico.dto.OrdemServicoResponse;
import com.neritech.saas.ordemservico.service.OrdemServicoService;
import com.neritech.saas.relatorios.service.JasperRelatorioService;
import com.neritech.saas.relatorios.dto.RelatorioItemDTO;
import com.neritech.saas.relatorios.dto.OrdemServicoRelatorioDTO;
import com.neritech.saas.relatorios.dto.RelatorioClienteDTO;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.empresa.domain.Empresa;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/relatorios")
@Tag(name = "Relatórios de Impressão", description = "Geração de PDFs via JasperReports")
public class RelatorioImpressaoController {

    private final JasperRelatorioService jasperService;
    private final OrdemServicoService osService;
    private final com.neritech.saas.ordemservico.service.ItemOSProdutoService itemProdutoService;
    private final com.neritech.saas.ordemservico.service.ItemOSServicoService itemServicoService;

    // Repositories para queries diretas de relatório
    private final com.neritech.saas.ordemservico.repository.OrdemServicoRepository osRepository;
    private final com.neritech.saas.cliente.repository.ClienteRepository clienteRepository;
    private final com.neritech.saas.produtoServico.repository.ProdutoRepository produtoRepository;
    private final com.neritech.saas.estoque.repository.EstoqueRepository estoqueRepository;
    private final com.neritech.saas.financeiro.repository.ContasReceberRepository contasReceberRepository;
    private final com.neritech.saas.financeiro.repository.ContasPagarRepository contasPagarRepository;
    private final com.neritech.saas.financeiro.repository.ComissaoFuncionarioRepository comissaoRepository;
    private final com.neritech.saas.empresa.repository.EmpresaRepository empresaRepository;
    private final com.neritech.saas.veiculo.repository.VeiculoRepository veiculoRepository;
    private final com.neritech.saas.ordemservico.repository.ItemOSProdutoRepository itemOSProdutoRepository;
    private final com.neritech.saas.ordemservico.repository.ItemOSServicoRepository itemOSServicoRepository;

    public RelatorioImpressaoController(JasperRelatorioService jasperService,
            OrdemServicoService osService,
            com.neritech.saas.ordemservico.service.ItemOSProdutoService itemProdutoService,
            com.neritech.saas.ordemservico.service.ItemOSServicoService itemServicoService,
            com.neritech.saas.ordemservico.repository.OrdemServicoRepository osRepository,
            com.neritech.saas.cliente.repository.ClienteRepository clienteRepository,
            com.neritech.saas.produtoServico.repository.ProdutoRepository produtoRepository,
            com.neritech.saas.estoque.repository.EstoqueRepository estoqueRepository,
            com.neritech.saas.financeiro.repository.ContasReceberRepository contasReceberRepository,
            com.neritech.saas.financeiro.repository.ContasPagarRepository contasPagarRepository,
            com.neritech.saas.financeiro.repository.ComissaoFuncionarioRepository comissaoRepository,
            com.neritech.saas.empresa.repository.EmpresaRepository empresaRepository,
            com.neritech.saas.veiculo.repository.VeiculoRepository veiculoRepository,
            com.neritech.saas.ordemservico.repository.ItemOSProdutoRepository itemOSProdutoRepository,
            com.neritech.saas.ordemservico.repository.ItemOSServicoRepository itemOSServicoRepository) {
        this.jasperService = jasperService;
        this.osService = osService;
        this.itemProdutoService = itemProdutoService;
        this.itemServicoService = itemServicoService;
        this.osRepository = osRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
        this.contasReceberRepository = contasReceberRepository;
        this.contasPagarRepository = contasPagarRepository;
        this.comissaoRepository = comissaoRepository;
        this.empresaRepository = empresaRepository;
        this.veiculoRepository = veiculoRepository;
        this.itemOSProdutoRepository = itemOSProdutoRepository;
        this.itemOSServicoRepository = itemOSServicoRepository;
    }

    @GetMapping("/os/{id}")
    @Operation(summary = "Imprimir Ordem de Serviço (PDF)")
    public ResponseEntity<byte[]> imprimirOSBasica(@PathVariable Long id) {
        // 1. Buscar dados da OS e Itens
        OrdemServicoResponse os = osService.findById(id);
        var produtos = itemProdutoService.findByOrdemServicoId(id);
        var servicos = itemServicoService.findByOrdemServicoId(id);

        // 2. Montar Lista Unificada de Itens
        java.util.List<RelatorioItemDTO> itens = new java.util.ArrayList<>();

        produtos.forEach(p -> itens.add(new RelatorioItemDTO(
                p.descricao(), p.quantidade(), p.valorUnitario(), p.valorFinal(), "PRODUTO")));

        servicos.forEach(s -> itens.add(new RelatorioItemDTO(
                s.descricao(), s.quantidade(), s.valorUnitario(), s.valorFinal(), "SERVICO")));

        // 3. Montar Parâmetros
        Map<String, Object> params = new HashMap<>();
        params.put("NOME_EMPRESA", "Neritech Auto Center");
        params.put("NUMERO_OS", os.numeroOS());
        params.put("NOME_CLIENTE", os.nomeCliente() != null ? os.nomeCliente() : "Consumidor");
        params.put("PLACA_VEICULO", os.placaVeiculo() != null ? os.placaVeiculo() : "N/A");
        params.put("TOTAL_OS", os.valorTotal() != null ? os.valorTotal() : BigDecimal.ZERO);

        // 4. Gerar PDF com DataSource
        byte[] pdfBytes = jasperService.gerarRelatorioPdf("os", params, itens);

        return retornarPdf(pdfBytes, "os-" + os.numeroOS());
    }

    @GetMapping("/vendas")
    @Operation(summary = "Relatório de Vendas")
    public ResponseEntity<byte[]> relatorioVendas(@RequestParam Long empresaId) {
        // Exemplo simplificado: Pega todas as OS
        var lista = osRepository.findByEmpresaId(empresaId, org.springframework.data.domain.Pageable.unpaged())
                .getContent();

        // Mapear para DTO simples (Map<String, Object>) ou criar DTO específico
        var dados = lista.stream().map(os -> {
            Map<String, Object> m = new HashMap<>();
            m.put("numeroOS", os.getNumeroOS());
            m.put("valorTotal", os.getValorTotal());
            return m;
        }).toList();

        byte[] pdfBytes = jasperService.gerarRelatorioPdf("vendas", criarParamsPadrao(), dados);
        return retornarPdf(pdfBytes, "relatorio-vendas");
    }

    @GetMapping("/estoque")
    @Operation(summary = "Relatório de Estoque")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> relatorioEstoque() {
        // TODO: Filtrar por empresaId (pegar do token ou param). Por enquanto findAll
        var lista = estoqueRepository.findAll();

        var dados = lista.stream().map(e -> {
            Map<String, Object> m = new HashMap<>();
            m.put("nomeProduto", e.getProduto().getNome());
            m.put("quantidade", e.getQuantidadeAtual() != null ? e.getQuantidadeAtual().intValue() : 0);
            return m;
        }).toList();

        byte[] pdfBytes = jasperService.gerarRelatorioPdf("estoque", criarParamsPadrao(), dados);
        return retornarPdf(pdfBytes, "relatorio-estoque");
    }

    @GetMapping("/estoque/etiquetas")
    @Operation(summary = "Etiquetas de Produto/Estoque")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> etiquetasEstoque() {
        var lista = estoqueRepository.findAll();

        var dados = lista.stream().map(e -> {
            Map<String, Object> m = new HashMap<>();
            m.put("nomeProduto", e.getProduto().getNome());
            m.put("codigoBarras", e.getProduto().getCodigoBarras() != null && !e.getProduto().getCodigoBarras().isEmpty() ? e.getProduto().getCodigoBarras() : e.getProduto().getCodigoInterno());
            m.put("preco", e.getProduto().getPrecoVenda());
            return m;
        }).toList();

        byte[] pdfBytes = jasperService.gerarRelatorioPdf("etiquetas_produto", criarParamsPadrao(), dados);
        return retornarPdf(pdfBytes, "etiquetas-produtos");
    }

    @GetMapping("/imprimir-os/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Imprimir OS/Orçamento", description = "Gera o PDF da Ordem de Serviço ou Orçamento no padrão Ultracar")
    public ResponseEntity<byte[]> imprimirOS(@PathVariable Long id) {
        Long empresaId = TenantContext.getCurrentTenant();
        
        OrdemServico os = osRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new RuntimeException("OS não encontrada"));
        
        Cliente cliente = os.getClienteId() != null ? clienteRepository.findById(os.getClienteId()).orElse(null) : null;
        Veiculo veiculo = os.getVeiculoId() != null ? veiculoRepository.findById(os.getVeiculoId()).orElse(null) : null;
        Empresa empresa = empresaRepository.findById(empresaId).orElse(null);

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataEmis = sdf.format(new java.util.Date());

        // Mapeamento para o DTO do Relatório
        OrdemServicoRelatorioDTO dto = new OrdemServicoRelatorioDTO();
        dto.setNumeroOS(os.getNumeroOS());
        dto.setTipoOS(os.getTipoOS() != null ? os.getTipoOS().name() : "SERVIÇO");
        dto.setStatus(os.getStatus() != null ? os.getStatus().getNome() : "—");
        dto.setDataAbertura(os.getDataAbertura() != null ? os.getDataAbertura().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "—");
        
        if (cliente != null) {
            dto.setClienteNome(cliente.getNomeCompleto() != null ? cliente.getNomeCompleto() : cliente.getRazaoSocial());
            dto.setClienteDocumento(cliente.getCpf() != null ? cliente.getCpf() : cliente.getCnpj());
            dto.setClienteEmail(cliente.getEmail());
            // TODO: Buscar telefone e endereço se houver tabelas relacionadas
        }

        if (veiculo != null) {
            dto.setVeiculoPlaca(veiculo.getPlaca());
            dto.setVeiculoModelo(veiculo.getModelo() != null ? veiculo.getModelo().getNome() : "—");
            dto.setVeiculoMarca(veiculo.getMarca() != null ? veiculo.getMarca().getNome() : "—");
            dto.setVeiculoAno(veiculo.getAnoModelo() != null ? veiculo.getAnoModelo().getAnoModelo().toString() : "—");
            dto.setVeiculoKm(os.getQuilometragemEntrada() != null ? os.getQuilometragemEntrada().toString() : "—");
            dto.setVeiculoCor(veiculo.getCorExterna());
            dto.setVeiculoChassi(veiculo.getChassi());
        }

        dto.setValorServicos(os.getValorServicos());
        dto.setValorProdutos(os.getValorProdutos());
        dto.setValorDesconto(os.getValorDesconto());
        dto.setValorTotal(os.getValorTotal());

        // Campos Adicionais da OS
        dto.setProblemaRelatado(os.getProblemaRelatado());
        dto.setSolucaoAplicada(os.getSolucaoAplicada());
        dto.setObservacoesCliente(os.getObservacoesCliente());
        dto.setDataPromessa(os.getDataPromessa() != null ? os.getDataPromessa().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "—");
        dto.setDataEntrega(os.getDataEntrega() != null ? os.getDataEntrega().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "—");

        // Buscar Itens
        dto.setServicos(itemOSServicoRepository.findByOrdemServicoId(os.getId()).stream()
                .map(s -> OrdemServicoRelatorioDTO.ItemRelatorioDTO.builder()
                        .codigo("SERV")
                        .descricao(s.getDescricao())
                        .quantidade(s.getQuantidade())
                        .valorUnitario(s.getValorUnitario())
                        .valorTotal(s.getValorFinal())
                        .build())
                .toList());

        dto.setProdutos(itemOSProdutoRepository.findByOrdemServicoId(os.getId()).stream()
                .map(p -> OrdemServicoRelatorioDTO.ItemRelatorioDTO.builder()
                        .codigo("PEÇA")
                        .descricao(p.getDescricao())
                        .quantidade(p.getQuantidade())
                        .valorUnitario(p.getValorUnitario())
                        .valorTotal(p.getValorFinal())
                        .build())
                .toList());

        // Parâmetros da Empresa
        Map<String, Object> params = new HashMap<>();
        if (empresa != null) {
            params.put("NOME_EMPRESA", empresa.getNomeFantasia() != null ? empresa.getNomeFantasia() : empresa.getRazaoSocial());
            params.put("CNPJ_EMPRESA", empresa.getCnpj());
            params.put("TELEFONE_EMPRESA", ""); 
            params.put("ENDERECO_EMPRESA", ""); 
        }
        params.put("DATA_EMISSAO", dataEmis);

        byte[] pdf = jasperService.gerarRelatorioPdf("ordem_servico", params, java.util.List.of(dto));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=os_" + os.getNumeroOS() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/clientes")
    @Operation(summary = "Relatório de Clientes")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<byte[]> relatorioClientes(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipoCliente,
            @RequestParam(required = false) String origemCliente,
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false, defaultValue = "NeriTech Auto Center") String empresaNome,
            @RequestParam(required = false, defaultValue = "") String empresaEndereco,
            @RequestParam(required = false, defaultValue = "") String empresaTelefone,
            @RequestParam(required = false, defaultValue = "") String empresaCnpj,
            @RequestParam(required = false, defaultValue = "") String empresaEmail) {

        // Normalizar: null se em branco (a query nativa trata com CAST IS NULL)
        String statusParam  = (status       != null && !status.isBlank())       ? status       : null;
        String tipoParam    = (tipoCliente  != null && !tipoCliente.isBlank())  ? tipoCliente  : null;
        String origemParam  = (origemCliente!= null && !origemCliente.isBlank())? origemCliente: null;
        String buscaParam   = (busca        != null && !busca.isBlank())        ? busca        : null;

        java.time.LocalDateTime dtInicio = parseDate(dataInicio, false);
        java.time.LocalDateTime dtFim    = parseDate(dataFim,    true);

        Long empresaId = TenantContext.getCurrentTenant();
        var lista = clienteRepository.findForRelatorio(empresaId, statusParam, tipoParam, origemParam, buscaParam, dtInicio, dtFim);

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        var dados = lista.stream().map(c -> {
            String nome     = c.getNomeCompleto() != null ? c.getNomeCompleto() : (c.getRazaoSocial() != null ? c.getRazaoSocial() : "—");
            String doc      = c.getCpf() != null ? c.getCpf() : (c.getCnpj() != null ? c.getCnpj() : "—");
            String tipoLbl  = c.getTipoCliente() != null && "PESSOA_FISICA".equals(c.getTipoCliente().name()) ? "Pessoa Física" : "Pessoa Jurídica";
            String statusLbl = c.getStatus() != null ? switch (c.getStatus().name()) {
                case "ATIVO" -> "Ativo"; case "INATIVO" -> "Inativo"; case "BLOQUEADO" -> "Bloqueado"; default -> c.getStatus().name();
            } : "—";
            String origemLbl = c.getOrigemCliente() != null ? switch (c.getOrigemCliente().name()) {
                case "SITE" -> "Site"; case "TELEFONE" -> "Telefone"; case "INDICACAO" -> "Indicação";
                case "REDES_SOCIAIS" -> "Redes Sociais"; default -> "Outros";
            } : "—";
            String dataCad  = c.getDataCadastro() != null ? sdf.format(java.sql.Timestamp.valueOf(c.getDataCadastro())) : "—";
            String dataNasc = c.getDataNascimento() != null ? c.getDataNascimento().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "—";
            String sexoLbl  = c.getSexo() != null ? ("M".equals(c.getSexo().name()) ? "Masculino" : "Feminino") : "—";

            return new RelatorioClienteDTO(
                    nome, doc, tipoLbl, c.getEmail() != null ? c.getEmail() : "—",
                    origemLbl, statusLbl, dataCad, dataNasc, sexoLbl);
        }).toList();

        String filtrosDesc = buildFiltrosDesc(statusParam, tipoParam, origemParam, buscaParam, dataInicio, dataFim);
        Map<String, Object> params = new HashMap<>();
        params.put("NOME_EMPRESA",    empresaNome);
        params.put("ENDERECO_EMPRESA",empresaEndereco);
        params.put("TELEFONE_EMPRESA",empresaTelefone);
        params.put("CNPJ_EMPRESA",    empresaCnpj);
        params.put("EMAIL_EMPRESA",   empresaEmail);
        params.put("TOTAL_REGISTROS", (long) dados.size());
        params.put("FILTROS",         filtrosDesc);
        params.put("DATA_EMISSAO",    new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));

        byte[] pdfBytes = jasperService.gerarRelatorioPdf("clientes", params, dados);
        return retornarPdf(pdfBytes, "relatorio-clientes");
    }

    private java.time.LocalDateTime parseDate(String date, boolean endOfDay) {
        if (date == null || date.isBlank()) return null;
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(date);
            return endOfDay ? d.atTime(23, 59, 59) : d.atStartOfDay();
        } catch (Exception e) { return null; }
    }

    private String buildFiltrosDesc(String status, String tipo, String origem,
                                    String busca, String dataInicio, String dataFim) {
        java.util.List<String> parts = new java.util.ArrayList<>();
        if (status != null)    parts.add("Status: " + status);
        if (tipo   != null)    parts.add("Tipo: "   + tipo);
        if (origem != null)    parts.add("Origem: " + origem);
        if (busca  != null)    parts.add("Busca: \"" + busca + "\"");
        if (dataInicio != null)parts.add("De: " + dataInicio);
        if (dataFim    != null)parts.add("Até: " + dataFim);
        return parts.isEmpty() ? "Todos os clientes" : String.join(" · ", parts);
    }

    @GetMapping("/produtos")
    @Operation(summary = "Relatório de Produtos")
    public ResponseEntity<byte[]> relatorioProdutos() {
        var lista = produtoRepository.findAll();

        var dados = lista.stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("nome", p.getNome());
            m.put("precoVenda", p.getPrecoVenda());
            return m;
        }).toList();

        byte[] pdfBytes = jasperService.gerarRelatorioPdf("produtos", criarParamsPadrao(), dados);
        return retornarPdf(pdfBytes, "relatorio-produtos");
    }

    @GetMapping("/aniversariantes")
    @Operation(summary = "Relatório de Aniversariantes")
    public ResponseEntity<byte[]> relatorioAniversariantes(
            @RequestParam Integer mes,
            @RequestParam(required = false, defaultValue = "") String empresaNome,
            @RequestParam(required = false, defaultValue = "") String empresaEndereco,
            @RequestParam(required = false, defaultValue = "") String empresaTelefone,
            @RequestParam(required = false, defaultValue = "") String empresaCnpj,
            @RequestParam(required = false, defaultValue = "") String empresaEmail) {

        var lista = clienteRepository.findByMesAniversario(mes);

        var dados = lista.stream().map(c -> {
            Map<String, Object> m = new HashMap<>();
            String nome = c.getNomeCompleto() != null ? c.getNomeCompleto() : (c.getRazaoSocial() != null ? c.getRazaoSocial() : "Cliente sem nome");
            m.put("nome", nome);
            m.put("dataNascimento", c.getDataNascimento() != null ? c.getDataNascimento().toString() : "");
            return m;
        }).toList();

        // Nomes dos meses em português
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                           "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        String mesNome = (mes >= 1 && mes <= 12) ? meses[mes - 1] : mes.toString();

        Map<String, Object> params = new HashMap<>();
        params.put("NOME_EMPRESA", empresaNome.isEmpty() ? "NeriTech Auto Center" : empresaNome);
        params.put("ENDERECO_EMPRESA", empresaEndereco.isEmpty() ? "" : empresaEndereco);
        params.put("TELEFONE_EMPRESA", empresaTelefone.isEmpty() ? "" : empresaTelefone);
        params.put("CNPJ_EMPRESA", empresaCnpj.isEmpty() ? "" : empresaCnpj);
        params.put("EMAIL_EMPRESA", empresaEmail.isEmpty() ? "" : empresaEmail);
        params.put("MES_REFERENCIA", mesNome);
        params.put("DATA_EMISSAO", new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));

        byte[] pdfBytes = jasperService.gerarRelatorioPdf("aniversariantes", params, dados);
        return retornarPdf(pdfBytes, "relatorio-aniversariantes-mes-" + mes);
    }


    @GetMapping("/financeiro")
    @Operation(summary = "Relatório Financeiro (Receitas)")
    public ResponseEntity<byte[]> relatorioFinanceiro() {
        // Exemplo: Lista contas a receber
        var lista = contasReceberRepository.findAll();

        var dados = lista.stream().map(c -> {
            Map<String, Object> m = new HashMap<>();
            m.put("descricao", c.getNumeroTitulo());
            m.put("valor", c.getValorNominal());
            m.put("tipo", "RECEITA");
            return m;
        }).toList();

        byte[] pdfBytes = jasperService.gerarRelatorioPdf("financeiro", criarParamsPadrao(), dados);
        return retornarPdf(pdfBytes, "relatorio-financeiro");
    }

    @GetMapping("/comissoes")
    @Operation(summary = "Relatório de Comissões")
    public ResponseEntity<byte[]> relatorioComissoes(@RequestParam(required = false) Long funcionarioId) {
        // Se funcionarioId vier, filtra. Se nao, todos.
        // Assumindo findAll por enquanto pois repository padrao nao tem filtro complexo
        // exposto aqui facil
        var lista = comissaoRepository.findAll();

        var dados = lista.stream()
                .filter(c -> funcionarioId == null
                        || (c.getFuncionario() != null && c.getFuncionario().getId().equals(funcionarioId)))
                .map(c -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("funcionario", c.getFuncionario() != null ? c.getFuncionario().getNomeCompleto() : "N/A");
                    m.put("data", c.getDataCompetencia() != null ? c.getDataCompetencia().toString() : "");
                    m.put("valorBase", c.getBaseCalculo());
                    m.put("valorComissao", c.getValorComissao());
                    return m;
                }).toList();

        byte[] pdfBytes = jasperService.gerarRelatorioPdf("comissoes", criarParamsPadrao(), dados);
        return retornarPdf(pdfBytes, "relatorio-comissoes");
    }

    private Map<String, Object> criarParamsPadrao() {
        Map<String, Object> params = new HashMap<>();
        params.put("NOME_EMPRESA", "Neritech Auto Center");
        return params;
    }

    private ResponseEntity<byte[]> retornarPdf(byte[] pdfBytes, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
