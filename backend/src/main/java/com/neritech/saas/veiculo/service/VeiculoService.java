package com.neritech.saas.veiculo.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.tenancy.TenantContext;
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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private final VeiculoMapper mapper;

    public VeiculoService(VeiculoRepository repository,
            ClienteRepository clienteRepository,
            MarcaVeiculoRepository marcaRepository,
            ModeloVeiculoRepository modeloRepository,
            AnoModeloRepository anoModeloRepository,
            TipoCombustivelRepository tipoCombustivelRepository,
            VeiculoMapper mapper) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
        this.anoModeloRepository = anoModeloRepository;
        this.tipoCombustivelRepository = tipoCombustivelRepository;
        this.mapper = mapper;
    }

    public VeiculoResponse create(VeiculoRequest request) {
        // Validações de existência
        Cliente cliente = clienteRepository.findById(request.clienteId())
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
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null && repository.findByEmpresaIdAndPlaca(tenantId, request.placa()).isPresent()) {
            throw new IllegalArgumentException("Já existe um veículo com esta placa para esta empresa");
        }

        Veiculo entity = mapper.toEntity(request);
        entity.setCliente(cliente);
        entity.setMarca(marca);
        entity.setModelo(modelo);
        entity.setAnoModelo(anoModelo);
        entity.setTipoCombustivel(tipoCombustivel);

        return mapper.toResponse(repository.save(entity));
    }

    public VeiculoResponse update(Long id, VeiculoRequest request) {
        Veiculo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));

        Cliente cliente = clienteRepository.findById(request.clienteId())
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
            Long tenantId = TenantContext.getCurrentTenant();
            if (tenantId != null && repository.findByEmpresaIdAndPlaca(tenantId, request.placa()).isPresent()) {
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
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponse> findByCliente(Long clienteId) {
        return repository.findByClienteId(clienteId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Veículo não encontrado");
        }
        repository.deleteById(id);
    }
}
