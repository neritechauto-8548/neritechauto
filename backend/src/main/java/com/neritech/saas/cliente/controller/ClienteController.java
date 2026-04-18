package com.neritech.saas.cliente.controller;

import com.neritech.saas.common.exception.ErrorResponse;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.cliente.dto.ClienteRequest;
import com.neritech.saas.cliente.dto.ClienteResponse;
import com.neritech.saas.cliente.mapper.ClienteMapper;
import com.neritech.saas.cliente.service.ClienteService;
import com.neritech.saas.common.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/clientes")
@Tag(name = "Clientes", description = "GestÃ£o completa de clientes (Pessoas FÃ­sicas e JurÃ­dicas)")
public class ClienteController {

        private final ClienteService service;

        public ClienteController(ClienteService service) {
                this.service = service;
        }

        /**
         * Busca um cliente pelo seu identificador Ãºnico.
         *
         * @param id Identificador Ãºnico do cliente
         * @return Dados detalhados do cliente encontrado
         * @throws ResourceNotFoundException se o cliente nÃ£o for encontrado
         */
        @GetMapping("/{id}")
        @Operation(summary = "Buscar cliente por ID", description = "Retorna os dados detalhados de um cliente especÃ­fico baseado no ID fornecido.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Cliente nÃ£o encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public ResponseEntity<ClienteResponse> getById(
                        @Parameter(description = "ID do cliente a ser buscado", required = true, example = "1") @PathVariable Long id) {
                Cliente c = service.findById(id);
                return ResponseEntity.ok(ClienteMapper.toResponse(c));
        }

        /**
         * Lista clientes com suporte a filtros e paginaÃ§Ã£o.
         *
         * @param nomeCompleto Filtro por nome completo (opcional)
         * @param razaoSocial  Filtro por razÃ£o social (opcional)
         * @param cpf          Filtro por CPF (opcional)
         * @param cnpj         Filtro por CNPJ (opcional)
         * @param tipoCliente  Filtro por tipo de cliente (PF/PJ) (opcional)
         * @param status       Filtro por status do cliente (opcional)
         * @param pageable     ParÃ¢metros de paginaÃ§Ã£o (page, size, sort)
         * @return PÃ¡gina com lista de clientes encontrados
         */
        @GetMapping
        @Operation(summary = "Listar clientes", description = "Retorna uma lista paginada de clientes que correspondem aos filtros fornecidos.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        public Page<ClienteResponse> search(
                        @Parameter(description = "Nome completo para filtro") @RequestParam(required = false) String nomeCompleto,
                        @Parameter(description = "RazÃ£o social para filtro") @RequestParam(required = false) String razaoSocial,
                        @Parameter(description = "CPF para filtro") @RequestParam(required = false) String cpf,
                        @Parameter(description = "CNPJ para filtro") @RequestParam(required = false) String cnpj,
                        @Parameter(description = "Tipo de cliente para filtro") @RequestParam(required = false) TipoCliente tipoCliente,
                        @Parameter(description = "Status do cliente para filtro") @RequestParam(required = false) StatusCliente status,
                        @org.springframework.data.web.PageableDefault(size = 5, sort = "nomeCompleto", direction = org.springframework.data.domain.Sort.Direction.ASC) Pageable pageable) {

                return service.search(nomeCompleto, razaoSocial, cpf, cnpj, tipoCliente, status, pageable)
                                .map(ClienteMapper::toResponse);
        }

        /**
         * Cria um novo cliente no sistema.
         *
         * @param request Dados do novo cliente
         * @return Dados do cliente criado com ID gerado
         * @throws BusinessException se houver violaÃ§Ã£o de regras de negÃ³cio (ex: CPF
         *                           duplicado)
         */
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(summary = "Criar novo cliente", description = "Cadastra um novo cliente no sistema. Valida duplicidade de CPF/CNPJ.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Dados invÃ¡lidos fornecidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "409", description = "Conflito (ex: CPF jÃ¡ cadastrado)", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public ResponseEntity<ClienteResponse> create(
                        @Parameter(description = "Dados do cliente a ser criado", required = true) @Valid @RequestBody ClienteRequest request) {
                Cliente toCreate = ClienteMapper.toEntity(request);
                Cliente saved = service.create(toCreate);
                return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toResponse(saved));
        }

        /**
         * Atualiza os dados de um cliente existente.
         *
         * @param id      Identificador do cliente a ser atualizado
         * @param request Novos dados do cliente
         * @return Dados atualizados do cliente
         * @throws ResourceNotFoundException se o cliente nÃ£o for encontrado
         */
        @PutMapping("/{id}")
        @Operation(summary = "Atualizar cliente", description = "Atualiza os dados cadastrais de um cliente existente.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Cliente nÃ£o encontrado"),
                        @ApiResponse(responseCode = "400", description = "Dados invÃ¡lidos")
        })
        public ResponseEntity<ClienteResponse> update(
                        @Parameter(description = "ID do cliente", required = true) @PathVariable Long id,
                        @Parameter(description = "Novos dados do cliente", required = true) @Valid @RequestBody ClienteRequest request) {
                Cliente saved = service.update(id, request);
                return ResponseEntity.ok(ClienteMapper.toResponse(saved));
        }

        /**
         * Remove (ou inativa) um cliente do sistema.
         *
         * @param id Identificador do cliente a ser removido
         * @throws ResourceNotFoundException se o cliente nÃ£o for encontrado
         */
        @DeleteMapping("/{id}")
        @Operation(summary = "Excluir cliente", description = "Remove um cliente do sistema pelo ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Cliente nÃ£o encontrado"),
                        @ApiResponse(responseCode = "409", description = "NÃ£o Ã© possÃ­vel remover cliente com vÃ­nculos")
        })
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public ResponseEntity<Void> delete(
                        @Parameter(description = "ID do cliente", required = true) @PathVariable Long id) {
                service.delete(id);
                return ResponseEntity.noContent().build();
        }
}
