package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.Fatura;
import com.neritech.saas.financeiro.domain.Pagamento;
import com.neritech.saas.financeiro.domain.ParcelaPagamento;
import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import com.neritech.saas.financeiro.dto.PagamentoRequest;
import com.neritech.saas.financeiro.dto.PagamentoResponse;
import com.neritech.saas.financeiro.dto.ParcelaPagamentoResponse;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:38-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class PagamentoMapperImpl implements PagamentoMapper {

    @Autowired
    private ParcelaPagamentoMapper parcelaPagamentoMapper;
    private final DatatypeFactory datatypeFactory;

    public PagamentoMapperImpl() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        }
        catch ( DatatypeConfigurationException ex ) {
            throw new RuntimeException( ex );
        }
    }

    @Override
    public Pagamento toEntity(PagamentoRequest request) {
        if ( request == null ) {
            return null;
        }

        Pagamento pagamento = new Pagamento();

        pagamento.setClienteId( request.clienteId() );
        pagamento.setComprovanteUrl( request.comprovanteUrl() );
        pagamento.setDataPagamento( xmlGregorianCalendarToLocalDateTime( localDateToXmlGregorianCalendar( request.dataPagamento() ) ) );
        pagamento.setObservacoes( request.observacoes() );
        pagamento.setStatus( request.status() );

        return pagamento;
    }

    @Override
    public PagamentoResponse toResponse(Pagamento entity) {
        if ( entity == null ) {
            return null;
        }

        String faturaNumero = null;
        Long id = null;
        Long empresaId = null;
        Long clienteId = null;
        LocalDate dataPagamento = null;
        StatusPagamento status = null;
        String comprovanteUrl = null;
        String observacoes = null;
        List<ParcelaPagamentoResponse> parcelas = null;

        faturaNumero = entityFaturaNumeroFatura( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        clienteId = entity.getClienteId();
        dataPagamento = xmlGregorianCalendarToLocalDate( localDateTimeToXmlGregorianCalendar( entity.getDataPagamento() ) );
        status = entity.getStatus();
        comprovanteUrl = entity.getComprovanteUrl();
        observacoes = entity.getObservacoes();
        parcelas = parcelaPagamentoListToParcelaPagamentoResponseList( entity.getParcelas() );

        Long faturaId = null;
        Long fornecedorId = null;
        Long formaPagamentoId = null;
        String formaPagamentoNome = null;
        Long condicaoPagamentoId = null;
        String condicaoPagamentoNome = null;
        Long contaBancariaId = null;
        String contaBancariaNome = null;
        BigDecimal valorOriginal = null;
        BigDecimal valorDesconto = null;
        BigDecimal valorJuros = null;
        BigDecimal valorMulta = null;
        BigDecimal valorTotal = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        PagamentoResponse pagamentoResponse = new PagamentoResponse( id, empresaId, faturaId, faturaNumero, fornecedorId, clienteId, formaPagamentoId, formaPagamentoNome, condicaoPagamentoId, condicaoPagamentoNome, contaBancariaId, contaBancariaNome, dataPagamento, valorOriginal, valorDesconto, valorJuros, valorMulta, valorTotal, status, comprovanteUrl, observacoes, parcelas, createdAt, updatedAt );

        return pagamentoResponse;
    }

    @Override
    public void updateEntityFromDTO(PagamentoRequest request, Pagamento entity) {
        if ( request == null ) {
            return;
        }

        if ( request.clienteId() != null ) {
            entity.setClienteId( request.clienteId() );
        }
        if ( request.comprovanteUrl() != null ) {
            entity.setComprovanteUrl( request.comprovanteUrl() );
        }
        if ( request.dataPagamento() != null ) {
            entity.setDataPagamento( xmlGregorianCalendarToLocalDateTime( localDateToXmlGregorianCalendar( request.dataPagamento() ) ) );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
    }

    private XMLGregorianCalendar localDateToXmlGregorianCalendar( LocalDate localDate ) {
        if ( localDate == null ) {
            return null;
        }

        return datatypeFactory.newXMLGregorianCalendarDate(
            localDate.getYear(),
            localDate.getMonthValue(),
            localDate.getDayOfMonth(),
            DatatypeConstants.FIELD_UNDEFINED );
    }

    private XMLGregorianCalendar localDateTimeToXmlGregorianCalendar( LocalDateTime localDateTime ) {
        if ( localDateTime == null ) {
            return null;
        }

        return datatypeFactory.newXMLGregorianCalendar(
            localDateTime.getYear(),
            localDateTime.getMonthValue(),
            localDateTime.getDayOfMonth(),
            localDateTime.getHour(),
            localDateTime.getMinute(),
            localDateTime.getSecond(),
            localDateTime.get( ChronoField.MILLI_OF_SECOND ),
            DatatypeConstants.FIELD_UNDEFINED );
    }

    private static LocalDate xmlGregorianCalendarToLocalDate( XMLGregorianCalendar xcal ) {
        if ( xcal == null ) {
            return null;
        }

        return LocalDate.of( xcal.getYear(), xcal.getMonth(), xcal.getDay() );
    }

    private static LocalDateTime xmlGregorianCalendarToLocalDateTime( XMLGregorianCalendar xcal ) {
        if ( xcal == null ) {
            return null;
        }

        if ( xcal.getYear() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getMonth() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getDay() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getHour() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getMinute() != DatatypeConstants.FIELD_UNDEFINED
        ) {
            if ( xcal.getSecond() != DatatypeConstants.FIELD_UNDEFINED
                && xcal.getMillisecond() != DatatypeConstants.FIELD_UNDEFINED ) {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute(),
                    xcal.getSecond(),
                    Duration.ofMillis( xcal.getMillisecond() ).getNano()
                );
            }
            else if ( xcal.getSecond() != DatatypeConstants.FIELD_UNDEFINED ) {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute(),
                    xcal.getSecond()
                );
            }
            else {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute()
                );
            }
        }
        return null;
    }

    private String entityFaturaNumeroFatura(Pagamento pagamento) {
        if ( pagamento == null ) {
            return null;
        }
        Fatura fatura = pagamento.getFatura();
        if ( fatura == null ) {
            return null;
        }
        String numeroFatura = fatura.getNumeroFatura();
        if ( numeroFatura == null ) {
            return null;
        }
        return numeroFatura;
    }

    protected List<ParcelaPagamentoResponse> parcelaPagamentoListToParcelaPagamentoResponseList(List<ParcelaPagamento> list) {
        if ( list == null ) {
            return null;
        }

        List<ParcelaPagamentoResponse> list1 = new ArrayList<ParcelaPagamentoResponse>( list.size() );
        for ( ParcelaPagamento parcelaPagamento : list ) {
            list1.add( parcelaPagamentoMapper.toResponse( parcelaPagamento ) );
        }

        return list1;
    }
}
