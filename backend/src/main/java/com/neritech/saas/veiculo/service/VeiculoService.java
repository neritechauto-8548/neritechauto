package com.neritech.saas.veiculo.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.veiculo.domain.AnoModelo;
import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import com.neritech.saas.veiculo.domain.TipoCombustivel;
import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.veiculo.domain.enums.StatusVeiculo;
import com.neritech.saas.veiculo.dto.VeiculoRequest;
import com.neritech.saas.veiculo.dto.VeiculoResponse;
import com.neritech.saas.veiculo.mapper.VeiculoMapper;
import com.neritech.saas.veiculo.repository.AnoModeloRepository;
import com.neritech.saas.veiculo.repository.MarcaVeiculoRepository;
import com.neritech.saas.veiculo.repository.ModeloVeiculoRepository;
import com.neritech.saas.veiculo.repository.TipoCombustivelRepository;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class VeiculoService {

    private final VeiculoRepository repository;
    private final ClienteRepository clienteRepository;
    private final MarcaVeiculoRepository marcaRepository;
    private final ModeloVeiculoRepository modeloRepository;
    private final AnoModeloRepository anoModeloRepository;
    private final TipoCombustivelRepository tipoCombustivelRepository;
    private final VehicleExternalLookupService externalLookupService;
    private final VeiculoMapper mapper;

    public VeiculoService(
            VeiculoRepository repository,
            ClienteRepository clienteRepository,
            MarcaVeiculoRepository marcaRepository,
            ModeloVeiculoRepository modeloRepository,
            AnoModeloRepository anoModeloRepository,
            TipoCombustivelRepository tipoCombustivelRepository,
            VehicleExternalLookupService externalLookupService,
            VeiculoMapper mapper) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
        this.anoModeloRepository = anoModeloRepository;
        this.tipoCombustivelRepository = tipoCombustivelRepository;
        this.externalLookupService = externalLookupService;
        this.mapper = mapper;
    }

    public VeiculoResponse create(VeiculoRequest request) {
        Long tenantId = requireTenant();
        Cliente cliente = requireClienteDoTenant(request.clienteId());
        String placaNormalizada = normalizePlate(request.placa());

        repository.findByEmpresaIdAndPlaca(tenantId, placaNormalizada)
                .ifPresent(existing -> {
                    throw new BusinessException("Já existe um veículo com esta placa para esta empresa");
                });

        validateOdometerForCreate(request);

        Veiculo entity = mapper.toEntity(request);
        entity.setEmpresaId(tenantId);
        entity.setCliente(cliente);
        entity.setMarca(resolveMarca(request.marcaId()));
        entity.setModelo(resolveModelo(request.modeloId()));
        entity.setAnoModelo(resolveAnoModelo(request.anoModeloId()));
        entity.setTipoCombustivel(resolveCombustivel(request.combustivelId()));
        entity.setPlaca(placaNormalizada);
        entity.setChassi(normalizeUpperIdentifier(request.chassi()));
        entity.setRenavam(normalizeDigits(request.renavam()));

        if (entity.getStatus() == null) {
            entity.setStatus(StatusVeiculo.ATIVO);
        }

        return mapper.toResponse(repository.save(entity));
    }

    public VeiculoResponse update(Long id, VeiculoRequest request) {
        Long tenantId = requireTenant();
        Veiculo entity = repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));

        Cliente cliente = requireClienteDoTenant(request.clienteId());
        String placaNormalizada = normalizePlate(request.placa());

        repository.findByEmpresaIdAndPlaca(tenantId, placaNormalizada)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("Já existe um veículo com esta placa para esta empresa");
                });

        validateOdometerForUpdate(entity, request);

        mapper.updateEntityFromRequest(request, entity);
        entity.setCliente(cliente);
        entity.setMarca(resolveMarca(request.marcaId()));
        entity.setModelo(resolveModelo(request.modeloId()));
        entity.setAnoModelo(resolveAnoModelo(request.anoModeloId()));
        entity.setTipoCombustivel(resolveCombustivel(request.combustivelId()));
        entity.setPlaca(placaNormalizada);
        entity.setChassi(normalizeUpperIdentifier(request.chassi()));
        entity.setRenavam(normalizeDigits(request.renavam()));

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public VeiculoResponse findById(Long id) {
        Long tenantId = requireTenant();
        return repository.findByIdAndEmpresaId(id, tenantId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponse> findAll() {
        Long tenantId = requireTenant();
        return repository.findByEmpresaId(tenantId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponse> findByCliente(Long clienteId) {
        Long tenantId = requireTenant();
        if (!clienteRepository.existsByIdScoped(clienteId)) {
            throw new EntityNotFoundException("Cliente não encontrado");
        }
        return repository.findByClienteIdAndEmpresaId(clienteId, tenantId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<VeiculoResponse> findByPlaca(String placa) {
        Long tenantId = requireTenant();
        String placaNormalizada = normalizePlate(placa);
        return repository.findByEmpresaIdAndPlaca(tenantId, placaNormalizada)
                .map(mapper::toResponse);
    }

    /**
     * Enriquecimento opcional para formulário. Não representa o cadastro canônico
     * e deliberadamente não devolve VIN/RENAVAM/chassi completos.
     */
    @Transactional(readOnly = true)
    public Optional<VeiculoResponse> lookupExternalByPlaca(String placa) {
        requireTenant();
        String placaNormalizada = normalizePlate(placa);

        return externalLookupService.lookup(placaNormalizada).map(external -> {
            Long marcaId = marcaRepository.findByNomeIgnoreCase(external.marca())
                    .map(MarcaVeiculo::getId)
                    .orElse(null);

            Long modeloId = null;
            if (marcaId != null && external.modelo() != null) {
                modeloId = modeloRepository.findByMarcaId(marcaId).stream()
                        .filter(modelo -> modelo.getNome().equalsIgnoreCase(external.modelo()))
                        .findFirst()
                        .map(ModeloVeiculo::getId)
                        .orElse(null);
            }

            return new VeiculoResponse(
                    null,
                    null,
                    null,
                    marcaId,
                    external.marca(),
                    modeloId,
                    external.modelo(),
                    null,
                    parseInteger(external.ano()),
                    parseInteger(external.anoModelo()),
                    null,
                    external.combustivel(),
                    placaNormalizada,
                    null,
                    null,
                    null,
                    external.cor(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    StatusVeiculo.ATIVO,
                    "Sugestão de dados externos; confirme antes de salvar");
        });
    }

    /**
     * Compatibilidade com o endpoint legado DELETE: a operação é lógica.
     * O registro, seus vínculos e histórico permanecem preservados.
     */
    public void delete(Long id) {
        deactivate(id);
    }

    public VeiculoResponse deactivate(Long id) {
        Long tenantId = requireTenant();
        Veiculo entity = repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
        entity.setStatus(StatusVeiculo.INATIVO);
        return mapper.toResponse(repository.save(entity));
    }

    public VeiculoResponse reactivate(Long id) {
        Long tenantId = requireTenant();
        Veiculo entity = repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
        entity.setStatus(StatusVeiculo.ATIVO);
        return mapper.toResponse(repository.save(entity));
    }

    private Cliente requireClienteDoTenant(Long clienteId) {
        if (clienteId == null) {
            throw new BusinessException("Cliente é obrigatório");
        }
        return clienteRepository.findByIdScoped(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    private MarcaVeiculo resolveMarca(Long id) {
        return id == null ? null : marcaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Marca não encontrada"));
    }

    private ModeloVeiculo resolveModelo(Long id) {
        return id == null ? null : modeloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modelo não encontrado"));
    }

    private AnoModelo resolveAnoModelo(Long id) {
        return id == null ? null : anoModeloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ano Modelo não encontrado"));
    }

    private TipoCombustivel resolveCombustivel(Long id) {
        return id == null ? null : tipoCombustivelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Combustível não encontrado"));
    }

    private void validateOdometerForCreate(VeiculoRequest request) {
        if (request.quilometragemAtual() != null && request.quilometragemAtual() < 0) {
            throw new BusinessException("Quilometragem atual não pode ser negativa");
        }
        if (request.quilometragemCadastro() != null && request.quilometragemCadastro() < 0) {
            throw new BusinessException("Quilometragem de cadastro não pode ser negativa");
        }
    }

    private void validateOdometerForUpdate(Veiculo current, VeiculoRequest request) {
        validateOdometerForCreate(request);
        Integer atualPersistida = current.getQuilometragemAtual();
        Integer nova = request.quilometragemAtual();
        if (atualPersistida != null && nova != null && nova < atualPersistida) {
            throw new BusinessException(
                    "Regressão de odômetro exige fluxo explícito com motivo e auditoria; a leitura atual não foi sobrescrita");
        }
    }

    private Long requireTenant() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("Contexto de empresa autenticada não disponível");
        }
        return tenantId;
    }

    static String normalizePlate(String placa) {
        if (placa == null) {
            throw new BusinessException("Placa é obrigatória");
        }
        String normalized = placa.replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase(Locale.ROOT)
                .trim();
        if (normalized.isBlank()) {
            throw new BusinessException("Placa é obrigatória");
        }
        return normalized;
    }

    private static String normalizeUpperIdentifier(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.replaceAll("\\s+", "")
                .toUpperCase(Locale.ROOT);
    }

    private static String normalizeDigits(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.replaceAll("\\D", "");
        return normalized.isBlank() ? null : normalized;
    }

    private static Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(value.replaceAll("\\D", ""));
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
