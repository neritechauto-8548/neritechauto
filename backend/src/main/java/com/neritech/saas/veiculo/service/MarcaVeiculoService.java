package com.neritech.saas.veiculo.service;

import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.dto.MarcaVeiculoRequest;
import com.neritech.saas.veiculo.mapper.MarcaVeiculoMapper;
import com.neritech.saas.veiculo.repository.MarcaVeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MarcaVeiculoService {

    private final MarcaVeiculoRepository repository;

    public MarcaVeiculoService(MarcaVeiculoRepository repository) {
        this.repository = repository;
    }

    public MarcaVeiculo criar(MarcaVeiculoRequest request) {
        if (repository.existsByNomeIgnoreCase(request.getNome())) {
            throw new IllegalArgumentException("Já existe uma marca com este nome");
        }
        MarcaVeiculo marcaVeiculo = MarcaVeiculoMapper.toEntity(request);
        return repository.save(marcaVeiculo);
    }

    @Transactional(readOnly = true)
    public MarcaVeiculo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Marca de veículo não encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<MarcaVeiculo> listar(String nome, Pageable pageable) {
        return repository.findAll(pageable);
    }

    public MarcaVeiculo atualizar(Long id, MarcaVeiculoRequest request) {
        MarcaVeiculo marcaVeiculo = buscarPorId(id);
        
        if (!marcaVeiculo.getNome().equalsIgnoreCase(request.getNome()) && 
            repository.existsByNomeIgnoreCase(request.getNome())) {
            throw new IllegalArgumentException("Já existe uma marca com este nome");
        }

        MarcaVeiculoMapper.updateEntityFromRequest(request, marcaVeiculo);
        return repository.save(marcaVeiculo);
    }

    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Marca de veículo não encontrada");
        }
        repository.deleteById(id);
    }
}
