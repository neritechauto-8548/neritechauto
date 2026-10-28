package com.neritech.saas.gestaoUsuarios.controller;

import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import com.neritech.saas.gestaoUsuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gestão de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Listar usuários", description = "Retorna todos os usuários da empresa")
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/pagina")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('GERAL_USUARIO')")
    public ResponseEntity<org.springframework.data.domain.Page<UsuarioResponse>> search(
            @RequestParam(required = false) String q, @RequestParam(required = false) Boolean ativo,
            @org.springframework.data.web.PageableDefault(size = 20, sort = "nomeCompleto")
            org.springframework.data.domain.Pageable pageable) {
        return ResponseEntity.ok(usuarioService.search(q, ativo, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Buscar usuário", description = "Busca um usuário pelo ID")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @GetMapping("/{id}/historico")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public ResponseEntity<org.springframework.data.domain.Page<com.neritech.saas.gestaoUsuarios.dto.UsuarioAlteracaoResponse>> history(
            @PathVariable Long id, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(usuarioService.history(id, page, size));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário")
    public ResponseEntity<UsuarioResponse> create(@RequestBody @Valid UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.create(request));
    }

    @PostMapping("/convites")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    @Operation(summary = "Convidar usuário", description = "Cria a identidade e envia link de uso único")
    public ResponseEntity<UsuarioResponse> invite(@RequestBody @Valid UsuarioRequest request) {
        return ResponseEntity.accepted().body(usuarioService.invite(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário")
    public ResponseEntity<UsuarioResponse> update(@PathVariable Long id, @RequestBody @Valid UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    @Operation(summary = "Inativar usuário", description = "Inativa um usuário (soft delete)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Obter usuário atual", description = "Retorna os dados do usuário autenticado")
    public ResponseEntity<UsuarioResponse> getCurrentUser() {
        return ResponseEntity.ok(usuarioService.getCurrentUser());
    }

    @GetMapping("/menu")
    @Operation(summary = "Obter menu do usuário", description = "Retorna o menu baseado nas permissões do usuário")
    public ResponseEntity<MenuResponse> getMenu() {
        // Por enquanto, retorna um menu vazio. Pode ser implementado depois com base nas permissões
        return ResponseEntity.ok(new MenuResponse(Collections.emptyList()));
    }

    // Classe interna para resposta do menu
    public static class MenuResponse {
        private List<Object> menu;

        public MenuResponse(List<Object> menu) {
            this.menu = menu;
        }

        public List<Object> getMenu() {
            return menu;
        }

        public void setMenu(List<Object> menu) {
            this.menu = menu;
        }
    }
}
