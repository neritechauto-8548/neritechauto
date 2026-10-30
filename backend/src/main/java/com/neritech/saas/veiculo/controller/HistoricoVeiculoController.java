package com.neritech.saas.veiculo.controller;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.ItemOSProduto;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.ordemservico.repository.ItemOSProdutoRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.math.BigDecimal;

/**
 * Histórico por veículo. A origem é o cadastro persistido e as OS da empresa
 * autenticada; esta API não materializa nem inventa eventos de manutenção.
 */
@RestController
@RequestMapping("/v1/historicos-veiculos")
@PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
@Transactional(readOnly = true)
public class HistoricoVeiculoController {
    private final VeiculoRepository veiculos;
    private final OrdemServicoRepository ordens;
    private final ItemOSServicoRepository servicos;
    private final ItemOSProdutoRepository produtos;

    public HistoricoVeiculoController(VeiculoRepository veiculos, OrdemServicoRepository ordens,
            ItemOSServicoRepository servicos, ItemOSProdutoRepository produtos) {
        this.veiculos = veiculos;
        this.ordens = ordens;
        this.servicos = servicos;
        this.produtos = produtos;
    }

    public record VeiculoHistoricoRow(Long id, Long clienteId, String cliente, String documento,
        String placa, String modelo, String marca, Integer anoFabricacao, Integer anoModelo,
        String combustivel, BigDecimal cilindrada) {}
    public record ItemHistorico(Long id, String tipo, String descricao, BigDecimal quantidade,
        BigDecimal valorUnitario, BigDecimal desconto, BigDecimal valorFinal, Long comissionadoId) {}
    public record OrdemHistorico(Long id, String numero, LocalDateTime dataAbertura,
        LocalDateTime dataEntrega, String status, Boolean autorizada, String problema, String solucao,
        BigDecimal valorTotal, List<ItemHistorico> itens) {}

    private long tenant() {
        Long id = TenantContext.getCurrentTenant();
        if (id == null || id <= 0L)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Empresa da sessão não identificada");
        return id;
    }

    private Veiculo vehicle(Long id, long tenant) {
        Veiculo veiculo = veiculos.findByIdAndEmpresaId(id, tenant)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado"));
        if (veiculo.getCliente() == null || !Objects.equals(veiculo.getCliente().getEmpresaId(), tenant))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vínculo de cliente indisponível");
        return veiculo;
    }

    private VeiculoHistoricoRow row(Veiculo v) {
        var c = v.getCliente();
        String nome = c.getNomeCompleto();
        if (nome == null || nome.isBlank()) nome = c.getNomeFantasia();
        if (nome == null || nome.isBlank()) nome = c.getRazaoSocial();
        return new VeiculoHistoricoRow(v.getId(), c.getId(), nome == null ? "—" : nome,
            c.getCpf() == null ? c.getCnpj() : c.getCpf(), v.getPlaca(),
            v.getModelo() == null ? null : v.getModelo().getNome(),
            v.getMarca() == null ? null : v.getMarca().getNome(),
            v.getAnoModelo() == null ? null : v.getAnoModelo().getAnoFabricacao(),
            v.getAnoModelo() == null ? null : v.getAnoModelo().getAnoModelo(),
            v.getTipoCombustivel() == null ? null : v.getTipoCombustivel().getNome(), v.getCilindrada());
    }

