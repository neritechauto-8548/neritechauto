package com.neritech.saas.fiscal.service;

import com.neritech.saas.fiscal.domain.CstProduto;
import com.neritech.saas.fiscal.dto.CstProdutoRequest;
import com.neritech.saas.fiscal.dto.CstProdutoResponse;
import com.neritech.saas.fiscal.mapper.CstProdutoMapper;
import com.neritech.saas.fiscal.repository.CstProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CstProdutoService {

    private final CstProdutoRepository repository;
    private final CstProdutoMapper mapper;

    @Transactional(readOnly = true)
    public List<CstProdutoResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CstProdutoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("CST nÃ£o encontrado com id: " + id));
    }

    @Transactional
    public CstProdutoResponse create(CstProdutoRequest request) {
        if (repository.findByCodigo(request.codigo()).isPresent()) {
            throw new IllegalArgumentException("JÃ¡ existe um CST com este cÃ³digo.");
        }
        CstProduto entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public CstProdutoResponse update(Long id, CstProdutoRequest request) {
        CstProduto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CST nÃ£o encontrado com id: " + id));

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CST nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
