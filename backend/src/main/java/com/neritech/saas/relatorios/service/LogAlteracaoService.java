package com.neritech.saas.relatorios.service;

import com.neritech.saas.relatorios.domain.LogAlteracao;
import com.neritech.saas.relatorios.dto.LogAlteracaoRequest;
import com.neritech.saas.relatorios.dto.LogAlteracaoResponse;
import com.neritech.saas.relatorios.mapper.LogAlteracaoMapper;
import com.neritech.saas.relatorios.repository.LogAlteracaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogAlteracaoService {

    private final LogAlteracaoRepository logRepository;
    private final LogAlteracaoMapper logMapper;
    private final com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository usuarioRepository;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Transactional
    public LogAlteracaoResponse create(LogAlteracaoRequest request) {
        LogAlteracao entity = logMapper.toEntity(request);
        entity.setDataAlteracao(LocalDateTime.now());
        LogAlteracao saved = logRepository.save(entity);
        return logMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<LogAlteracaoResponse> findByEmpresa(Long empresaId) {
        return logRepository.findByEmpresaId(empresaId).stream()
                .map(entity -> {
                    LogAlteracaoResponse resp = logMapper.toResponse(entity);
                    if (entity.getUsuarioResponsavel() != null) {
                        usuarioRepository.findById(entity.getUsuarioResponsavel()).ifPresent(u -> {
                            resp.setUsuarioNome(u.getNomeCompleto());
                        });
                    }
                    return resp;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<com.neritech.saas.relatorios.dto.ProdutoHistoricoDTO> getProdutoHistorico(Long empresaId, Long produtoId) {
        var logs = logRepository.findByEmpresaIdAndTabelaAfetadaAndRegistroIdOrderByDataAlteracaoAsc(empresaId, "produtos", produtoId);
        
        java.util.List<com.neritech.saas.relatorios.dto.ProdutoHistoricoDTO> historico = new java.util.ArrayList<>();
        java.math.BigDecimal previousQty = java.math.BigDecimal.ZERO;
        
        for (var log : logs) {
            java.math.BigDecimal qty = java.math.BigDecimal.ZERO;
            java.math.BigDecimal venda = java.math.BigDecimal.ZERO;
            java.math.BigDecimal custo = java.math.BigDecimal.ZERO;
            
            try {
                if (log.getValoresNovos() != null) {
                    var map = objectMapper.readValue(log.getValoresNovos(), java.util.Map.class);
                    if (map.containsKey("quantidadeEstoque")) {
                        qty = new java.math.BigDecimal(map.get("quantidadeEstoque").toString());
                    }
                    if (map.containsKey("precoVenda")) {
                        venda = new java.math.BigDecimal(map.get("precoVenda").toString());
                    }
                    if (map.containsKey("precoCusto")) {
                        custo = new java.math.BigDecimal(map.get("precoCusto").toString());
                    }
                }
            } catch (Exception e) {
                // ignore
            }
            
            String variacaoStr = "-";
            if (log.getOperacao() == com.neritech.saas.relatorios.domain.enums.OperacaoAuditoria.INSERT) {
                variacaoStr = "-";
            } else {
                java.math.BigDecimal diff = qty.subtract(previousQty);
                if (diff.compareTo(java.math.BigDecimal.ZERO) > 0) {
                    variacaoStr = "+" + diff.stripTrailingZeros().toPlainString();
                } else if (diff.compareTo(java.math.BigDecimal.ZERO) < 0) {
                    variacaoStr = diff.stripTrailingZeros().toPlainString();
                }
            }
            
            previousQty = qty;
            
            String colaborador = "SISTEMA";
            if (log.getUsuarioResponsavel() != null) {
                var u = usuarioRepository.findById(log.getUsuarioResponsavel()).orElse(null);
                if (u != null) {
                    colaborador = u.getNomeCompleto().toUpperCase();
                }
            }
            
            historico.add(com.neritech.saas.relatorios.dto.ProdutoHistoricoDTO.builder()
                .estoque(qty)
                .variacao(variacaoStr)
                .valorVenda(venda)
                .valorCompra(custo)
                .observacao(log.getMotivoAlteracao() != null ? log.getMotivoAlteracao() : "Alteracao manual do produto")
                .dataAlteracao(log.getDataAlteracao())
                .colaborador(colaborador)
                .build());
        }
        
        java.util.Collections.reverse(historico);
        return historico;
    }
}
