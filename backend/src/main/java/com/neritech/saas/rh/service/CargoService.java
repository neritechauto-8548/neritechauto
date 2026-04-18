package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.Cargo;
import com.neritech.saas.rh.dto.CargoRequest;
import com.neritech.saas.rh.dto.CargoResponse;
import com.neritech.saas.rh.mapper.CargoMapper;
import com.neritech.saas.rh.repository.CargoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CargoService {
    private final CargoRepository repository;
    private final CargoMapper mapper;

    public Page<CargoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
    }

    public CargoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Cargo nÃƒÂ£o encontrado"));
    }

    @Transactional
    public CargoResponse create(CargoRequest request) {
        Cargo entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public CargoResponse update(Long id, CargoRequest request) {
        Cargo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo nÃƒÂ£o encontrado"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Cargo nÃƒÂ£o encontrado");
        repository.deleteById(id);
    }
}
