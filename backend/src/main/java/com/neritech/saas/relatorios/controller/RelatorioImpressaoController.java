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

import com.neritech.saas.ordemservico.dto.OrdemServicoResponse;
import com.neritech.saas.ordemservico.service.OrdemServicoService;
import com.neritech.saas.relatorios.service.JasperRelatorioService;

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
            com.neritech.saas.financeiro.repository.ComissaoFuncionarioRepository comissaoRepository) {
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
    }

    @GetMapping("/os/{id}")
    @Operation(summary = "Imprimir Ordem de Serviço (PDF)")
    public ResponseEntity<byte[]> imprimirOS(@PathVariable Long id) {
        // 1. Buscar dados da OS e Itens
        OrdemServicoResponse os = osService.findById(id);
        var produtos = itemProdutoService.findByOrdemServicoId(id);
        var servicos = itemServicoService.findByOrdemServicoId(id);

        // 2. Montar Lista Unificada de Itens
        java.util.List<com.neritech.saas.relatorios.dto.RelatorioItemDTO> itens = new java.util.ArrayList<>();

        produtos.forEach(p -> itens.add(new com.neritech.saas.relatorios.dto.RelatorioItemDTO(
                p.descricao(), p.quantidade(), p.valorUnitario(), p.valorFinal(), "PRODUTO")));

        servicos.forEach(s -> itens.add(new com.neritech.saas.relatorios.dto.RelatorioItemDTO(
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

    @GetMapping("/clientes")
    @Operation(summary = "Relatório de Clientes")
    public ResponseEntity<byte[]> relatorioClientes() {
        var lista = clienteRepository.findAll();

        var dados = lista.stream().map(c -> {
            Map<String, Object> m = new HashMap<>();
            m.put("nome", c.getNomeCompleto());
            m.put("email", "N/A"); // Cliente entity nao tem email direto visivel aqui, placeholder
            return m;
        }).toList();

        byte[] pdfBytes = jasperService.gerarRelatorioPdf("clientes", criarParamsPadrao(), dados);
        return retornarPdf(pdfBytes, "relatorio-clientes");
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
    public ResponseEntity<byte[]> relatorioAniversariantes(@RequestParam Integer mes) {
        var lista = clienteRepository.findByMesAniversario(mes);

        var dados = lista.stream().map(c -> {
            Map<String, Object> m = new HashMap<>();
            m.put("nome", c.getNomeCompleto());
            m.put("dataNascimento", c.getDataNascimento().toString());
            return m;
        }).toList();

        byte[] pdfBytes = jasperService.gerarRelatorioPdf("aniversariantes", criarParamsPadrao(), dados);
        return retornarPdf(pdfBytes, "relatorio-aniversariantes");
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