    private Page<Veiculo> search(String busca, String ordem, int page, int size, long tenant) {
        if (page < 0 || size < 1 || size > 100)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paginação inválida");
        Sort sort = switch (ordem == null ? "AZ" : ordem.toUpperCase(Locale.ROOT)) {
            case "ZA" -> Sort.by(Sort.Order.desc("cliente.nomeCompleto"), Sort.Order.asc("id"));
            case "PLACA" -> Sort.by(Sort.Order.asc("placa"), Sort.Order.asc("id"));
            default -> Sort.by(Sort.Order.asc("cliente.nomeCompleto"), Sort.Order.asc("id"));
        };
        String termo = busca == null ? "" : busca.trim().toLowerCase(Locale.ROOT);
        return veiculos.findAll((root, query, cb) -> {
            var cliente = root.join("cliente", JoinType.INNER);
            var tenantPredicate = cb.and(cb.equal(root.get("empresaId"), tenant),
                cb.equal(cliente.get("empresaId"), tenant));
            if (termo.isBlank()) return tenantPredicate;
            String like = "%" + termo.replace("%", "\\%").replace("_", "\\_") + "%";
            return cb.and(tenantPredicate, cb.or(
                cb.like(cb.lower(root.get("placa")), like, '\\'),
                cb.like(cb.lower(cliente.get("nomeCompleto")), like, '\\'),
                cb.like(cb.lower(cliente.get("razaoSocial")), like, '\\'),
                cb.like(cb.lower(cliente.get("nomeFantasia")), like, '\\'),
                cb.like(cb.lower(root.join("modelo", JoinType.LEFT).get("nome")), like, '\\')
            ));
        }, PageRequest.of(page, size, sort));
    }

    @GetMapping
    public Page<VeiculoHistoricoRow> listar(@RequestParam(defaultValue = "") String busca,
        @RequestParam(defaultValue = "AZ") String ordem,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return search(busca, ordem, page, size, tenant()).map(this::row);
    }

    @GetMapping("/{id}")
    public VeiculoHistoricoRow detalhe(@PathVariable Long id) {
        long empresa = tenant();
        return row(vehicle(id, empresa));
    }

    private Page<OrdemServico> orders(Long veiculoId, boolean incluirAutorizados, boolean soAutorizadas,
            String busca, int page, int size, long tenant, String ordenar) {
        if (page < 0 || size < 1 || size > 100)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paginação inválida");
        String termo = busca == null ? "" : busca.trim();
        return ordens.historicoPorVeiculo(tenant, veiculoId, incluirAutorizados, soAutorizadas, termo,
            PageRequest.of(page, size, "ENTRADA".equalsIgnoreCase(ordenar)
                ? Sort.by(Sort.Order.desc("dataAbertura"), Sort.Order.desc("id"))
                : Sort.by(Sort.Order.desc("numeroOS"), Sort.Order.desc("id"))));
    }

    private OrdemHistorico osRow(OrdemServico os, long tenant) {
        // Todos os itens são consultados apenas após confirmar a empresa da OS.
        if (os.getEmpresaId() == null || !Objects.equals(os.getEmpresaId(), tenant))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        List<ItemHistorico> itens = new ArrayList<>();
        for (ItemOSServico item : servicos.findByOrdemServicoId(os.getId())) {
            itens.add(new ItemHistorico(item.getId(), "SERVICO", item.getDescricao(),
                item.getQuantidade(), item.getValorUnitario(), item.getDescontoValor(),
                item.getValorFinal(), item.getMecanicoExecutorId()));
        }
        for (ItemOSProduto item : produtos.findByOrdemServicoId(os.getId())) {
            itens.add(new ItemHistorico(item.getId(), "PRODUTO", item.getDescricao(),
                item.getQuantidade(), item.getValorUnitario(), item.getDescontoValor(),
                item.getValorFinal(), null));
        }
        return new OrdemHistorico(os.getId(), os.getNumeroOS(), os.getDataAbertura(),
            os.getDataEntrega(), os.getStatus() == null ? "Não informado" : os.getStatus().getNome(),
            Boolean.TRUE.equals(os.getAprovadoCliente()), os.getProblemaRelatado(),
            os.getSolucaoAplicada(), os.getValorTotal(), itens);
    }

    @GetMapping("/{id}/ordens")
    public Page<OrdemHistorico> ordens(@PathVariable Long id,
        @RequestParam(defaultValue = "false") boolean incluirAutorizados,
        @RequestParam(defaultValue = "false") boolean soAutorizadas,
        @RequestParam(defaultValue = "") String busca,
        @RequestParam(defaultValue = "OS") String ordenar,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        long empresa = tenant();
        vehicle(id, empresa);
        return orders(id, incluirAutorizados, soAutorizadas, busca, page, size, empresa, ordenar)
            .map(os -> osRow(os, empresa));
    }

