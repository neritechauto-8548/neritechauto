package com.neritech.saas.veiculo.service;

import com.neritech.saas.veiculo.domain.TipoCombustivel;
import com.neritech.saas.veiculo.dto.TipoCombustivelRequest;
import com.neritech.saas.veiculo.dto.TipoCombustivelResponse;
import com.neritech.saas.veiculo.mapper.TipoCombustivelMapper;
import com.neritech.saas.veiculo.repository.TipoCombustivelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TipoCombustivelService {

    private final TipoCombustivelRepository repository;
    private final TipoCombustivelMapper mapper;

    public TipoCombustivelService(TipoCombustivelRepository repository, TipoCombustivelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public TipoCombustivelResponse create(TipoCombustivelRequest request) {
        TipoCombustivel entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    public TipoCombustivelResponse update(Long id, TipoCombustivelRequest request) {
        TipoCombustivel entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de CombustÃ­vel nÃ£o encontrado"));

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public TipoCombustivelResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de CombustÃ­vel nÃ£o encontrado"));
    }

    @Transactional(readOnly = true)
    public List<TipoCombustivelResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de CombustÃ­vel nÃ£o encontrado");
        }
        repository.deleteById(id);
    }
}
