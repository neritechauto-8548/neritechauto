package com.neritech.saas.configuracoes;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.common.tenancy.TenantContext;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConfiguracoesSegurasService {
    private static final String CIPHER = "AES/GCM/NoPadding";
    private final JdbcTemplate banco;
    private final String chaveBase64;

    public ConfiguracoesSegurasService(JdbcTemplate banco, @Value("${neritech.criptografia.chave:${NERITECH_CHAVE_CRIPTOGRAFIA:}}") String chaveBase64) {
        this.banco = banco;
        this.chaveBase64 = chaveBase64;
    }

    @Transactional(readOnly = true)
    public Map<String,Object> obterSmtp(String escopo) {
        validarEscopo(escopo); Long empresa = TenantAccess.requireCurrent();
        return banco.query("select email, servidor_smtp, porta_smtp, usar_ssl, usar_tls, assinatura, (octet_length(senha_cifrada)>0) tem_senha from configuracoes_smtp_empresa where empresa_id=? and escopo=?",
            rs -> rs.next() ? Map.of("email",rs.getString("email"),"servidorSmtp",rs.getString("servidor_smtp"),"portaSmtp",rs.getInt("porta_smtp"),"usarSsl",rs.getBoolean("usar_ssl"),"usarTls",rs.getBoolean("usar_tls"),"assinatura",rs.getString("assinatura") == null ? "" : rs.getString("assinatura"),"temSenha",rs.getBoolean("tem_senha")) : Map.of("email","","servidorSmtp","","portaSmtp",587,"usarSsl",false,"usarTls",false,"assinatura","","temSenha",false), empresa, escopo);
    }

    @Transactional(readOnly = true)
    public Map<String,Object> obterSmtpPerfil() {
        Long usuario=usuarioAtualId(), empresa=TenantAccess.requireCurrent();
        return banco.query("select email,servidor_smtp,porta_smtp,usar_ssl,usar_tls,assinatura from configuracoes_smtp_usuario where usuario_id=? and empresa_id=?",rs -> rs.next()?Map.of("email",rs.getString(1),"servidorSmtp",rs.getString(2),"portaSmtp",rs.getInt(3),"usarSsl",rs.getBoolean(4),"usarTls",rs.getBoolean(5),"assinatura",rs.getString(6),"temSenha",true):Map.of("email","","servidorSmtp","","portaSmtp",587,"usarSsl",false,"usarTls",false,"assinatura","","temSenha",false),usuario,empresa);
    }

    @Transactional
    public Map<String,Object> salvarSmtpPerfil(RequisicaoSmtp req) {
        validarSmtp(req);
        Long usuario=usuarioAtualId(), empresa=TenantAccess.requireCurrent();
        byte[] senha=req.senha()!=null&&!req.senha().isBlank()?cifrar(req.senha().getBytes(StandardCharsets.UTF_8)):banco.query("select senha_cifrada from configuracoes_smtp_usuario where usuario_id=? and empresa_id=?",rs->rs.next()?rs.getBytes(1):null,usuario,empresa);
        if(senha==null) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Informe a senha SMTP na primeira configuração.");
        banco.update("insert into configuracoes_smtp_usuario(usuario_id,empresa_id,email,senha_cifrada,servidor_smtp,porta_smtp,usar_ssl,usar_tls,assinatura) values(?,?,?,?,?,?,?,?,?) on conflict(usuario_id) do update set empresa_id=excluded.empresa_id,email=excluded.email,senha_cifrada=excluded.senha_cifrada,servidor_smtp=excluded.servidor_smtp,porta_smtp=excluded.porta_smtp,usar_ssl=excluded.usar_ssl,usar_tls=excluded.usar_tls,assinatura=excluded.assinatura,atualizado_em=current_timestamp",usuario,empresa,req.email(),senha,req.servidorSmtp(),req.portaSmtp(),req.usarSsl(),req.usarTls(),req.assinatura()==null?"":req.assinatura());
        return obterSmtpPerfil();
    }

    @Transactional(readOnly = true)
    public void verificarSmtpPerfil(RequisicaoSmtp req) {
        validarSmtp(req); Long usuario=usuarioAtualId(),empresa=TenantAccess.requireCurrent();
        String senha=req.senha();
        if(senha==null||senha.isBlank()){byte[] cifrada=banco.query("select senha_cifrada from configuracoes_smtp_usuario where usuario_id=? and empresa_id=?",rs->rs.next()?rs.getBytes(1):null,usuario,empresa);if(cifrada==null)throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Informe a senha SMTP para verificar.");senha=new String(decifrar(cifrada),StandardCharsets.UTF_8);}
        enviarTeste(req.email(),req.servidorSmtp(),req.portaSmtp(),req.usarSsl(),req.usarTls(),senha);
    }

    private void enviarTeste(String email,String servidor,Integer porta,Boolean ssl,Boolean tls,String senhaTexto) {
        JavaMailSenderImpl remetente=new JavaMailSenderImpl();remetente.setHost(servidor);remetente.setPort(porta);remetente.setUsername(email);remetente.setPassword(senhaTexto);
        var props=remetente.getJavaMailProperties();props.put("mail.smtp.auth","true");props.put("mail.smtp.connectiontimeout","10000");props.put("mail.smtp.timeout","10000");props.put("mail.smtp.writetimeout","10000");if(ssl)props.put("mail.smtp.ssl.enable","true");if(tls)props.put("mail.smtp.starttls.enable","true");
        try{MimeMessage msg=remetente.createMimeMessage();var helper=new org.springframework.mail.javamail.MimeMessageHelper(msg,false,"UTF-8");helper.setFrom(email);helper.setTo(email);helper.setSubject("Verificação de configuração de e-mail");helper.setText("Sua configuração SMTP pessoal foi verificada com sucesso.",false);remetente.send(msg);}catch(Exception falha){throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"Não foi possível enviar o e-mail de verificação. Confira servidor, porta, SSL/TLS e credenciais.",falha);}
    }

    @Transactional
    public Map<String,Object> salvarSmtp(String escopo, RequisicaoSmtp req) {
        validarEscopo(escopo); Long empresa=TenantAccess.requireCurrent(); validarSmtp(req);
        byte[] senha = req.senha()!=null && !req.senha().isBlank() ? cifrar(req.senha().getBytes(StandardCharsets.UTF_8)) : banco.query("select senha_cifrada from configuracoes_smtp_empresa where empresa_id=? and escopo=?", rs -> rs.next()?rs.getBytes(1):null, empresa,escopo);
        if (senha==null) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Informe a senha SMTP na primeira configuração");
        Long usuario=usuarioAtualId();
        banco.update("insert into configuracoes_smtp_empresa(empresa_id,escopo,email,senha_cifrada,servidor_smtp,porta_smtp,usar_ssl,usar_tls,assinatura,criado_por,atualizado_por) values(?,?,?,?,?,?,?,?,?,?,?) on conflict(empresa_id,escopo) do update set email=excluded.email,senha_cifrada=excluded.senha_cifrada,servidor_smtp=excluded.servidor_smtp,porta_smtp=excluded.porta_smtp,usar_ssl=excluded.usar_ssl,usar_tls=excluded.usar_tls,assinatura=excluded.assinatura,data_atualizacao=current_timestamp,atualizado_por=excluded.atualizado_por,versao=configuracoes_smtp_empresa.versao+1",
            empresa,escopo,req.email(),senha,req.servidorSmtp(),req.portaSmtp(),req.usarSsl(),req.usarTls(),req.assinatura(),usuario,usuario);
        return obterSmtp(escopo);
    }

    @Transactional(readOnly = true)
    public void verificarSmtp(String escopo, RequisicaoSmtp requisicao) {
        validarEscopo(escopo); Long empresa=TenantAccess.requireCurrent();
        byte[] senha=requisicao==null||requisicao.senha()==null||requisicao.senha().isBlank()?banco.query("select senha_cifrada from configuracoes_smtp_empresa where empresa_id=? and escopo=?",rs->rs.next()?rs.getBytes(1):null,empresa,escopo):cifrar(requisicao.senha().getBytes(StandardCharsets.UTF_8));
        String email=requisicao==null?null:requisicao.email(); String servidor=requisicao==null?null:requisicao.servidorSmtp(); Integer porta=requisicao==null?null:requisicao.portaSmtp(); Boolean ssl=requisicao==null?null:requisicao.usarSsl(); Boolean tls=requisicao==null?null:requisicao.usarTls();
        if(email==null||servidor==null||porta==null||ssl==null||tls==null){var salvo=banco.query("select email,servidor_smtp,porta_smtp,usar_ssl,usar_tls from configuracoes_smtp_empresa where empresa_id=? and escopo=?",rs->rs.next()?new DadosSmtp(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getBoolean(4),rs.getBoolean(5)):null,empresa,escopo);if(salvo==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Informe e salve a configuração SMTP antes de verificar");if(email==null)email=salvo.email();if(servidor==null)servidor=salvo.servidor();if(porta==null)porta=salvo.porta();if(ssl==null)ssl=salvo.ssl();if(tls==null)tls=salvo.tls();}
        if(senha==null)throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Informe a senha SMTP antes de verificar");
        JavaMailSenderImpl remetente=new JavaMailSenderImpl(); remetente.setHost(servidor); remetente.setPort(porta); remetente.setUsername(email); remetente.setPassword(new String(decifrar(senha),StandardCharsets.UTF_8));
        var propriedades=remetente.getJavaMailProperties(); propriedades.put("mail.smtp.auth","true"); propriedades.put("mail.smtp.connectiontimeout","10000"); propriedades.put("mail.smtp.timeout","10000"); propriedades.put("mail.smtp.writetimeout","10000");
        if(ssl) propriedades.put("mail.smtp.ssl.enable","true"); if(tls) propriedades.put("mail.smtp.starttls.enable","true");
        try { MimeMessage msg=remetente.createMimeMessage(); var helper=new org.springframework.mail.javamail.MimeMessageHelper(msg,true,"UTF-8"); helper.setTo(email); helper.setSubject("Verificação de configuração de e-mail"); helper.setText("A configuração SMTP da sua empresa foi verificada com sucesso.",false); remetente.send(msg); }
        catch(Exception falha){ throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"Não foi possível autenticar e enviar o e-mail de verificação. Confira servidor, porta, SSL/TLS e credenciais.",falha); }
    }

    @Transactional(readOnly = true)
    public boolean enviarPorSmtpConfigurado(String escopo, String destinatario, String assunto, String conteudo) {
        validarEscopo(escopo); Long empresa=TenantContext.getCurrentTenant(); if(empresa==null)return false;
        var dados=banco.query("select email,senha_cifrada,servidor_smtp,porta_smtp,usar_ssl,usar_tls,assinatura from configuracoes_smtp_empresa where empresa_id=? and escopo=?",rs->rs.next()?new SmtpCompleto(rs.getString(1),rs.getBytes(2),rs.getString(3),rs.getInt(4),rs.getBoolean(5),rs.getBoolean(6),rs.getString(7)):null,empresa,escopo);
        if(dados==null)return false;
        JavaMailSenderImpl remetente=new JavaMailSenderImpl();remetente.setHost(dados.servidor());remetente.setPort(dados.porta());remetente.setUsername(dados.email());remetente.setPassword(new String(decifrar(dados.senha()),StandardCharsets.UTF_8));
        var propriedades=remetente.getJavaMailProperties();propriedades.put("mail.smtp.auth","true");propriedades.put("mail.smtp.connectiontimeout","10000");propriedades.put("mail.smtp.timeout","10000");propriedades.put("mail.smtp.writetimeout","10000");if(dados.ssl())propriedades.put("mail.smtp.ssl.enable","true");if(dados.tls())propriedades.put("mail.smtp.starttls.enable","true");
        try{MimeMessage mensagem=remetente.createMimeMessage();var auxiliar=new org.springframework.mail.javamail.MimeMessageHelper(mensagem,true,"UTF-8");auxiliar.setFrom(dados.email());auxiliar.setTo(destinatario);auxiliar.setSubject(assunto);String assinatura=dados.assinatura()!=null&&!dados.assinatura().isBlank()?"<br><br>"+dados.assinatura():"";auxiliar.setText((conteudo==null?"":conteudo)+assinatura,true);remetente.send(mensagem);return true;}catch(Exception falha){throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"Falha no envio pelo SMTP configurado para esta empresa",falha);}
    }

    @Transactional(readOnly = true)
    public List<Map<String,Object>> listarCertificados() {
        Long empresa=TenantAccess.requireCurrent(); return banco.query("select id,nome_certificado,email_contato,data_vencimento,data_cadastro from certificados_digitais where empresa_id=? order by data_cadastro desc",(rs,n)->Map.of("id",rs.getLong("id"),"nomeArquivo",rs.getString("nome_certificado"),"email",rs.getString("email_contato")==null?"":rs.getString("email_contato"),"dataValidade",rs.getDate("data_vencimento").toLocalDate().atStartOfDay(),"dataCadastro",rs.getTimestamp("data_cadastro").toLocalDateTime()),empresa);
    }

    @Transactional
    public void criarCertificado(String nome, String email, String senha, byte[] arquivo) {
        Long empresa=TenantAccess.requireCurrent(); if(nome==null||nome.isBlank()||nome.length()>255||senha==null||senha.isBlank()||arquivo==null||arquivo.length<1||arquivo.length>5_242_880) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Informe arquivo A1 (.pfx/.p12) de até 5 MB, senha e nome válido");
        try {
            KeyStore cofre=KeyStore.getInstance("PKCS12"); cofre.load(new ByteArrayInputStream(arquivo),senha.toCharArray());
            var aliases=cofre.aliases(); if(!aliases.hasMoreElements()) throw new IllegalArgumentException("Sem certificado");
            X509Certificate cert=(X509Certificate)cofre.getCertificate(aliases.nextElement()); Date validade=cert.getNotAfter(); String titular=cert.getSubjectX500Principal().getName();
            String cpfCnpj=java.util.Arrays.stream(titular.split(",")).filter(parte->parte.trim().toUpperCase().startsWith("SERIALNUMBER=")).map(parte->parte.substring(parte.indexOf('=')+1).replaceAll("[^0-9]","")).filter(valor->!valor.isBlank()).findFirst().orElse("");
            String nomeFinal=nome==null||nome.isBlank()?"Certificado A1":nome;
            banco.update("insert into certificados_digitais(empresa_id,tipo_certificado,nome_certificado,numero_serie,emissor,titular,cpf_cnpj_titular,data_emissao,data_vencimento,arquivo_certificado,senha_certificado,ativo,principal,usado_nfe,data_instalacao,data_cadastro,cadastrado_por,arquivo_cifrado,senha_cifrada,email_contato) values(?,'A1',?,?,?,?,?,?,?,null,null,true,false,true,current_timestamp,current_timestamp,?,?,?,?)",empresa,nomeFinal,cert.getSerialNumber().toString(16),cert.getIssuerX500Principal().getName(),titular,cpfCnpj,java.sql.Date.valueOf(cert.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),java.sql.Date.valueOf(validade.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),usuarioAtualId(),cifrar(arquivo),cifrar(senha.getBytes(StandardCharsets.UTF_8)),email==null||email.isBlank()?null:email);
        } catch(ResponseStatusException falha){throw falha;} catch(Exception falha){throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Arquivo ou senha inválidos. Use um certificado A1 válido no formato .pfx ou .p12.",falha);}
    }

    @Transactional(readOnly = true)
    public Map<String,Object> obterSenhaPrefeituraNfse() {
        Long empresa = TenantAccess.requireCurrent();
        boolean cadastrada = banco.queryForObject("select exists(select 1 from configuracoes_complementares_empresa where empresa_id=? and secao='FISCAL_NFSE' and senha_nfse_cifrada is not null)", Boolean.class, empresa);
        return Map.of("temSenha", cadastrada);
    }

    @Transactional
    public Map<String,Object> salvarSenhaPrefeituraNfse(String senha) {
        Long empresa = TenantAccess.requireCurrent();
        if (senha == null || senha.isBlank() || senha.length() > 255) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Informe uma senha válida para o acesso à prefeitura.");
        Long usuario = usuarioAtualId();
        byte[] protegida = cifrar(senha.getBytes(StandardCharsets.UTF_8));
        banco.update("insert into configuracoes_complementares_empresa(empresa_id,secao,dados,versao,atualizado_por,senha_nfse_cifrada) values(?,'FISCAL_NFSE','{}'::jsonb,0,?,?) on conflict(empresa_id,secao) do update set senha_nfse_cifrada=excluded.senha_nfse_cifrada,atualizado_por=excluded.atualizado_por,atualizado_em=current_timestamp", empresa, usuario, protegida);
        auditarSegredoFiscal(empresa, "FISCAL_NFSE", "senhaPrefeitura", usuario);
        return Map.of("temSenha", true);
    }

    @Transactional(readOnly = true)
    public Map<String,Object> obterCredenciaisNfce() {
        Long empresa = TenantAccess.requireCurrent();
        return banco.query("select csc_nfce_cifrada is not null tem_csc, token_nfce_cifrado is not null tem_token from configuracoes_complementares_empresa where empresa_id=? and secao='FISCAL_NFCE'", rs -> rs.next() ? Map.of("temCsc",rs.getBoolean("tem_csc"),"temToken",rs.getBoolean("tem_token")) : Map.of("temCsc",false,"temToken",false), empresa);
    }

    @Transactional
    public Map<String,Object> salvarCredenciaisNfce(String csc, String token) {
        Long empresa = TenantAccess.requireCurrent();
        if (csc == null || csc.isBlank() || csc.length() > 255 || token == null || token.isBlank() || token.length() > 255) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Informe o CSC e o token CLD da NFC-e.");
        Long usuario = usuarioAtualId();
        banco.update("insert into configuracoes_complementares_empresa(empresa_id,secao,dados,versao,atualizado_por,csc_nfce_cifrada,token_nfce_cifrado) values(?,'FISCAL_NFCE','{}'::jsonb,0,?,?,?) on conflict(empresa_id,secao) do update set csc_nfce_cifrada=excluded.csc_nfce_cifrada,token_nfce_cifrado=excluded.token_nfce_cifrado,atualizado_por=excluded.atualizado_por,atualizado_em=current_timestamp", empresa, usuario, cifrar(csc.getBytes(StandardCharsets.UTF_8)), cifrar(token.getBytes(StandardCharsets.UTF_8)));
        auditarSegredoFiscal(empresa, "FISCAL_NFCE", "credenciaisNfce", usuario);
        return Map.of("temCsc",true,"temToken",true);
    }

    private void auditarSegredoFiscal(Long empresa, String secao, String campo, Long usuario) {
        Long registro = banco.queryForObject("select id from configuracoes_complementares_empresa where empresa_id=? and secao=?", Long.class, empresa, secao);
        banco.update("""
                insert into logs_alteracoes (empresa_id,tabela_afetada,registro_id,operacao,campos_alterados,valores_antigos,valores_novos,usuario_responsavel,contexto_operacao,dados_adicionais,data_alteracao)
                values (?,'configuracoes_complementares_empresa',?,'UPDATE',?,'{}','{\"atualizado\":true}',?,'ADM-013',?::jsonb,current_timestamp)
                """, empresa, registro, "[\"" + campo + "\"]", usuario, "{\"secao\":\"" + secao + "\"}");
    }

    private byte[] chaveProtecao() { try { byte[] bytes=Base64.getDecoder().decode(chaveBase64 == null ? "" : chaveBase64); if(bytes.length!=32) throw new IllegalArgumentException(); return bytes; } catch(Exception falha){ throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"A chave de proteção de dados não está configurada corretamente no servidor"); } }
    private byte[] cifrar(byte[] texto) { try { byte[] iv=new byte[12]; new java.security.SecureRandom().nextBytes(iv); Cipher cifra=Cipher.getInstance(CIPHER); cifra.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(chaveProtecao(),"AES"),new GCMParameterSpec(128,iv)); byte[] cifrado=cifra.doFinal(texto); byte[] saida=new byte[iv.length+cifrado.length]; System.arraycopy(iv,0,saida,0,iv.length);System.arraycopy(cifrado,0,saida,iv.length,cifrado.length); return saida; } catch(org.springframework.web.server.ResponseStatusException falha){throw falha;} catch(Exception falha){throw new IllegalStateException("Falha ao proteger dado sensível",falha);} }
    private byte[] decifrar(byte[] dado) { try { byte[] iv=java.util.Arrays.copyOfRange(dado,0,12); Cipher cifra=Cipher.getInstance(CIPHER); cifra.init(Cipher.DECRYPT_MODE,new SecretKeySpec(chaveProtecao(),"AES"),new GCMParameterSpec(128,iv)); return cifra.doFinal(java.util.Arrays.copyOfRange(dado,12,dado.length)); } catch(org.springframework.web.server.ResponseStatusException falha){throw falha;} catch(Exception falha){throw new IllegalStateException("Falha ao acessar configuração protegida",falha);} }
    private void validarEscopo(String escopo){if(!List.of("MATRIZ","CRM").contains(escopo)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Escopo de e-mail inválido");}
    private void validarSmtp(RequisicaoSmtp req){if(req==null||req.email()==null||!req.email().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")||req.servidorSmtp()==null||req.servidorSmtp().isBlank()||req.portaSmtp()<1||req.portaSmtp()>65535) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Confira e-mail, servidor SMTP e porta");}
    private Long usuarioAtualId(){var auth=SecurityContextHolder.getContext().getAuthentication(); if(auth==null||!auth.isAuthenticated()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Autenticação necessária"); return banco.query("select id from usuarios where lower(email)=lower(?) and empresa_id=? and ativo=true and bloqueado=false",rs->rs.next()?rs.getLong(1):null,auth.getName(),TenantAccess.requireCurrent());}
    public record RequisicaoSmtp(String email,String senha,String servidorSmtp,int portaSmtp,boolean usarSsl,boolean usarTls,String assinatura){}
    private record SmtpCompleto(String email,byte[] senha,String servidor,int porta,boolean ssl,boolean tls,String assinatura){ SmtpCompleto(String email,byte[] senha,String servidor,int porta,boolean ssl,boolean tls){this(email,senha,servidor,porta,ssl,tls,null);} }
    private record DadosSmtp(String email,String servidor,int porta,boolean ssl,boolean tls){}
}
