package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.dto.ParcelaPagamentoResponse;
import com.neritech.saas.financeiro.mapper.ParcelaPagamentoMapper;
import com.neritech.saas.financeiro.repository.ParcelaPagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcelaPagamentoService {

    private final ParcelaPagamentoRepository repository;
    private final ParcelaPagamentoMapper mapper;

    @Transactional(readOnly = true)
    public List<ParcelaPagamentoResponse> findByPagamentoId(Long pagamentoId) {
        return repository.findByPagamentoId(pagamentoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParcelaPagamentoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Parcela de pagamento nÃ£o encontrada"));
    }
}
