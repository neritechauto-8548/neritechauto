package com.neritech.saas.rh.service;

import com.neritech.saas.empresa.repository.SetorRepository;
import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.domain.RegraComissao;
import com.neritech.saas.rh.dto.RegraComissaoRequest;
import com.neritech.saas.rh.dto.RegraComissaoResponse;
import com.neritech.saas.rh.mapper.RegraComissaoMapper;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import com.neritech.saas.rh.repository.RegraComissaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.neritech.saas.common.exception.BusinessException;
import java.util.List;

@Service
public class RegraComissaoService {

    private final RegraComissaoRepository repository;
    private final FuncionarioRepository funcionarioRepository;
    private final SetorRepository setorRepository;
    private final RegraComissaoMapper mapper;

    public RegraComissaoService(RegraComissaoRepository repository,
                                FuncionarioRepository funcionarioRepository,
                                SetorRepository setorRepository,
                                RegraComissaoMapper mapper) {
        this.repository = repository;
        this.funcionarioRepository = funcionarioRepository;
        this.setorRepository = setorRepository;
        this.mapper = mapper;
    }

    public Page<RegraComissaoResponse> listarPorFuncionario(Long funcionarioId, Pageable pageable) {
        return repository.findByFuncionarioId(funcionarioId, pageable)
                .map(mapper::toResponse);
    }

    private void validarRegraDeNegocio(Long id, Long funcionarioId, Long setorId, java.time.LocalDateTime dataInicio, java.time.LocalDateTime dataFinal, String sobreServico, String sobreProdutos, String faturamentoGeral) {
        boolean temServico = "SIM".equals(sobreServico);
        boolean temProdutos = "SIM".equals(sobreProdutos);
        boolean temFaturamento = faturamentoGeral != null && faturamentoGeral.startsWith("SIM");

        if (!temServico && !temProdutos && !temFaturamento) {
            throw new BusinessException("A regra deve comissionar sobre serviços, produtos ou faturamento geral.");
        }

        if (dataInicio == null) {
            throw new BusinessException("A data de início da regra é obrigatória.");
        }

        if (dataFinal != null && dataFinal.isBefore(dataInicio)) {
            throw new BusinessException("A data final não pode ser anterior à data de início.");
        }
    }

    private void processarAutoFechamento(Long currentId, Long funcionarioId, Long setorId, java.time.LocalDateTime dataInicio) {
        List<RegraComissao> regrasExistentes = repository.findByFuncionarioId(funcionarioId);
        for (RegraComissao regra : regrasExistentes) {
            if (currentId != null && regra.getId().equals(currentId)) {
                continue;
            }

            if (Boolean.TRUE.equals(regra.getAtivo())) {
                boolean mesmoSetor = (setorId == null && regra.getSetor() == null) 
                        || (setorId != null && regra.getSetor() != null && regra.getSetor().getId().equals(setorId));

                if (mesmoSetor) {
                    java.time.LocalDateTime rInicio = regra.getDataInicio();
                    java.time.LocalDateTime rFinal = regra.getDataFinal();

                    if (rInicio.isBefore(dataInicio)) {
                        if (rFinal == null || rFinal.isAfter(dataInicio)) {
                            regra.setDataFinal(dataInicio);
                            regra.setAtivo(false);
                            repository.save(regra);
                        }
                    } else {
                        // Se a regra anterior começa na mesma data ou depois, desativa
                        regra.setAtivo(false);
                        repository.save(regra);
                    }
                }
            }
        }
    }

    @Transactional
    public RegraComissaoResponse criar(RegraComissaoRequest request) {
        Funcionario funcionario = funcionarioRepository.findById(request.getFuncionarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionario não encontrado"));

        processarAutoFechamento(null, request.getFuncionarioId(), request.getSetorId(), request.getDataInicio());
        validarRegraDeNegocio(null, request.getFuncionarioId(), request.getSetorId(), request.getDataInicio(), request.getDataFinal(), request.getSobreServico(), request.getSobreProdutos(), request.getFaturamentoGeral());

        RegraComissao entity = mapper.toEntity(request);
        entity.setFuncionario(funcionario);
        entity.setEmpresaId(funcionario.getEmpresaId());

        if (request.getSetorId() != null) {
            entity.setSetor(setorRepository.findById(request.getSetorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Setor não encontrado")));
        }

        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public RegraComissaoResponse atualizar(Long id, RegraComissaoRequest request) {
        RegraComissao entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Regra não encontrada"));

        processarAutoFechamento(id, entity.getFuncionario().getId(), request.getSetorId(), request.getDataInicio());
        validarRegraDeNegocio(id, entity.getFuncionario().getId(), request.getSetorId(), request.getDataInicio(), request.getDataFinal(), request.getSobreServico(), request.getSobreProdutos(), request.getFaturamentoGeral());

        mapper.updateEntity(request, entity);

        if (request.getSetorId() != null) {
            entity.setSetor(setorRepository.findById(request.getSetorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Setor não encontrado")));
        } else {
            entity.setSetor(null);
        }

        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Regra não encontrada");
        }
        repository.deleteById(id);
    }
}
