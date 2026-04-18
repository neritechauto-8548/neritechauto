package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.*;
import com.neritech.saas.financeiro.dto.FluxoCaixaRequest;
import com.neritech.saas.financeiro.dto.FluxoCaixaResponse;
import com.neritech.saas.financeiro.mapper.FluxoCaixaMapper;
import com.neritech.saas.financeiro.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FluxoCaixaService {

    private final FluxoCaixaRepository repository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final CentroCustoRepository centroCustoRepository;
    private final FluxoCaixaMapper mapper;

    @Transactional(readOnly = true)
    public Page<FluxoCaixaResponse> findAll(Long empresaId, Long contaBancariaId, Long centroCustoId,
                                            java.time.LocalDate dataInicio, java.time.LocalDate dataFim,
                                            Pageable pageable) {
        org.springframework.data.jpa.domain.Specification<FluxoCaixa> spec = (root, query, cb) -> cb.equal(root.get("empresaId"), empresaId);

        if (contaBancariaId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("contaBancaria").get("id"), contaBancariaId));
        }
        if (centroCustoId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("centroCusto").get("id"), centroCustoId));
        }
        if (dataInicio != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataMovimentacao"), dataInicio));
        }
        if (dataFim != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataMovimentacao"), dataFim));
        }

        return repository.findAll(spec, pageable).map(mapper::toResponse);
    }

    private void applyDefaults(FluxoCaixa entity, FluxoCaixaRequest request) {
        if (entity.getCategoria() == null || entity.getCategoria().isBlank()) {
            var tipo = request.tipoMovimentacao();
            String cat = tipo == com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.ENTRADA ? "RECEITA"
                    : tipo == com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.SAIDA ? "DESPESA" : "TRANSFERENCIA";
            entity.setCategoria(cat);
        }
        // Fix for JSONB column error: Ensure empty strings are converted to null
        if (entity.getAnexos() != null && (entity.getAnexos().isBlank() || "null".equals(entity.getAnexos()))) {
            entity.setAnexos(null);
        }
        if (entity.getDescricao() == null || entity.getDescricao().isBlank()) {
            entity.setDescricao("Movimento de caixa");
        }
        if (entity.getTipoMovimentacao() == null) {
            entity.setTipoMovimentacao(request.tipoMovimentacao());
        }
    }

    @Transactional(readOnly = true)
    public FluxoCaixaResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("LanÃ§amento de fluxo de caixa nÃ£o encontrado"));
    }

    @Transactional
    public FluxoCaixaResponse create(Long empresaId, FluxoCaixaRequest request) {
        FluxoCaixa entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context
        entity.setUsuarioResponsavel(1L);

        applyDefaults(entity, request);

        setRelationships(entity, request);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public FluxoCaixaResponse update(Long id, Long empresaId, FluxoCaixaRequest request) {
        FluxoCaixa entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("LanÃ§amento de fluxo de caixa nÃ£o encontrado"));

        mapper.updateEntityFromDTO(request, entity);
        applyDefaults(entity, request);
        setRelationships(entity, request);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        FluxoCaixa entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("LanÃ§amento de fluxo de caixa nÃ£o encontrado"));
        repository.delete(entity);
    }

    private void setRelationships(FluxoCaixa entity, FluxoCaixaRequest request) {
        if (request.contaBancariaId() != null) {
            ContaBancaria contaBancaria = contaBancariaRepository.findById(request.contaBancariaId())
                    .orElseThrow(() -> new RuntimeException("Conta bancÃ¡ria nÃ£o encontrada"));
            entity.setContaBancaria(contaBancaria);
        }

        if (request.centroCustoId() != null) {
            CentroCusto centroCusto = centroCustoRepository.findById(request.centroCustoId())
                    .orElseThrow(() -> new RuntimeException("Centro de custo nÃ£o encontrado"));
            entity.setCentroCusto(centroCusto);
        }

        // Note: pagamentoId and recebimentoId are stored as documentoId in FluxoCaixa
        // The service layer handles the logic to determine which ID to use
        if (request.pagamentoId() != null) {
            entity.setDocumentoId(request.pagamentoId());
        } else if (request.recebimentoId() != null) {
            entity.setDocumentoId(request.recebimentoId());
        }
    }
}
