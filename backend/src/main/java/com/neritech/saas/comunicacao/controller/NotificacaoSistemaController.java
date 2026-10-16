package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.NotificacaoSistemaRequest;
import com.neritech.saas.comunicacao.dto.NotificacaoSistemaResponse;
import com.neritech.saas.comunicacao.service.NotificacaoSistemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comunicacao/notificacoes")
@RequiredArgsConstructor
@Tag(name = "NotificaÃ§Ãµes do Sistema", description = "Gerenciamento de notificaÃ§Ãµes internas para usuÃ¡rios")
public class NotificacaoSistemaController {

    private final NotificacaoSistemaService service;

    @GetMapping("/me")
    @Operation(summary = "Minhas notificações", description = "Somente notificações do usuário e empresa autenticados")
    public ResponseEntity<Page<NotificacaoSistemaResponse>> findMine(Pageable pageable) {
        return ResponseEntity.ok(service.findMine(pageable));
    }

    @PatchMapping("/{id}/ler")
    @Operation(summary = "Marcar minha notificação como lida")
    public ResponseEntity<NotificacaoSistemaResponse> markMineAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(service.markMineAsRead(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar notificaÃ§Ãµes", description = "Retorna uma lista paginada de notificaÃ§Ãµes")
    public ResponseEntity<Page<NotificacaoSistemaResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar notificaÃ§Ãµes por usuÃ¡rio", description = "Retorna as notificaÃ§Ãµes de um usuÃ¡rio especÃ­fico")
    public ResponseEntity<Page<NotificacaoSistemaResponse>> findByUsuario(
            @PathVariable Long usuarioId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByUsuarioDestinatario(usuarioId, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar notificaÃ§Ã£o por ID", description = "Retorna os detalhes de uma notificaÃ§Ã£o especÃ­fica")
    public ResponseEntity<NotificacaoSistemaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar notificaÃ§Ã£o", description = "Cria uma nova notificaÃ§Ã£o para um usuÃ¡rio")
    public ResponseEntity<NotificacaoSistemaResponse> create(@RequestBody @Valid NotificacaoSistemaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar notificaÃ§Ã£o", description = "Atualiza os dados de uma notificaÃ§Ã£o (ex: marcar como lida)")
    public ResponseEntity<NotificacaoSistemaResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid NotificacaoSistemaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Excluir notificaÃ§Ã£o", description = "Remove uma notificaÃ§Ã£o do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
