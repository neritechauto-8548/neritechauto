package com.neritech.saas.veiculo.controller;

import com.neritech.saas.veiculo.service.FipeDataLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Endpoints administrativos para operações manuais de carga de dados.
 */
@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    private final FipeDataLoader fipeDataLoader;

    public AdminController(FipeDataLoader fipeDataLoader) {
        this.fipeDataLoader = fipeDataLoader;
    }

    /**
     * Aciona a carga de dados da tabela FIPE manualmente.
     * Carrega marcas, modelos, anos/modelo e tipos de combustível.
     */
    @PostMapping("/fipe/carregar")
    public ResponseEntity<Map<String, String>> carregarFipe() {
        boolean iniciou = fipeDataLoader.iniciarCarga();
        if (iniciou) {
            return ResponseEntity.accepted().body(Map.of(
                "status", "iniciado",
                "mensagem", "Carga FIPE iniciada em background. Acompanhe o progresso nos logs do servidor."
            ));
        } else {
            return ResponseEntity.status(409).body(Map.of(
                "status", "em_andamento",
                "mensagem", "Uma carga FIPE já está em andamento. Aguarde a conclusão."
            ));
        }
    }
}
