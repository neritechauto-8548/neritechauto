package com.neritech.saas.ordemservico.service;

import com.neritech.saas.ordemservico.domain.FotoOS;
import com.neritech.saas.ordemservico.domain.ItemOSProduto;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.*;
import com.neritech.saas.ordemservico.mapper.*;
import com.neritech.saas.ordemservico.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository repository;
    private final StatusOSRepository statusOSRepository;
    private final ItemOSServicoRepository itemOSServicoRepository;
    private final ItemOSProdutoRepository itemOSProdutoRepository;
    private final FotoOSRepository fotoOSRepository;
    private final OrdemServicoMapper mapper;
    private final ItemOSServicoMapper servicoMapper;
    private final ItemOSProdutoMapper produtoMapper;

    public OrdemServicoResponse create(OrdemServicoRequest request) {
        if (repository.existsByEmpresaIdAndNumeroOS(request.empresaId(), request.numeroOS())) {
            throw new IllegalArgumentException("Número de OS já existe para esta empresa");
        }

        OrdemServico entity = mapper.toEntity(request);

        if (request.statusId() != null) {
            entity.setStatus(statusOSRepository.findById(request.statusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status não encontrado")));
        }

        OrdemServico saved = repository.save(entity);
        return enrichResponse(mapper.toResponse(saved));
    }

    @Transactional(readOnly = true)
    public OrdemServicoResponse findById(Long id) {
        OrdemServico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada"));
        return enrichResponse(mapper.toResponse(entity));
    }

    @Transactional(readOnly = true)
    public Page<OrdemServicoResponse> findByEmpresaId(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse); // No enriquecimento aqui para performance em listagem
    }

    @Transactional(readOnly = true)
    public Page<OrdemServicoResponse> findByEmpresaIdAndStatusId(Long empresaId, Long statusId, Pageable pageable) {
        return repository.findByEmpresaIdAndStatusId(empresaId, statusId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<OrdemServicoResponse> findByClienteId(Long clienteId, Pageable pageable) {
        return repository.findByClienteId(clienteId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public java.util.Optional<OrdemServicoResponse> findByCpfAndNumeroOS(String cpf, String numeroOS) {
        return repository.findByCpfAndNumeroOS(cpf, numeroOS)
                .map(entity -> enrichResponse(mapper.toResponse(entity)));
    }

    public OrdemServicoResponse update(Long id, OrdemServicoRequest request) {
        OrdemServico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada"));

        mapper.updateEntityFromRequest(request, entity);

        if (request.statusId() != null) {
            entity.setStatus(statusOSRepository.findById(request.statusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status não encontrado")));
        }

        OrdemServico updated = repository.save(entity);
        return enrichResponse(mapper.toResponse(updated));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Ordem de Serviço não encontrada");
        }
        repository.deleteById(id);
    }

    /**
     * Popula as coleções aninhadas (serviços, peças, fotos) no DTO de resposta.
     */
    private OrdemServicoResponse enrichResponse(OrdemServicoResponse response) {
        Long osId = response.id();

        // Buscar Serviços
        List<ItemOSServicoResponse> servicos = itemOSServicoRepository.findByOrdemServicoId(osId)
                .stream()
                .map(servicoMapper::toResponse)
                .collect(Collectors.toList());

        // Buscar Produtos (Peças)
        List<ItemOSProdutoResponse> produtos = itemOSProdutoRepository.findByOrdemServicoId(osId)
                .stream()
                .map(produtoMapper::toResponse)
                .collect(Collectors.toList());

        // Buscar Fotos
        List<FotoOSResponse> fotos = fotoOSRepository.findByOrdemServicoIdOrderByIdAsc(osId)
                .stream()
                .map(this::mapFotoToResponse)
                .collect(Collectors.toList());

        // Retornar nova instância do record com as listas populadas
        return new OrdemServicoResponse(
                response.id(), response.empresaId(), response.numeroOS(), response.clienteId(),
                response.veiculoId(), response.statusId(), response.tipoOS(), response.prioridade(),
                response.dataAbertura(), response.dataPromessa(), response.dataInicioExecucao(),
                response.dataFimExecucao(), response.dataEntrega(), response.quilometragemEntrada(),
                response.quilometragemSaida(), response.nivelCombustivelEntrada(), response.nivelCombustivelSaida(),
                response.consultorResponsavelId(), response.mecanicoResponsavelId(), response.equipeExecucao(),
                response.problemaRelatado(), response.solucaoAplicada(), response.observacoesInternas(),
                response.observacoesCliente(), response.valorServicos(), response.valorProdutos(),
                response.valorDesconto(), response.valorAcrescimo(), response.valorTotal(),
                response.formaPagamentoId(), response.condicaoPagamentoId(), response.valorEntrada(),
                response.valorFinanciado(), response.aprovadoCliente(), response.dataAprovacaoCliente(),
                response.metodoAprovacao(), response.garantiaDias(), response.dataVencimentoGarantia(),
                response.nfeEmitida(), response.numeroNFe(), response.notaAvaliacaoCliente(),
                response.tempoTotalExecucaoMinutos(), response.margemLucroRealizada(), response.dataCadastro(),
                response.dataAtualizacao(), response.versao(), response.nomeCliente(),
                response.placaVeiculo(), response.nomeVeiculo(), response.statusNome(), response.statusCor(),
                servicos, produtos, fotos
        );
    }

    private FotoOSResponse mapFotoToResponse(FotoOS entity) {
        return new FotoOSResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getOrdemServicoId(),
                entity.getArquivoUrl(),
                entity.getContentType(),
                entity.getTamanho(),
                entity.getDescricao(),
                entity.getDataCadastro(),
                entity.getDataAtualizacao()
        );
    }
}