    /**
     * PDF gerado do servidor, com limites explícitos para evitar consumo excessivo.
     * Para relatório amplo sem filtro, selecionar no máximo 100 veículos.
     */
    @GetMapping(value = "/relatorio", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> relatorio(
        @RequestParam(required = false) Long veiculoId,
        @RequestParam(defaultValue = "") String busca,
        @RequestParam(defaultValue = "false") boolean soAutorizadas,
        @RequestParam(defaultValue = "false") boolean mostrarComissionado,
        @RequestParam(defaultValue = "false") boolean mostrarValores) {
        long empresa = tenant();
        List<Veiculo> selected;
        if (veiculoId != null) selected = List.of(vehicle(veiculoId, empresa));
        else {
            Page<Veiculo> pagina = search(busca, "AZ", 0, 100, empresa);
            if (pagina.getTotalElements() > 100)
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Refine a busca: máximo de 100 veículos por relatório");
            selected = pagina.getContent();
        }
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 36, 36, 42, 42);
            PdfWriter.getInstance(doc, output);
            doc.open();
            Font title = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
            Font sub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font body = FontFactory.getFont(FontFactory.HELVETICA, 9);
            doc.add(new Paragraph("NERITECH AUTO | Histórico de veículos", title));
            doc.add(new Paragraph("Empresa autenticada · Emitido em " + LocalDateTime.now(), body));
            doc.add(new Paragraph(" ", body));
            int totalOrdens = 0;
            for (Veiculo v : selected) {
                var info = row(v);
                doc.add(new Paragraph(info.cliente() + " — " + info.placa()
                    + " | " + (info.marca() == null ? "" : info.marca()) + " "
                    + (info.modelo() == null ? "" : info.modelo()), sub));
                // Reportável: até 150 OS por veículo. Não gerar documento incompleto.
                Page<OrdemServico> paginas = orders(v.getId(), true, soAutorizadas,
                    "", 0, 100, empresa, "ENTRADA");
                if (paginas.getTotalElements() > 100 || totalOrdens + paginas.getTotalElements() > 700)
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Histórico muito extenso. Refine por veículo para exportar.");
                totalOrdens += paginas.getTotalElements();
                if (paginas.isEmpty()) {
                    doc.add(new Paragraph("Nenhuma OS fechada ou autorizada encontrada.", body));
                    continue;
                }
                for (OrdemServico os : paginas) {
                    doc.add(new Paragraph("OS " + os.getNumeroOS()
                        + "  |  " + (os.getDataAbertura() == null ? "Sem data" : os.getDataAbertura())
                        + "  |  " + (os.getStatus() == null ? "—" : os.getStatus().getNome()), sub));
                    PdfPTable tabela = new PdfPTable(mostrarValores ? 3 : 1);
                    tabela.setWidthPercentage(100);
                    tabela.addCell("Descrição");
                    if (mostrarValores) { tabela.addCell("Desconto"); tabela.addCell("Valor final"); }
                    OrdemHistorico historico = osRow(os, empresa);
                    for (ItemHistorico item : historico.itens()) {
                        String label = (item.descricao() == null ? "Item" : item.descricao());
                        if (mostrarComissionado && item.comissionadoId() != null)
                            label += " (Responsável ID " + item.comissionadoId() + ")";
                        tabela.addCell(label);
                        if (mostrarValores) {
                            tabela.addCell(item.desconto() == null ? "—" : item.desconto().toPlainString());
                            tabela.addCell(item.valorFinal() == null ? "—" : item.valorFinal().toPlainString());
                        }
                    }
                    if (historico.itens().isEmpty()) tabela.addCell("Sem itens cadastrados");
                    doc.add(tabela);
                    if (mostrarValores) doc.add(new Paragraph("Total OS: "
                        + (os.getValorTotal() == null ? "0,00" : os.getValorTotal().toPlainString()), body));
                    doc.add(new Paragraph(" ", body));
                }
            }
            doc.close();
            byte[] bytes = output.toByteArray();
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"historico-veiculos.pdf\"").contentLength(bytes.length)
                .contentType(MediaType.APPLICATION_PDF).body(bytes);
        } catch (DocumentException | java.io.IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Não foi possível gerar o documento PDF", ex);
        }
    }
}
