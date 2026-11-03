package com.neritech.saas.empresa.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.empresa.dto.ContatoEmpresaRequest;
import com.neritech.saas.common.exception.BusinessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContatoEmpresaService {
    private final JdbcTemplate banco;
    private final EmpresaAlteracaoService auditoria;

    public ContatoEmpresaService(JdbcTemplate banco, EmpresaAlteracaoService auditoria) {
        this.banco = banco;
        this.auditoria = auditoria;
    }

    @Transactional(readOnly = true)
    public List<ContatoEmpresaRequest> listar(Long empresaId) {
        TenantAccess.require(empresaId);
        return banco.query("select tipo_contato, contato from contatos_empresa where empresa_id=? order by ordem,id",
                (rs, linha) -> new ContatoEmpresaRequest(rs.getString("tipo_contato"), rs.getString("contato")), empresaId);
    }

    @Transactional
    public void substituir(Long empresaId, List<ContatoEmpresaRequest> contatos) {
        TenantAccess.require(empresaId);
        List<ContatoEmpresaRequest> anteriores = listar(empresaId);
        banco.update("delete from contatos_empresa where empresa_id=?", empresaId);
        for (int ordem = 0; ordem < contatos.size(); ordem++) {
            ContatoEmpresaRequest contato = contatos.get(ordem);
            if ("EMAIL".equals(contato.tipoContato())
                    && !contato.contato().trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                throw new BusinessException("O e-mail de contato informado não é válido.");
            }
            if ("TELEFONE".equals(contato.tipoContato())) {
                long digitos = contato.contato().chars().filter(Character::isDigit).count();
                if (digitos < 8 || digitos > 15) {
                    throw new BusinessException("O telefone de contato deve conter de 8 a 15 dígitos.");
                }
            }
            banco.update("insert into contatos_empresa(empresa_id,tipo_contato,contato,ordem) values(?,?,?,?)",
                    empresaId, contato.tipoContato(), contato.contato().trim(), ordem);
        }
        Map<String, Object> antes = new LinkedHashMap<>();
        antes.put("contatos", anteriores);
        Map<String, Object> depois = new LinkedHashMap<>();
        depois.put("contatos", contatos);
        auditoria.registrar(empresaId, "contatos_empresa", empresaId, null, antes, depois);
    }
}
