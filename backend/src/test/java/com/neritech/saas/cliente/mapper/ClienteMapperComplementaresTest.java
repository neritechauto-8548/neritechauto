package com.neritech.saas.cliente.mapper;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.ContatoCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.cliente.dto.ClienteRequest;
import com.neritech.saas.cliente.dto.ContatoClienteRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ClienteMapperComplementaresTest {
    @Test void preservaConsentimentoIndefinidoECamposLegais() {
        var req = new ClienteRequest();
        req.setTipoCliente(TipoCliente.PESSOA_JURIDICA);
        req.setNomeCompleto("Oficina Exemplo");
        req.setNumeroApolice("AP-123");
        req.setLimiteCredito(new BigDecimal("2500.00"));
        req.setOrgaoPublico(false);
        req.setRetencaoIss(true);
        req.setAlertaLgpd(true);
        req.setAutorizacaoLgpd(null);
        Cliente entity = ClienteMapper.toEntity(req);
        assertEquals("AP-123", entity.getNumeroApolice());
        assertEquals(new BigDecimal("2500.00"), entity.getLimiteCredito());
        assertTrue(entity.getRetencaoIss());
        assertNull(entity.getAutorizacaoLgpd());
        var response = ClienteMapper.toResponse(entity);
        assertEquals("AP-123", response.getNumeroApolice());
        assertNull(response.getAutorizacaoLgpd());
    }

    @Test void atualizacaoMantemCamposComplementares() {
        var req = new ClienteRequest();
        req.setDocumentoEstrangeiro("PASSAPORTE123");
        req.setPais("Brasil");
        req.setEstadoCivil("CASADO");
        req.setProfissao("Mecânico");
        req.setAutorizacaoLgpd(false);
        var entity = new Cliente();
        ClienteMapper.updateEntity(entity, req);
        assertEquals("Mecânico", entity.getProfissao());
        assertEquals("PASSAPORTE123", entity.getDocumentoEstrangeiro());
        assertFalse(entity.getAutorizacaoLgpd());
    }

    @Test void contatoPermiteIdentificacaoSemConsentimentoImplicitamente() {
        var req = new ContatoClienteRequest();
        req.setNome("Ana");
        req.setSexo("F");
        req.setDataNascimento(LocalDate.of(1990,5,4));
        req.setGrupoPrincipal(true);
        var entity = ContatoClienteMapper.toEntity(req);
        assertEquals("Ana", entity.getNome());
        assertEquals("F", entity.getSexo());
        assertTrue(entity.getGrupoPrincipal());
        assertEquals("Ana", ContatoClienteMapper.toResponse(entity).getNome());
    }
}
