package com.neritech.saas.veiculo.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.repository.MarcaVeiculoRepository;
import com.neritech.saas.veiculo.domain.AnoModelo;
import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import com.neritech.saas.veiculo.repository.AnoModeloRepository;
import com.neritech.saas.veiculo.repository.ModeloVeiculoRepository;
import com.neritech.saas.veiculo.domain.TipoCombustivel;
import com.neritech.saas.veiculo.repository.TipoCombustivelRepository;
import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.veiculo.dto.VeiculoRequest;
import com.neritech.saas.veiculo.dto.VeiculoResponse;
import com.neritech.saas.veiculo.mapper.VeiculoMapper;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import com.neritech.saas.veiculo.domain.enums.StatusVeiculo;
import com.neritech.saas.util.DocumentoValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public VeiculoService(VeiculoRepository repository,
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
        Long tenantId = TenantAccess.requireCurrent();
        // O cliente vinculado tem de pertencer à empresa da sessão.
        Cliente cliente = clienteRepository.findByIdScoped(request.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        MarcaVeiculo marca = null;
        if (request.marcaId() != null) {
            marca = marcaRepository.findById(request.marcaId())
                    .orElseThrow(() -> new EntityNotFoundException("Marca não encontrada"));
        }

        ModeloVeiculo modelo = null;
        if (request.modeloId() != null) {
            modelo = modeloRepository.findById(request.modeloId())
                    .orElseThrow(() -> new EntityNotFoundException("Modelo não encontrado"));
        }
        if (modelo != null && (marca == null || !modelo.getMarca().getId().equals(marca.getId()))) {
            throw new IllegalArgumentException("O modelo não pertence à marca informada");
        }

        AnoModelo anoModelo = null;
        if (request.anoModeloId() != null) {
            anoModelo = anoModeloRepository.findById(request.anoModeloId())
                    .orElseThrow(() -> new EntityNotFoundException("Ano Modelo não encontrado"));
        }

        TipoCombustivel tipoCombustivel = null;
        if (request.combustivelId() != null) {
            tipoCombustivel = tipoCombustivelRepository.findById(request.combustivelId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de Combustível não encontrado"));
        }

        // Validação de unicidade da placa no tenant
        if (repository.findByEmpresaIdAndPlaca(tenantId, request.placa()).isPresent()) {
            throw new IllegalArgumentException("Já existe um veículo com esta placa para esta empresa");
        }

        Veiculo entity = mapper.toEntity(request);
        entity.setEmpresaId(tenantId);
        entity.setCliente(cliente);
        entity.setMarca(marca);
        entity.setModelo(modelo);
        entity.setAnoModelo(anoModelo);
        entity.setTipoCombustivel(tipoCombustivel);

        return mapper.toResponse(repository.save(entity));
    }

    public VeiculoResponse update(Long id, VeiculoRequest request) {
        Long tenantId = TenantAccess.requireCurrent();
        Veiculo entity = repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));

        Cliente cliente = clienteRepository.findByIdScoped(request.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        MarcaVeiculo marca = null;
        if (request.marcaId() != null) {
            marca = marcaRepository.findById(request.marcaId())
                    .orElseThrow(() -> new EntityNotFoundException("Marca não encontrada"));
        }

        ModeloVeiculo modelo = null;
        if (request.modeloId() != null) {
            modelo = modeloRepository.findById(request.modeloId())
                    .orElseThrow(() -> new EntityNotFoundException("Modelo não encontrado"));
        }
        if (modelo != null && (marca == null || !modelo.getMarca().getId().equals(marca.getId()))) {
            throw new IllegalArgumentException("O modelo não pertence à marca informada");
        }

        AnoModelo anoModelo = null;
        if (request.anoModeloId() != null) {
            anoModelo = anoModeloRepository.findById(request.anoModeloId())
                    .orElseThrow(() -> new EntityNotFoundException("Ano Modelo não encontrado"));
        }

        TipoCombustivel tipoCombustivel = null;
        if (request.combustivelId() != null) {
            tipoCombustivel = tipoCombustivelRepository.findById(request.combustivelId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de Combustível não encontrado"));
        }

        // Validação de placa se mudou
        if (!entity.getPlaca().equals(request.placa())) {
            if (repository.findByEmpresaIdAndPlaca(tenantId, request.placa()).isPresent()) {
                throw new IllegalArgumentException("Já existe um veículo com esta placa para esta empresa");
            }
        }

        mapper.updateEntityFromRequest(request, entity);
        entity.setCliente(cliente);
        entity.setMarca(marca);
        entity.setModelo(modelo);
        entity.setAnoModelo(anoModelo);
        entity.setTipoCombustivel(tipoCombustivel);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public VeiculoResponse findById(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantAccess.requireCurrent())
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponse> findAll() {
        Long tenantId = TenantAccess.requireCurrent();
        return repository.findByEmpresaId(tenantId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponse> findByCliente(Long clienteId) {
        Long tenantId = TenantAccess.requireCurrent();
        return repository.findByClienteIdAndEmpresaId(clienteId, tenantId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    /** Consulta apenas registros da empresa autenticada, sem serviços externos. */
    @Transactional(readOnly = true)
    public Optional<VeiculoResponse> findInternoByPlaca(String placa) {
        Long tenantId = TenantAccess.requireCurrent();
        if (placa == null || placa.isBlank()) return Optional.empty();
        String normalizada = placa.trim().toUpperCase(java.util.Locale.ROOT);
        return repository.findByEmpresaIdAndPlacaIgnoreCase(tenantId, normalizada).map(mapper::toResponse);
    }

    public Optional<VeiculoResponse> findByPlaca(String placa) {
        TenantAccess.requireCurrent();
        if (placa == null || placa.isBlank()) return Optional.empty();
        
        // Conforme pedido pelo usuário: Busca diretamente na API externa, ignorando o histórico interno do banco
        return externalLookupService.lookup(placa).map(external -> {
             // Tenta mapear Marca e Modelo para nossos IDs internos
             Long marcaId = marcaRepository.findByNomeIgnoreCase(external.marca()).map(m -> m.getId()).orElse(null);
             Long modeloId = null;
             if (marcaId != null) {
                 // Busca o modelo ignorando case (algumas APIs retornam tudo em maiúsculo)
                 // Como o repositório não tem IgnoreCase para modelo ainda, fazemos um filtro simples ou usamos o nome bruto
                 modeloId = modeloRepository.findByMarcaId(marcaId).stream()
                         .filter(m -> m.getNome().equalsIgnoreCase(external.modelo()))
                         .findFirst()
                         .map(m -> m.getId())
                         .orElse(null);
             }
             
             // Retorna um response com os dados da API para preencher o formulário
             return new VeiculoResponse(
                 null, // id
                 null, // clienteId
                 null, // clienteNome
                 marcaId,
                 external.marca(),
                 modeloId,
                 external.modelo(),
                 null, // modeloImagemUrl (consulta externa sem imagem cadastrada)
                 null, // anoModeloId
                 external.ano() != null && !external.ano().isBlank() ? Integer.parseInt(external.ano()) : null,
                 external.anoModelo() != null && !external.anoModelo().isBlank() ? Integer.parseInt(external.anoModelo()) : null,
                 null, // combustivelId
                 external.combustivel(),
                 external.placa(),
                 external.renavam(),
                 external.chassi(),
                 external.motor(),
                 external.cor(),
                 null, // direcao
                 null, // cambio
                 null, // arCondicionado
                 null, // portas
                 null, // quilometragemAtual
                 null, // quilometragemCadastro
                 null, // dataUltimaRevisao
                 null, // proximaRevisaoKm
                 null, // proximaRevisaoData
                 StatusVeiculo.ATIVO,
                 "Dados carregados via API externa"
             );
        });
    }

    public void delete(Long id) {
        Veiculo entity = repository.findByIdAndEmpresaId(id, TenantAccess.requireCurrent())
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
        repository.delete(entity);
    }
}
