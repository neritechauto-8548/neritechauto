package com.neritech.saas.fiscal.service;

import com.neritech.saas.fiscal.domain.NcmProduto;
import com.neritech.saas.fiscal.dto.NcmProdutoRequest;
import com.neritech.saas.fiscal.dto.NcmProdutoResponse;
import com.neritech.saas.fiscal.mapper.NcmProdutoMapper;
import com.neritech.saas.fiscal.repository.NcmProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NcmProdutoService {

    private final NcmProdutoRepository repository;
    private final NcmProdutoMapper mapper;

    @Transactional(readOnly = true)
    public List<NcmProdutoResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NcmProdutoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("NCM nÃ£o encontrado com id: " + id));
    }

    @Transactional
    public NcmProdutoResponse create(NcmProdutoRequest request) {
        if (repository.findByCodigo(request.codigo()).isPresent()) {
            throw new IllegalArgumentException("JÃ¡ existe um NCM com este cÃ³digo.");
        }
        NcmProduto entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public NcmProdutoResponse update(Long id, NcmProdutoRequest request) {
        NcmProduto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NCM nÃ£o encontrado com id: " + id));

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("NCM nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
