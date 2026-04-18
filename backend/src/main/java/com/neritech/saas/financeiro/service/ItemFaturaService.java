package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.dto.ItemFaturaResponse;
import com.neritech.saas.financeiro.mapper.ItemFaturaMapper;
import com.neritech.saas.financeiro.repository.ItemFaturaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemFaturaService {

    private final ItemFaturaRepository repository;
    private final ItemFaturaMapper mapper;

    @Transactional(readOnly = true)
    public List<ItemFaturaResponse> findByFaturaId(Long faturaId) {
        return repository.findByFaturaId(faturaId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemFaturaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Item da fatura nÃ£o encontrado"));
    }

    // Create/Update/Delete are handled via FaturaService usually, but we can expose
    // them if needed.
    // For now, I'll leave them out unless specifically requested, as items are
    // usually managed within the invoice context.
}
