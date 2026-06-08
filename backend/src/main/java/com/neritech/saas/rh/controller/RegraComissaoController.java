package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.RegraComissaoRequest;
import com.neritech.saas.rh.dto.RegraComissaoResponse;
import com.neritech.saas.rh.service.RegraComissaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/rh/regras-comissao")
public class RegraComissaoController {

    private final RegraComissaoService service;

    public RegraComissaoController(RegraComissaoService service) {
        this.service = service;
    }

    @GetMapping("/funcionario/{funcionarioId}")
    public Page<RegraComissaoResponse> listarPorFuncionario(
            @PathVariable Long funcionarioId,
            Pageable pageable) {
        return service.listarPorFuncionario(funcionarioId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegraComissaoResponse criar(@RequestBody RegraComissaoRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    public RegraComissaoResponse atualizar(@PathVariable Long id, @RequestBody RegraComissaoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
