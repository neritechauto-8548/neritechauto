package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.OrdemServicoRequest;
import com.neritech.saas.ordemservico.dto.OrdemServicoResponse;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class OrdemServicoMapper {

    @Autowired
    protected ClienteRepository clienteRepository;
    @Autowired
    protected VeiculoRepository veiculoRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    // Esses campos sao transientes no DTO e nao existem na entidade para serem populados
    @Mapping(target = "margemLucroRealizada", ignore = true)
    @Mapping(target = "custoTotalReal", ignore = true)
    @Mapping(target = "rentabilidadeOS", ignore = true)
    public abstract OrdemServico toEntity(OrdemServicoRequest request);

    @Mapping(source = "status.id", target = "statusId")
    @Mapping(target = "nomeCliente", expression = "java(getClienteNome(entity.getClienteId()))")
    @Mapping(target = "placaVeiculo", expression = "java(getVeiculoPlaca(entity.getVeiculoId()))")
    @Mapping(target = "nomeVeiculo", expression = "java(getVeiculoNome(entity.getVeiculoId()))")
    @Mapping(source = "status.nome", target = "statusNome")
    @Mapping(source = "status.corIdentificacao", target = "statusCor")
    public abstract OrdemServicoResponse toResponse(OrdemServico entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    // Ignore read-only/calculated fields during update
    @Mapping(target = "margemLucroRealizada", ignore = true)
    @Mapping(target = "custoTotalReal", ignore = true)
    @Mapping(target = "rentabilidadeOS", ignore = true)
    public abstract void updateEntityFromRequest(OrdemServicoRequest request, @MappingTarget OrdemServico entity);

    protected String getClienteNome(Long clienteId) {
        if (clienteId == null) return null;
        return clienteRepository.findById(clienteId)
                .map(c -> {
                    if (c.getTipoCliente() == TipoCliente.PESSOA_JURIDICA) {
                        String fantasia = c.getNomeFantasia();
                        return (fantasia != null && !fantasia.isBlank()) ? fantasia : c.getRazaoSocial();
                    }
                    return c.getNomeCompleto();
                })
                .orElse(null);
    }

    protected String getVeiculoPlaca(Long veiculoId) {
        if (veiculoId == null) return null;
        return veiculoRepository.findById(veiculoId)
                .map(v -> v.getPlaca())
                .orElse(null);
    }

    protected String getVeiculoNome(Long veiculoId) {
        if (veiculoId == null) return null;
        return veiculoRepository.findById(veiculoId)
                .map(v -> v.getModelo() != null ? v.getModelo().getNome() : null)
                .orElse(null);
    }
}
