package com.neritech.saas.produtoServico.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neritech.saas.produtoServico.domain.Promocao;
import com.neritech.saas.produtoServico.dto.PromocaoRequest;
import com.neritech.saas.produtoServico.dto.PromocaoResponse;
import com.neritech.saas.produtoServico.mapper.PromocaoMapper;
import com.neritech.saas.produtoServico.repository.PromocaoRepository;

@Service
public class PromocaoService {

    private final PromocaoRepository repository;
    private final PromocaoMapper mapper;

    public PromocaoService(PromocaoRepository repository, PromocaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public PromocaoResponse create(PromocaoRequest request) {
        if (request.codigoCupom() != null
                && repository.existsByEmpresaIdAndCodigoCupom(request.empresaId(), request.codigoCupom())) {
            throw new IllegalArgumentException("JÃ¡ existe uma promoÃ§Ã£o com este cÃ³digo de cupom nesta empresa");
        }

        Promocao entity = mapper.toEntity(request);
        entity.setEmpresaId(request.empresaId());

        Promocao saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PromocaoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("PromoÃ§Ã£o nÃ£o encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<PromocaoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public PromocaoResponse update(Long id, PromocaoRequest request) {
        Promocao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PromoÃ§Ã£o nÃ£o encontrada com ID: " + id));

        if (!entity.getEmpresaId().equals(request.empresaId())) {
            throw new IllegalArgumentException("NÃ£o Ã© permitido alterar a empresa da promoÃ§Ã£o");
        }

        if (request.codigoCupom() != null && !request.codigoCupom().equals(entity.getCodigoCupom()) &&
                repository.existsByEmpresaIdAndCodigoCupom(request.empresaId(), request.codigoCupom())) {
            throw new IllegalArgumentException("JÃ¡ existe uma promoÃ§Ã£o com este cÃ³digo de cupom nesta empresa");
        }

        mapper.updateEntityFromRequest(request, entity);
        Promocao saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("PromoÃ§Ã£o nÃ£o encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
