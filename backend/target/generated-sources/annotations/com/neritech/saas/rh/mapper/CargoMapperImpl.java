package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Cargo;
import com.neritech.saas.rh.dto.CargoRequest;
import com.neritech.saas.rh.dto.CargoResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:29-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CargoMapperImpl implements CargoMapper {

    @Override
    public Cargo toEntity(CargoRequest request) {
        if ( request == null ) {
            return null;
        }

        Cargo cargo = new Cargo();

        cargo.setEmpresaId( request.empresaId() );
        cargo.setNome( request.nome() );
        cargo.setDescricao( request.descricao() );
        cargo.setCodigoCbo( request.codigoCbo() );
        cargo.setNivelHierarquico( request.nivelHierarquico() );
        cargo.setCargoSuperiorId( request.cargoSuperiorId() );
        cargo.setSalarioBaseMinimo( request.salarioBaseMinimo() );
        cargo.setSalarioBaseMaximo( request.salarioBaseMaximo() );
        cargo.setRequisitos( request.requisitos() );
        cargo.setResponsabilidades( request.responsabilidades() );
        cargo.setBeneficios( request.beneficios() );
        cargo.setTemComissao( request.temComissao() );
        cargo.setPercentualComissaoPadrao( request.percentualComissaoPadrao() );
        cargo.setMetaVendasMensal( request.metaVendasMensal() );
        cargo.setCargaHorariaSemanal( request.cargaHorariaSemanal() );
        cargo.setAtivo( request.ativo() );

        return cargo;
    }

    @Override
    public CargoResponse toResponse(Cargo entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nome = null;
        String descricao = null;
        String codigoCbo = null;
        Integer nivelHierarquico = null;
        Long cargoSuperiorId = null;
        BigDecimal salarioBaseMinimo = null;
        BigDecimal salarioBaseMaximo = null;
        String requisitos = null;
        String responsabilidades = null;
        String beneficios = null;
        Boolean temComissao = null;
        BigDecimal percentualComissaoPadrao = null;
        BigDecimal metaVendasMensal = null;
        Integer cargaHorariaSemanal = null;
        Boolean ativo = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        codigoCbo = entity.getCodigoCbo();
        nivelHierarquico = entity.getNivelHierarquico();
        cargoSuperiorId = entity.getCargoSuperiorId();
        salarioBaseMinimo = entity.getSalarioBaseMinimo();
        salarioBaseMaximo = entity.getSalarioBaseMaximo();
        requisitos = entity.getRequisitos();
        responsabilidades = entity.getResponsabilidades();
        beneficios = entity.getBeneficios();
        temComissao = entity.getTemComissao();
        percentualComissaoPadrao = entity.getPercentualComissaoPadrao();
        metaVendasMensal = entity.getMetaVendasMensal();
        cargaHorariaSemanal = entity.getCargaHorariaSemanal();
        ativo = entity.getAtivo();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        CargoResponse cargoResponse = new CargoResponse( id, empresaId, nome, descricao, codigoCbo, nivelHierarquico, cargoSuperiorId, salarioBaseMinimo, salarioBaseMaximo, requisitos, responsabilidades, beneficios, temComissao, percentualComissaoPadrao, metaVendasMensal, cargaHorariaSemanal, ativo, dataCadastro, dataAtualizacao );

        return cargoResponse;
    }

    @Override
    public void updateEntity(CargoRequest request, Cargo entity) {
        if ( request == null ) {
            return;
        }

        if ( request.empresaId() != null ) {
            entity.setEmpresaId( request.empresaId() );
        }
        if ( request.nome() != null ) {
            entity.setNome( request.nome() );
        }
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.codigoCbo() != null ) {
            entity.setCodigoCbo( request.codigoCbo() );
        }
        if ( request.nivelHierarquico() != null ) {
            entity.setNivelHierarquico( request.nivelHierarquico() );
        }
        if ( request.cargoSuperiorId() != null ) {
            entity.setCargoSuperiorId( request.cargoSuperiorId() );
        }
        if ( request.salarioBaseMinimo() != null ) {
            entity.setSalarioBaseMinimo( request.salarioBaseMinimo() );
        }
        if ( request.salarioBaseMaximo() != null ) {
            entity.setSalarioBaseMaximo( request.salarioBaseMaximo() );
        }
        if ( request.requisitos() != null ) {
            entity.setRequisitos( request.requisitos() );
        }
        if ( request.responsabilidades() != null ) {
            entity.setResponsabilidades( request.responsabilidades() );
        }
        if ( request.beneficios() != null ) {
            entity.setBeneficios( request.beneficios() );
        }
        if ( request.temComissao() != null ) {
            entity.setTemComissao( request.temComissao() );
        }
        if ( request.percentualComissaoPadrao() != null ) {
            entity.setPercentualComissaoPadrao( request.percentualComissaoPadrao() );
        }
        if ( request.metaVendasMensal() != null ) {
            entity.setMetaVendasMensal( request.metaVendasMensal() );
        }
        if ( request.cargaHorariaSemanal() != null ) {
            entity.setCargaHorariaSemanal( request.cargaHorariaSemanal() );
        }
        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
    }
}
