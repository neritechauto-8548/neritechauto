package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.*;
import com.neritech.saas.financeiro.dto.FluxoCaixaRequest;
import com.neritech.saas.financeiro.dto.FluxoCaixaResponse;
import com.neritech.saas.financeiro.mapper.FluxoCaixaMapper;
import com.neritech.saas.financeiro.repository.*;
import com.neritech.saas.empresa.repository.DepartamentoContabioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FluxoCaixaService {

    private final FluxoCaixaRepository repository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final DepartamentoContabioRepository departamentoContabioRepository;
    private final FluxoCaixaMapper mapper;
    private final ContasPagarRepository contasPagarRepository;
    private final ContasReceberRepository contasReceberRepository;
    private final FechamentoCaixaService fechamentoCaixaService;

    private FluxoCaixaResponse toResponseWithDates(FluxoCaixa entity) {
        return toResponseWithDates(entity, java.util.Collections.emptyMap(), java.util.Collections.emptyMap());
    }

    private FluxoCaixaResponse toResponseWithDates(FluxoCaixa entity,
            java.util.Map<Long, ContasPagar> cpMap,
            java.util.Map<Long, ContasReceber> crMap) {
        FluxoCaixaResponse dto = mapper.toResponse(entity);
        java.time.LocalDate dataVenc = null;
        java.time.LocalDate dataPag = null;

        if (dto.pagamentoId() != null) {
            var pg = cpMap.containsKey(dto.pagamentoId())
                ? cpMap.get(dto.pagamentoId())
                : contasPagarRepository.findById(dto.pagamentoId()).orElse(null);
            if (pg != null) {
                dataVenc = pg.getDataVencimento();
                dataPag = pg.getDataPagamento();
            }
        } else if (dto.recebimentoId() != null) {
            var rec = crMap.containsKey(dto.recebimentoId())
                ? crMap.get(dto.recebimentoId())
                : contasReceberRepository.findById(dto.recebimentoId()).orElse(null);
            if (rec != null) {
                dataVenc = rec.getDataVencimento();
                java.time.LocalDate dataRec = null;
                if (rec.getRecebimentos() != null && !rec.getRecebimentos().isEmpty()) {
                    dataRec = rec.getRecebimentos().get(rec.getRecebimentos().size() - 1).getDataRecebimento();
                }
                dataPag = dataRec;
            }
        }

        return new FluxoCaixaResponse(
                dto.id(),
                dto.empresaId(),
                dto.dataMovimento(),
                dto.descricao(),
                dto.tipoMovimentacao(),
                dto.valor(),
                dto.saldoAcumulado(),
                dto.contaBancariaId(),
                dto.contaBancariaNome(),
                dto.centroCustoId(),
                dto.centroCustoNome(),
                dto.planoContasId(),
                dto.planoContasNome(),
                dto.pagamentoId(),
                dto.recebimentoId(),
                dto.observacoes(),
                dto.createdAt(),
                dto.updatedAt(),
                dataVenc,
                dataPag,
                dto.formaPagamentoNome());
    }

    @Transactional(readOnly = true)
    public Page<FluxoCaixaResponse> findAll(Long empresaId, Long contaBancariaId, Long centroCustoId,
            java.time.LocalDate dataInicio, java.time.LocalDate dataFim, Boolean includeClosed,
            Pageable pageable) {
        org.springframework.data.jpa.domain.Specification<FluxoCaixa> spec = (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("contaBancaria", jakarta.persistence.criteria.JoinType.LEFT);
                root.fetch("centroCusto", jakarta.persistence.criteria.JoinType.LEFT);
                root.fetch("formaPagamento", jakarta.persistence.criteria.JoinType.LEFT);
            }
            return cb.equal(root.get("empresaId"), empresaId);
        };

        if (Boolean.TRUE.equals(includeClosed)) {
            // Se deseja incluir fechados (ex: buscando detalhamento de um caixa fechado específico),
            // não limitamos a dataInicio pela data do último fechamento.
        } else {
            LocalDate ultimaDataFechamento = fechamentoCaixaService.getUltimaDataFechamento(empresaId);
            if (ultimaDataFechamento != null) {
                if (dataInicio == null || !dataInicio.isAfter(ultimaDataFechamento)) {
                    dataInicio = ultimaDataFechamento.plusDays(1);
                }
            }
        }

        if (contaBancariaId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("contaBancaria").get("id"), contaBancariaId));
        }
        if (centroCustoId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("centroCusto").get("id"), centroCustoId));
        }
        if (dataInicio != null) {
            LocalDate finalDataInicio = dataInicio;
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataMovimentacao"), finalDataInicio));
        }
        if (dataFim != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataMovimentacao"), dataFim));
        }

        Page<FluxoCaixa> page = repository.findAll(spec, pageable);

        java.util.Set<Long> pagamentoIds = page.getContent().stream()
                .filter(e -> e.getTipoMovimentacao() == com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.SAIDA)
                .map(FluxoCaixa::getDocumentoId)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());

        java.util.Set<Long> recebimentoIds = page.getContent().stream()
                .filter(e -> e.getTipoMovimentacao() == com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.ENTRADA)
                .map(FluxoCaixa::getDocumentoId)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());

        java.util.Map<Long, ContasPagar> cpMap = new java.util.HashMap<>();
        if (!pagamentoIds.isEmpty()) {
            contasPagarRepository.findAllByIdsAndEmpresaId(pagamentoIds, empresaId)
                    .forEach(cp -> cpMap.put(cp.getId(), cp));
        }

        java.util.Map<Long, ContasReceber> crMap = new java.util.HashMap<>();
        if (!recebimentoIds.isEmpty()) {
            contasReceberRepository.findAllByIdsAndEmpresaId(recebimentoIds, empresaId)
                    .forEach(cr -> crMap.put(cr.getId(), cr));
        }

        return page.map(entity -> this.toResponseWithDates(entity, cpMap, crMap));
    }

    private void applyDefaults(FluxoCaixa entity, FluxoCaixaRequest request) {
        if (entity.getCategoria() == null || entity.getCategoria().isBlank()) {
            var tipo = request.tipoMovimentacao();
            String cat = tipo == com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.ENTRADA ? "RECEITA"
                    : tipo == com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.SAIDA ? "DESPESA"
                            : "TRANSFERENCIA";
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
                .map(this::toResponseWithDates)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento de fluxo de caixa não encontrado"));
    }

    @Transactional
    public FluxoCaixaResponse create(Long empresaId, FluxoCaixaRequest request) {
        java.time.LocalDate dataMov = request.dataMovimento() != null
                ? request.dataMovimento()
                : java.time.LocalDate.now();
        fechamentoCaixaService.validarCaixaAberto(empresaId, dataMov);

        FluxoCaixa entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context
        entity.setUsuarioResponsavel(1L);

        applyDefaults(entity, request);

        setRelationships(entity, request);

        return toResponseWithDates(repository.save(entity));
    }

    @Transactional
    public FluxoCaixaResponse update(Long id, Long empresaId, FluxoCaixaRequest request) {
        FluxoCaixa entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento de fluxo de caixa não encontrado"));

        fechamentoCaixaService.validarCaixaAberto(empresaId, entity.getDataMovimentacao());
        if (request.dataMovimento() != null) {
            fechamentoCaixaService.validarCaixaAberto(empresaId, request.dataMovimento());
        }

        mapper.updateEntityFromDTO(request, entity);
        applyDefaults(entity, request);
        setRelationships(entity, request);

        return toResponseWithDates(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        FluxoCaixa entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("LanÃ§amento de fluxo de caixa nÃ£o encontrado"));
        fechamentoCaixaService.validarCaixaAberto(empresaId, entity.getDataMovimentacao());
        repository.delete(entity);
    }

    private void setRelationships(FluxoCaixa entity, FluxoCaixaRequest request) {
        if (request.contaBancariaId() != null) {
            ContaBancaria contaBancaria = contaBancariaRepository.findById(request.contaBancariaId())
                    .orElseThrow(() -> new RuntimeException("Conta bancÃ¡ria nÃ£o encontrada"));
            entity.setContaBancaria(contaBancaria);
        }

        if (request.centroCustoId() != null) {
            com.neritech.saas.empresa.domain.DepartamentoContabio depto = departamentoContabioRepository
                    .findById(request.centroCustoId())
                    .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));
            entity.setCentroCusto(depto);
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
