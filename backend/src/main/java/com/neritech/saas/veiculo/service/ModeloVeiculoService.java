package com.neritech.saas.veiculo.service;

import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.repository.MarcaVeiculoRepository;
import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import com.neritech.saas.veiculo.dto.ModeloVeiculoRequest;
import com.neritech.saas.veiculo.dto.ModeloVeiculoResponse;
import com.neritech.saas.veiculo.mapper.ModeloVeiculoMapper;
import com.neritech.saas.veiculo.repository.ModeloVeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ModeloVeiculoService {

    private final ModeloVeiculoRepository repository;
    private final MarcaVeiculoRepository marcaRepository;
    private final ModeloVeiculoMapper mapper;

    public ModeloVeiculoService(ModeloVeiculoRepository repository, MarcaVeiculoRepository marcaRepository,
            ModeloVeiculoMapper mapper) {
        this.repository = repository;
        this.marcaRepository = marcaRepository;
        this.mapper = mapper;
    }

    public ModeloVeiculoResponse create(ModeloVeiculoRequest request) {
        MarcaVeiculo marca = marcaRepository.findById(request.marcaId())
                .orElseThrow(() -> new EntityNotFoundException("Marca nÃ£o encontrada"));

        ModeloVeiculo entity = mapper.toEntity(request);
        entity.setMarca(marca);

        return mapper.toResponse(repository.save(entity));
    }

    public ModeloVeiculoResponse update(Long id, ModeloVeiculoRequest request) {
        ModeloVeiculo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modelo nÃ£o encontrado"));

        MarcaVeiculo marca = marcaRepository.findById(request.marcaId())
                .orElseThrow(() -> new EntityNotFoundException("Marca nÃ£o encontrada"));

        mapper.updateEntityFromRequest(request, entity);
        entity.setMarca(marca);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public ModeloVeiculoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Modelo nÃ£o encontrado"));
    }

    @Transactional(readOnly = true)
    public List<ModeloVeiculoResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ModeloVeiculoResponse> findByMarca(Long marcaId) {
        return repository.findByMarcaId(marcaId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Modelo nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
