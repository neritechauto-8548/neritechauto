package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.Fatura;
import com.neritech.saas.financeiro.domain.ItemFatura;
import com.neritech.saas.financeiro.dto.FaturaRequest;
import com.neritech.saas.financeiro.dto.FaturaResponse;
import com.neritech.saas.financeiro.mapper.FaturaMapper;
import com.neritech.saas.financeiro.mapper.ItemFaturaMapper;
import com.neritech.saas.financeiro.repository.FaturaRepository;
import com.neritech.saas.financeiro.repository.ItemFaturaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaturaService {

    private final FaturaRepository repository;
    private final ItemFaturaRepository itemRepository;
    private final FaturaMapper mapper;
    private final ItemFaturaMapper itemMapper;

    @Transactional(readOnly = true)
    public Page<FaturaResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(this::toResponseWithItems);
    }

    @Transactional(readOnly = true)
    public FaturaResponse findById(Long id, Long empresaId) {
        Fatura fatura = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Fatura n\u00E3o encontrada"));
        return toResponseWithItems(fatura);
    }

    @Transactional(readOnly = true)
    public FaturaResponse findByOrdemServico(Long osId, Long empresaId) {
        Fatura fatura = repository.findByOrdemServicoIdAndEmpresaId(osId, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Fatura n\u00E3o encontrada para a OS"));
        return toResponseWithItems(fatura);
    }

    @Transactional
    public FaturaResponse create(Long empresaId, FaturaRequest request) {
        Fatura entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context

        Fatura savedFatura = repository.save(entity);

        if (request.itens() != null && !request.itens().isEmpty()) {
            List<ItemFatura> itens = request.itens().stream()
                    .map(itemRequest -> {
                        ItemFatura item = itemMapper.toEntity(itemRequest);
                        item.setFatura(savedFatura);
                        return item;
                    })
                    .collect(Collectors.toList());
            itemRepository.saveAll(itens);
        }

        return toResponseWithItems(savedFatura);
    }

    @Transactional
    public FaturaResponse update(Long id, Long empresaId, FaturaRequest request) {
        Fatura entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Fatura nÃ£o encontrada"));

        mapper.updateEntityFromDTO(request, entity);
        Fatura savedFatura = repository.save(entity);

        // Update items logic (simplified: delete all and recreate, or update existing)
        // For simplicity, we'll delete existing items and recreate them if provided
        // In a real scenario, we might want to update existing items to preserve IDs
        if (request.itens() != null) {
            List<ItemFatura> existingItems = itemRepository.findByFaturaId(id);
            itemRepository.deleteAll(existingItems);

            List<ItemFatura> newItems = request.itens().stream()
                    .map(itemRequest -> {
                        ItemFatura item = itemMapper.toEntity(itemRequest);
                        item.setFatura(savedFatura);
                        return item;
                    })
                    .collect(Collectors.toList());
            itemRepository.saveAll(newItems);
        }

        return toResponseWithItems(savedFatura);
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        Fatura entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Fatura nÃ£o encontrada"));

        List<ItemFatura> items = itemRepository.findByFaturaId(id);
        itemRepository.deleteAll(items);

        repository.delete(entity);
    }

    private FaturaResponse toResponseWithItems(Fatura fatura) {
        FaturaResponse response = mapper.toResponse(fatura);
        // MapStruct might not map items if they are not in the entity list (which they
        // aren't)
        // So we fetch them manually and set them in the response
        // But FaturaResponse is a Record, so we can't set fields.
        // We need to reconstruct the response or use a builder.
        // Or we can modify the mapper to use a custom method that fetches items.
        // Since I'm using records, I have to create a new instance.

        List<ItemFatura> items = itemRepository.findByFaturaId(fatura.getId());

        return new FaturaResponse(
                response.id(),
                response.empresaId(),
                response.numeroFatura(),
                response.clienteId(),
                response.ordemServicoId(),
                response.tipoFatura(),
                response.dataEmissao(),
                response.dataVencimento(),
                response.valorServicos(),
                response.valorProdutos(),
                response.valorDescontos(),
                response.valorAcrescimos(),
                response.valorTotal(),
                response.valorPago(),
                response.valorPendente(),
                response.status(),
                response.formaPagamentoId(),
                response.formaPagamentoNome(),
                response.condicaoPagamentoId(),
                response.condicaoPagamentoNome(),
                response.observacoes(),
                response.observacoesInternas(),
                response.nfeNumero(),
                response.nfeChave(),
                response.nfeUrlDanfe(),
                response.dataEnvioCliente(),
                response.enviadaPorEmail(),
                response.enviadaPorWhatsapp(),
                response.emailEnvio(),
                response.whatsappEnvio(),
                response.boletoNossoNumero(),
                response.boletoUrl(),
                response.pixQrCode(),
                response.pixCodigo(),
                items.stream().map(itemMapper::toResponse).collect(Collectors.toList()),
                response.createdAt(),
                response.updatedAt());
    }
}
