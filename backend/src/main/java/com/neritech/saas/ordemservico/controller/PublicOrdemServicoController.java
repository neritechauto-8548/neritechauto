package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.OrdemServicoResponse;
import com.neritech.saas.ordemservico.service.OrdemServicoService;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.ordemservico.mapper.OrdemServicoMapper;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.common.tenancy.TenantFilterConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping({"/public/ordem-servico", "/api/public/ordem-servico"})
@RequiredArgsConstructor
@Tag(name = "Consulta Pública", description = "Endpoints para consulta de O.S. sem autenticação")
public class PublicOrdemServicoController {

    private final OrdemServicoService service;
    private final OrdemServicoRepository repository;
    private final OrdemServicoMapper mapper;
    private final TenantFilterConfiguration tenantConfig;

    @GetMapping("/consulta")
    @Operation(summary = "Consultar O.S. por CPF e Número")
    public ResponseEntity<OrdemServicoResponse> consultar(
            @RequestParam String cpf,
            @RequestParam String numeroOs) {
        
        // Remove caracteres não numéricos do CPF para busca
        String cpfLimpo = cpf.replaceAll("\\D", "");
        
        return service.findByCpfAndNumeroOS(cpfLimpo, numeroOs)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes da O.S. (Público)")
    public ResponseEntity<OrdemServicoResponse> getById(@PathVariable Long id) {
        // Busca global (bypass tenant filter inicialmente)
        OrdemServico os = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de serviço não encontrada"));
        
        // Define o tenant no contexto para que o mapeador consiga buscar cliente/veículo
        TenantContext.setCurrentTenant(os.getEmpresaId());
        
        // Re-ativa o filtro para a sessão atual se necessário, ou apenas deixar o context preenchido
        tenantConfig.enableTenantFilter();
        
        return ResponseEntity.ok(mapper.toResponse(os));
    }
}
