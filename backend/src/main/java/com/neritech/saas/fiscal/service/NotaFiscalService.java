package com.neritech.saas.fiscal.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.fiscal.domain.NotaFiscalInterna;
import com.neritech.saas.fiscal.repository.NotaFiscalInternaRepository;
import com.neritech.saas.fiscal.domain.StatusNotaFiscal;
import com.neritech.saas.fiscal.dto.NotaFiscalResponse;
import com.neritech.saas.fiscal.dto.NFeItemDTO;
import com.neritech.saas.ordemservico.domain.ItemOSProduto;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.repository.ItemOSProdutoRepository;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.relatorios.service.JasperRelatorioService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class NotaFiscalService {
    private final NotaFiscalInternaRepository repository;
    private final OrdemServicoRepository osRepository;
    private final ItemOSServicoRepository servicoRepo;
    private final ItemOSProdutoRepository produtoRepo;
    private final EmpresaRepository empresaRepo;
    private final ClienteRepository clienteRepo;
    private final JasperRelatorioService jasper;

    public NotaFiscalService(
            NotaFiscalInternaRepository repository,
            OrdemServicoRepository osRepository,
            ItemOSServicoRepository servicoRepo,
            ItemOSProdutoRepository produtoRepo,
            EmpresaRepository empresaRepo,
            ClienteRepository clienteRepo,
            JasperRelatorioService jasper
    ) {
        this.repository = repository;
        this.osRepository = osRepository;
        this.servicoRepo = servicoRepo;
        this.produtoRepo = produtoRepo;
        this.empresaRepo = empresaRepo;
        this.clienteRepo = clienteRepo;
        this.jasper = jasper;
    }

    public NotaFiscalResponse emitir(Long osId) {
        OrdemServico os = osRepository.findById(osId).orElseThrow();

        long proximoNumero = repository.findTopByEmpresaIdOrderByNumeroDesc(os.getEmpresaId())
                .map(NotaFiscalInterna::getNumero)
                .map(n -> n + 1)
                .orElse(1L);

        NotaFiscalInterna nf = new NotaFiscalInterna();
        nf.setEmpresaId(os.getEmpresaId());
        nf.setOrdemServicoId(osId);
        nf.setNumero(proximoNumero);
        nf.setSerie("001");
        nf.setDataEmissao(LocalDateTime.now());
        nf.setStatus(StatusNotaFiscal.EMITIDA);
        nf = repository.save(nf);

        String downloadUrl = "/api/v1/ordens-servico/nfe/" + nf.getId() + "/download";
        return new NotaFiscalResponse(nf.getId(), nf.getEmpresaId(), nf.getOrdemServicoId(), nf.getNumero(), nf.getSerie(), nf.getDataEmissao(), nf.getStatus().name(), downloadUrl);
    }

    @Transactional(readOnly = true)
    public Resource gerarPdf(Long osId) {
        OrdemServico os = osRepository.findById(osId).orElseThrow();
        Empresa emp = empresaRepo.findById(os.getEmpresaId()).orElse(null);
        Cliente cli = os.getClienteId() != null ? clienteRepo.findById(os.getClienteId()).orElse(null) : null;
        List<ItemOSServico> servicos = servicoRepo.findByOrdemServicoId(osId);
        List<ItemOSProduto> produtos = produtoRepo.findByOrdemServicoId(osId);

        Map<String, Object> params = new HashMap<>();
        params.put("empresaRazao", emp != null ? Optional.ofNullable(emp.getRazaoSocial()).orElse(emp.getNomeFantasia()) : "");
        params.put("empresaCnpj", emp != null ? emp.getCnpj() : "");
        params.put("empresaIE", emp != null ? Optional.ofNullable(emp.getInscricaoEstadual()).orElse("") : "");
        params.put("empresaIM", emp != null ? Optional.ofNullable(emp.getInscricaoMunicipal()).orElse("") : "");
        params.put("osNumero", os.getNumeroOS());
        params.put("dataEmissao", LocalDateTime.now());
        params.put("nfNumero", repository.findTopByOrdemServicoIdOrderByIdDesc(osId).map(NotaFiscalInterna::getNumero).orElse(null));
        params.put("nfSerie", repository.findTopByOrdemServicoIdOrderByIdDesc(osId).map(NotaFiscalInterna::getSerie).orElse("001"));
        params.put("nfStatus", repository.findTopByOrdemServicoIdOrderByIdDesc(osId).map(n -> n.getStatus().name()).orElse("EMITIDA"));

        params.put("clienteNome", cli != null ? Optional.ofNullable(cli.getRazaoSocial()).orElse(Optional.ofNullable(cli.getNomeCompleto()).orElse("")) : "");
        params.put("clienteCpfCnpj", cli != null ? Optional.ofNullable(cli.getCnpj()).orElse(Optional.ofNullable(cli.getCpf()).orElse("")) : "");

        BigDecimal totalServ = os.getValorServicos() != null ? os.getValorServicos() : BigDecimal.ZERO;
        BigDecimal totalProd = os.getValorProdutos() != null ? os.getValorProdutos() : BigDecimal.ZERO;
        BigDecimal desconto = os.getValorDesconto() != null ? os.getValorDesconto() : BigDecimal.ZERO;
        BigDecimal acrescimo = os.getValorAcrescimo() != null ? os.getValorAcrescimo() : BigDecimal.ZERO;
        BigDecimal totalNota = os.getValorTotal() != null ? os.getValorTotal() : totalServ.add(totalProd).add(acrescimo).subtract(desconto);

        params.put("valorServicos", totalServ);
        params.put("valorProdutos", totalProd);
        params.put("desconto", desconto);
        params.put("acrescimo", acrescimo);
        params.put("valorTotal", totalNota);

        List<NFeItemDTO> itens = new ArrayList<>();
        for (ItemOSServico s : servicos) {
            itens.add(new NFeItemDTO(
                    s.getDescricao(),
                    s.getQuantidade(),
                    "UN",
                    s.getValorUnitario(),
                    s.getValorTotal(),
                    "5102"
            ));
        }
        for (ItemOSProduto p : produtos) {
            itens.add(new NFeItemDTO(
                    p.getDescricao(),
                    p.getQuantidade(),
                    "UN",
                    p.getValorUnitario(),
                    p.getValorTotal(),
                    "5102"
            ));
        }

        byte[] pdf = jasper.gerarRelatorioPdf("nfe_interna", params, itens);
        return new ByteArrayResource(pdf);
    }

    @Transactional(readOnly = true)
    public Resource gerarPdfByNotaId(Long notaId) {
        Long osId = repository.findById(notaId).map(NotaFiscalInterna::getOrdemServicoId).orElseThrow();
        return gerarPdf(osId);
    }
}
