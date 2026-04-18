package com.neritech.saas;

import com.neritech.saas.gestaoUsuarios.domain.*;
import com.neritech.saas.gestaoUsuarios.dto.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Builder de dados de teste seguindo o padrão Test Data Builder.
 * Facilita a criação de objetos para testes com valores padrão sensatos.
 */
public class TestDataBuilder {

    // ============================================
    // BUILDERS DE DOMAIN
    // ============================================

    public static UsuarioBuilder umUsuario() {
        return new UsuarioBuilder();
    }

    public static class UsuarioBuilder {
        private Long id = 1L;
        private Long empresaId = 1L;
        private String nomeCompleto = "João da Silva";
        private String email = "joao.silva@test.com";
        private String senha = "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYIw8fRXfRu"; // senha123
        private Boolean ativo = true;
        private Boolean bloqueado = false;
        private Boolean deveTrocarSenha = false;
        private LocalDateTime ultimoAcesso = LocalDateTime.now();
        private Set<Funcao> funcoes = new HashSet<>();

        public UsuarioBuilder comId(Long id) {
            this.id = id;
            return this;
        }

        public UsuarioBuilder comEmpresaId(Long empresaId) {
            this.empresaId = empresaId;
            return this;
        }

        public UsuarioBuilder comNomeCompleto(String nomeCompleto) {
            this.nomeCompleto = nomeCompleto;
            return this;
        }

        public UsuarioBuilder comEmail(String email) {
            this.email = email;
            return this;
        }

        public UsuarioBuilder comSenha(String senha) {
            this.senha = senha;
            return this;
        }

        public UsuarioBuilder inativo() {
            this.ativo = false;
            return this;
        }

        public UsuarioBuilder bloqueado() {
            this.bloqueado = true;
            return this;
        }

        public UsuarioBuilder deveTrocarSenha() {
            this.deveTrocarSenha = true;
            return this;
        }

        public UsuarioBuilder comFuncao(Funcao funcao) {
            this.funcoes.add(funcao);
            return this;
        }

        public Usuario build() {
            Usuario usuario = Usuario.builder()
                    .id(id)
                    .nomeCompleto(nomeCompleto)
                    .email(email)
                    .senha(senha)
                    .ativo(ativo)
                    .bloqueado(bloqueado)
                    .deveTrocarSenha(deveTrocarSenha)
                    .ultimoAcesso(ultimoAcesso)
                    .funcoes(funcoes)
                    .build();
            usuario.setEmpresaId(empresaId);
            return usuario;
        }
    }

    public static FuncaoBuilder umaFuncao() {
        return new FuncaoBuilder();
    }

    public static class FuncaoBuilder {
        private Long id = 1L;
        private Long empresaId = 1L;
        private String nome = "ADMIN";
        private String descricao = "Administrador do Sistema";
        private Boolean sistema = true;
        private Boolean ativo = true;
        private Set<Permissao> permissoes = new HashSet<>();

        public FuncaoBuilder comId(Long id) {
            this.id = id;
            return this;
        }

        public FuncaoBuilder comEmpresaId(Long empresaId) {
            this.empresaId = empresaId;
            return this;
        }

        public FuncaoBuilder comNome(String nome) {
            this.nome = nome;
            return this;
        }

        public FuncaoBuilder comDescricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public FuncaoBuilder naoSistema() {
            this.sistema = false;
            return this;
        }

        public FuncaoBuilder comPermissao(Permissao permissao) {
            this.permissoes.add(permissao);
            return this;
        }

        public Funcao build() {
            Funcao funcao = Funcao.builder()
                    .id(id)
                    .nome(nome)
                    .descricao(descricao)
                    .sistema(sistema)
                    .ativo(ativo)
                    .permissoes(permissoes)
                    .build();
            funcao.setEmpresaId(empresaId);
            return funcao;
        }
    }

    public static PermissaoBuilder umaPermissao() {
        return new PermissaoBuilder();
    }

    public static class PermissaoBuilder {
        private Long id = 1L;
        private Long empresaId = 1L;
        private String nome = "USUARIO.READ";
        private String descricao = "Visualizar usuários";
        private String modulo = "gestaoUsuarios";

        public PermissaoBuilder comId(Long id) {
            this.id = id;
            return this;
        }

        public PermissaoBuilder comEmpresaId(Long empresaId) {
            this.empresaId = empresaId;
            return this;
        }

        public PermissaoBuilder comNome(String nome) {
            this.nome = nome;
            return this;
        }

        public PermissaoBuilder comDescricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public PermissaoBuilder comModulo(String modulo) {
            this.modulo = modulo;
            return this;
        }

        public Permissao build() {
            Permissao permissao = Permissao.builder()
                    .id(id)
                    .nome(nome)
                    .descricao(descricao)
                    .modulo(modulo)
                    .build();
            permissao.setEmpresaId(empresaId);
            return permissao;
        }
    }

    // ============================================
    // BUILDERS DE DTO
    // ============================================

    public static LoginRequestBuilder umLoginRequest() {
        return new LoginRequestBuilder();
    }

    public static class LoginRequestBuilder {
        private String email = "joao.silva@test.com";
        private String senha = "senha123";
        private String ipAddress = "192.168.1.1";
        private String userAgent = "Mozilla/5.0";

        public LoginRequestBuilder comEmail(String email) {
            this.email = email;
            return this;
        }

        public LoginRequestBuilder comSenha(String senha) {
            this.senha = senha;
            return this;
        }

        public LoginRequestBuilder comIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public LoginRequestBuilder comUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public LoginRequest build() {
            LoginRequest request = new LoginRequest();
            request.setEmail(email);
            request.setSenha(senha);
            request.setIpAddress(ipAddress);
            request.setUserAgent(userAgent);
            return request;
        }
    }

    public static UsuarioRequestBuilder umUsuarioRequest() {
        return new UsuarioRequestBuilder();
    }

    public static class UsuarioRequestBuilder {
        private String nomeCompleto = "Maria Santos";
        private String email = "maria.santos@test.com";
        private String senha = "senha123";
        private boolean ativo = true;
        private boolean bloqueado = false;
        private String telefone = "(11) 98765-4321";
        private String cargo = "Analista";
        private String departamento = "TI";

        public UsuarioRequestBuilder comNomeCompleto(String nomeCompleto) {
            this.nomeCompleto = nomeCompleto;
            return this;
        }

        public UsuarioRequestBuilder comEmail(String email) {
            this.email = email;
            return this;
        }

        public UsuarioRequestBuilder comSenha(String senha) {
            this.senha = senha;
            return this;
        }

        public UsuarioRequestBuilder inativo() {
            this.ativo = false;
            return this;
        }

        public UsuarioRequestBuilder bloqueado() {
            this.bloqueado = true;
            return this;
        }

        public UsuarioRequest build() {
            UsuarioRequest request = new UsuarioRequest();
            request.setNomeCompleto(nomeCompleto);
            request.setEmail(email);
            request.setSenha(senha);
            request.setAtivo(ativo);
            request.setBloqueado(bloqueado);
            request.setTelefone(telefone);
            request.setCargo(cargo);
            request.setDepartamento(departamento);
            return request;
        }
    }

    public static RefreshTokenRequestBuilder umRefreshTokenRequest() {
        return new RefreshTokenRequestBuilder();
    }

    public static class RefreshTokenRequestBuilder {
        private String refreshToken = "valid.refresh.token";

        public RefreshTokenRequestBuilder comRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public RefreshTokenRequest build() {
            RefreshTokenRequest request = new RefreshTokenRequest();
            request.setRefreshToken(refreshToken);
            return request;
        }
    }
}
