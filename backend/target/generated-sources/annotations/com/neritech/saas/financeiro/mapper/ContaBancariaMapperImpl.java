package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ContaBancaria;
import com.neritech.saas.financeiro.domain.enums.TipoConta;
import com.neritech.saas.financeiro.dto.ContaBancariaRequest;
import com.neritech.saas.financeiro.dto.ContaBancariaResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:45-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ContaBancariaMapperImpl implements ContaBancariaMapper {

    @Override
    public ContaBancaria toEntity(ContaBancariaRequest request) {
        if ( request == null ) {
            return null;
        }

        ContaBancaria contaBancaria = new ContaBancaria();

        contaBancaria.setAtivo( request.ativo() );
        contaBancaria.setLimiteChequeEspecial( request.limiteChequeEspecial() );
        contaBancaria.setSaldoAtual( request.saldoAtual() );
        contaBancaria.setBancoCodigo( request.bancoCodigo() );
        contaBancaria.setBancoNome( request.bancoNome() );
        contaBancaria.setAgencia( request.agencia() );
        contaBancaria.setConta( request.conta() );
        contaBancaria.setDigitoConta( request.digitoConta() );
        contaBancaria.setTipoConta( request.tipoConta() );
        contaBancaria.setTitularConta( request.titularConta() );
        contaBancaria.setCpfCnpjTitular( request.cpfCnpjTitular() );
        contaBancaria.setDataUltimoSaldo( request.dataUltimoSaldo() );

        return contaBancaria;
    }

    @Override
    public ContaBancariaResponse toResponse(ContaBancaria entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String bancoCodigo = null;
        String bancoNome = null;
        String agencia = null;
        String conta = null;
        String digitoConta = null;
        TipoConta tipoConta = null;
        String titularConta = null;
        String cpfCnpjTitular = null;
        BigDecimal saldoAtual = null;
        BigDecimal limiteChequeEspecial = null;
        LocalDate dataUltimoSaldo = null;
        Boolean ativo = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        bancoCodigo = entity.getBancoCodigo();
        bancoNome = entity.getBancoNome();
        agencia = entity.getAgencia();
        conta = entity.getConta();
        digitoConta = entity.getDigitoConta();
        tipoConta = entity.getTipoConta();
        titularConta = entity.getTitularConta();
        cpfCnpjTitular = entity.getCpfCnpjTitular();
        saldoAtual = entity.getSaldoAtual();
        limiteChequeEspecial = entity.getLimiteChequeEspecial();
        dataUltimoSaldo = entity.getDataUltimoSaldo();
        ativo = entity.getAtivo();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        ContaBancariaResponse contaBancariaResponse = new ContaBancariaResponse( id, empresaId, bancoCodigo, bancoNome, agencia, conta, digitoConta, tipoConta, titularConta, cpfCnpjTitular, saldoAtual, limiteChequeEspecial, dataUltimoSaldo, ativo, dataCadastro, dataAtualizacao );

        return contaBancariaResponse;
    }

    @Override
    public void updateEntityFromDTO(ContaBancariaRequest request, ContaBancaria entity) {
        if ( request == null ) {
            return;
        }

        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
        if ( request.limiteChequeEspecial() != null ) {
            entity.setLimiteChequeEspecial( request.limiteChequeEspecial() );
        }
        if ( request.saldoAtual() != null ) {
            entity.setSaldoAtual( request.saldoAtual() );
        }
        if ( request.bancoCodigo() != null ) {
            entity.setBancoCodigo( request.bancoCodigo() );
        }
        if ( request.bancoNome() != null ) {
            entity.setBancoNome( request.bancoNome() );
        }
        if ( request.agencia() != null ) {
            entity.setAgencia( request.agencia() );
        }
        if ( request.conta() != null ) {
            entity.setConta( request.conta() );
        }
        if ( request.digitoConta() != null ) {
            entity.setDigitoConta( request.digitoConta() );
        }
        if ( request.tipoConta() != null ) {
            entity.setTipoConta( request.tipoConta() );
        }
        if ( request.titularConta() != null ) {
            entity.setTitularConta( request.titularConta() );
        }
        if ( request.cpfCnpjTitular() != null ) {
            entity.setCpfCnpjTitular( request.cpfCnpjTitular() );
        }
        if ( request.dataUltimoSaldo() != null ) {
            entity.setDataUltimoSaldo( request.dataUltimoSaldo() );
        }
    }
}
