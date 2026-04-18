package com.neritech.saas.veiculo.mapper;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.veiculo.domain.AnoModelo;
import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import com.neritech.saas.veiculo.domain.TipoCombustivel;
import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.veiculo.domain.enums.StatusVeiculo;
import com.neritech.saas.veiculo.dto.VeiculoRequest;
import com.neritech.saas.veiculo.dto.VeiculoResponse;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:52-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class VeiculoMapperImpl implements VeiculoMapper {

    @Override
    public Veiculo toEntity(VeiculoRequest request) {
        if ( request == null ) {
            return null;
        }

        Veiculo veiculo = new Veiculo();

        veiculo.setCliente( veiculoRequestToCliente( request ) );
        veiculo.setMarca( veiculoRequestToMarcaVeiculo( request ) );
        veiculo.setModelo( veiculoRequestToModeloVeiculo( request ) );
        veiculo.setAnoModelo( veiculoRequestToAnoModelo( request ) );
        veiculo.setPlaca( request.placa() );
        veiculo.setRenavam( request.renavam() );
        veiculo.setChassi( request.chassi() );
        veiculo.setNumeroMotor( request.numeroMotor() );
        veiculo.setCorExterna( request.corExterna() );
        veiculo.setQuilometragemAtual( request.quilometragemAtual() );
        veiculo.setQuilometragemCadastro( request.quilometragemCadastro() );
        veiculo.setDataUltimaRevisao( request.dataUltimaRevisao() );
        veiculo.setProximaRevisaoKm( request.proximaRevisaoKm() );
        veiculo.setProximaRevisaoData( request.proximaRevisaoData() );
        veiculo.setStatus( request.status() );
        veiculo.setObservacoes( request.observacoes() );

        return veiculo;
    }

    @Override
    public VeiculoResponse toResponse(Veiculo entity) {
        if ( entity == null ) {
            return null;
        }

        Long clienteId = null;
        String clienteNome = null;
        Long marcaId = null;
        String marcaNome = null;
        Long modeloId = null;
        String modeloNome = null;
        Long anoModeloId = null;
        Integer anoFabricacao = null;
        Integer anoModelo = null;
        Long combustivelId = null;
        String combustivelNome = null;
        Long id = null;
        String placa = null;
        String renavam = null;
        String chassi = null;
        String numeroMotor = null;
        String corExterna = null;
        Integer quilometragemAtual = null;
        Integer quilometragemCadastro = null;
        LocalDate dataUltimaRevisao = null;
        Integer proximaRevisaoKm = null;
        LocalDate proximaRevisaoData = null;
        StatusVeiculo status = null;
        String observacoes = null;

        clienteId = entityClienteId( entity );
        clienteNome = entityClienteNomeCompleto( entity );
        marcaId = entityMarcaId( entity );
        marcaNome = entityMarcaNome( entity );
        modeloId = entityModeloId( entity );
        modeloNome = entityModeloNome( entity );
        anoModeloId = entityAnoModeloId( entity );
        anoFabricacao = entityAnoModeloAnoFabricacao( entity );
        anoModelo = entityAnoModeloAnoModelo( entity );
        combustivelId = entityTipoCombustivelId( entity );
        combustivelNome = entityTipoCombustivelNome( entity );
        id = entity.getId();
        placa = entity.getPlaca();
        renavam = entity.getRenavam();
        chassi = entity.getChassi();
        numeroMotor = entity.getNumeroMotor();
        corExterna = entity.getCorExterna();
        quilometragemAtual = entity.getQuilometragemAtual();
        quilometragemCadastro = entity.getQuilometragemCadastro();
        dataUltimaRevisao = entity.getDataUltimaRevisao();
        proximaRevisaoKm = entity.getProximaRevisaoKm();
        proximaRevisaoData = entity.getProximaRevisaoData();
        status = entity.getStatus();
        observacoes = entity.getObservacoes();

        VeiculoResponse veiculoResponse = new VeiculoResponse( id, clienteId, clienteNome, marcaId, marcaNome, modeloId, modeloNome, anoModeloId, anoFabricacao, anoModelo, combustivelId, combustivelNome, placa, renavam, chassi, numeroMotor, corExterna, quilometragemAtual, quilometragemCadastro, dataUltimaRevisao, proximaRevisaoKm, proximaRevisaoData, status, observacoes );

        return veiculoResponse;
    }

    @Override
    public void updateEntityFromRequest(VeiculoRequest request, Veiculo entity) {
        if ( request == null ) {
            return;
        }

        if ( entity.getCliente() == null ) {
            entity.setCliente( new Cliente() );
        }
        veiculoRequestToCliente1( request, entity.getCliente() );
        if ( entity.getMarca() == null ) {
            entity.setMarca( new MarcaVeiculo() );
        }
        veiculoRequestToMarcaVeiculo1( request, entity.getMarca() );
        if ( entity.getModelo() == null ) {
            entity.setModelo( new ModeloVeiculo() );
        }
        veiculoRequestToModeloVeiculo1( request, entity.getModelo() );
        if ( entity.getAnoModelo() == null ) {
            entity.setAnoModelo( new AnoModelo() );
        }
        veiculoRequestToAnoModelo1( request, entity.getAnoModelo() );
        entity.setPlaca( request.placa() );
        entity.setRenavam( request.renavam() );
        entity.setChassi( request.chassi() );
        entity.setNumeroMotor( request.numeroMotor() );
        entity.setCorExterna( request.corExterna() );
        entity.setQuilometragemAtual( request.quilometragemAtual() );
        entity.setQuilometragemCadastro( request.quilometragemCadastro() );
        entity.setDataUltimaRevisao( request.dataUltimaRevisao() );
        entity.setProximaRevisaoKm( request.proximaRevisaoKm() );
        entity.setProximaRevisaoData( request.proximaRevisaoData() );
        entity.setStatus( request.status() );
        entity.setObservacoes( request.observacoes() );
    }

    protected Cliente veiculoRequestToCliente(VeiculoRequest veiculoRequest) {
        if ( veiculoRequest == null ) {
            return null;
        }

        Cliente cliente = new Cliente();

        cliente.setId( veiculoRequest.clienteId() );

        return cliente;
    }

    protected MarcaVeiculo veiculoRequestToMarcaVeiculo(VeiculoRequest veiculoRequest) {
        if ( veiculoRequest == null ) {
            return null;
        }

        MarcaVeiculo marcaVeiculo = new MarcaVeiculo();

        marcaVeiculo.setId( veiculoRequest.marcaId() );

        return marcaVeiculo;
    }

    protected ModeloVeiculo veiculoRequestToModeloVeiculo(VeiculoRequest veiculoRequest) {
        if ( veiculoRequest == null ) {
            return null;
        }

        ModeloVeiculo modeloVeiculo = new ModeloVeiculo();

        modeloVeiculo.setId( veiculoRequest.modeloId() );

        return modeloVeiculo;
    }

    protected AnoModelo veiculoRequestToAnoModelo(VeiculoRequest veiculoRequest) {
        if ( veiculoRequest == null ) {
            return null;
        }

        AnoModelo anoModelo = new AnoModelo();

        anoModelo.setId( veiculoRequest.anoModeloId() );

        return anoModelo;
    }

    private Long entityClienteId(Veiculo veiculo) {
        if ( veiculo == null ) {
            return null;
        }
        Cliente cliente = veiculo.getCliente();
        if ( cliente == null ) {
            return null;
        }
        Long id = cliente.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityClienteNomeCompleto(Veiculo veiculo) {
        if ( veiculo == null ) {
            return null;
        }
        Cliente cliente = veiculo.getCliente();
        if ( cliente == null ) {
            return null;
        }
        String nomeCompleto = cliente.getNomeCompleto();
        if ( nomeCompleto == null ) {
            return null;
        }
        return nomeCompleto;
    }

    private Long entityMarcaId(Veiculo veiculo) {
        if ( veiculo == null ) {
            return null;
        }
        MarcaVeiculo marca = veiculo.getMarca();
        if ( marca == null ) {
            return null;
        }
        Long id = marca.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityMarcaNome(Veiculo veiculo) {
        if ( veiculo == null ) {
            return null;
        }
        MarcaVeiculo marca = veiculo.getMarca();
        if ( marca == null ) {
            return null;
        }
        String nome = marca.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private Long entityModeloId(Veiculo veiculo) {
        if ( veiculo == null ) {
            return null;
        }
        ModeloVeiculo modelo = veiculo.getModelo();
        if ( modelo == null ) {
            return null;
        }
        Long id = modelo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityModeloNome(Veiculo veiculo) {
        if ( veiculo == null ) {
            return null;
        }
        ModeloVeiculo modelo = veiculo.getModelo();
        if ( modelo == null ) {
            return null;
        }
        String nome = modelo.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private Long entityAnoModeloId(Veiculo veiculo) {
        if ( veiculo == null ) {
            return null;
        }
        AnoModelo anoModelo = veiculo.getAnoModelo();
        if ( anoModelo == null ) {
            return null;
        }
        Long id = anoModelo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Integer entityAnoModeloAnoFabricacao(Veiculo veiculo) {
        if ( veiculo == null ) {
            return null;
        }
        AnoModelo anoModelo = veiculo.getAnoModelo();
        if ( anoModelo == null ) {
            return null;
        }
        Integer anoFabricacao = anoModelo.getAnoFabricacao();
        if ( anoFabricacao == null ) {
            return null;
        }
        return anoFabricacao;
    }

    private Integer entityAnoModeloAnoModelo(Veiculo veiculo) {
        if ( veiculo == null ) {
            return null;
        }
        AnoModelo anoModelo = veiculo.getAnoModelo();
        if ( anoModelo == null ) {
            return null;
        }
        Integer anoModelo1 = anoModelo.getAnoModelo();
        if ( anoModelo1 == null ) {
            return null;
        }
        return anoModelo1;
    }

    private Long entityTipoCombustivelId(Veiculo veiculo) {
        if ( veiculo == null ) {
            return null;
        }
        TipoCombustivel tipoCombustivel = veiculo.getTipoCombustivel();
        if ( tipoCombustivel == null ) {
            return null;
        }
        Long id = tipoCombustivel.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityTipoCombustivelNome(Veiculo veiculo) {
        if ( veiculo == null ) {
            return null;
        }
        TipoCombustivel tipoCombustivel = veiculo.getTipoCombustivel();
        if ( tipoCombustivel == null ) {
            return null;
        }
        String nome = tipoCombustivel.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    protected void veiculoRequestToCliente1(VeiculoRequest veiculoRequest, Cliente mappingTarget) {
        if ( veiculoRequest == null ) {
            return;
        }

        mappingTarget.setId( veiculoRequest.clienteId() );
    }

    protected void veiculoRequestToMarcaVeiculo1(VeiculoRequest veiculoRequest, MarcaVeiculo mappingTarget) {
        if ( veiculoRequest == null ) {
            return;
        }

        mappingTarget.setId( veiculoRequest.marcaId() );
    }

    protected void veiculoRequestToModeloVeiculo1(VeiculoRequest veiculoRequest, ModeloVeiculo mappingTarget) {
        if ( veiculoRequest == null ) {
            return;
        }

        mappingTarget.setId( veiculoRequest.modeloId() );
    }

    protected void veiculoRequestToAnoModelo1(VeiculoRequest veiculoRequest, AnoModelo mappingTarget) {
        if ( veiculoRequest == null ) {
            return;
        }

        mappingTarget.setId( veiculoRequest.anoModeloId() );
    }
}
