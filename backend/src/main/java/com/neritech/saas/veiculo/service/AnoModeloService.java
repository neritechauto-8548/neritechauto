package com.neritech.saas.veiculo.service;

import com.neritech.saas.veiculo.domain.AnoModelo;
import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import com.neritech.saas.veiculo.dto.AnoModeloRequest;
import com.neritech.saas.veiculo.dto.AnoModeloResponse;
import com.neritech.saas.veiculo.mapper.AnoModeloMapper;
import com.neritech.saas.veiculo.repository.AnoModeloRepository;
import com.neritech.saas.veiculo.repository.ModeloVeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnoModeloService {

    private final AnoModeloRepository repository;
    private final ModeloVeiculoRepository modeloRepository;
    private final AnoModeloMapper mapper;

    public AnoModeloService(AnoModeloRepository repository, ModeloVeiculoRepository modeloRepository,
            AnoModeloMapper mapper) {
        this.repository = repository;
        this.modeloRepository = modeloRepository;
        this.mapper = mapper;
    }

    public AnoModeloResponse create(AnoModeloRequest request) {
        ModeloVeiculo modelo = modeloRepository.findById(request.modeloId())
                .orElseThrow(() -> new EntityNotFoundException("Modelo nÃƒÂ£o encontrado"));

        AnoModelo entity = mapper.toEntity(request);
        entity.setModelo(modelo);

        return mapper.toResponse(repository.save(entity));
    }

    public AnoModeloResponse update(Long id, AnoModeloRequest request) {
        AnoModelo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ano Modelo nÃƒÂ£o encontrado"));

        ModeloVeiculo modelo = modeloRepository.findById(request.modeloId())
                .orElseThrow(() -> new EntityNotFoundException("Modelo nÃƒÂ£o encontrado"));

        mapper.updateEntityFromRequest(request, entity);
        entity.setModelo(modelo);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public AnoModeloResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Ano Modelo nÃƒÂ£o encontrado"));
    }

    @Transactional(readOnly = true)
    public List<AnoModeloResponse> findByModelo(Long modeloId) {
        return repository.findByModeloId(modeloId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Ano Modelo nÃƒÂ£o encontrado");
        }
        repository.deleteById(id);
    }
}

