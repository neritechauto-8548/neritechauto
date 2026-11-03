package com.neritech.saas.configuracoes;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/configuracoes-seguras")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('ADM_EMPRESA')")
public class ConfiguracoesSegurasController {
    private final ConfiguracoesSegurasService servico;

    @GetMapping("/smtp-perfil")
    @PreAuthorize("isAuthenticated()")
    public Map<String,Object> obterSmtpPerfil() { return servico.obterSmtpPerfil(); }

    @PutMapping("/smtp-perfil")
    @PreAuthorize("isAuthenticated()")
    public Map<String,Object> salvarSmtpPerfil(@RequestBody ConfiguracoesSegurasService.RequisicaoSmtp requisicao) { return servico.salvarSmtpPerfil(requisicao); }

    @PostMapping("/smtp-perfil/verificar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> verificarSmtpPerfil(@RequestBody ConfiguracoesSegurasService.RequisicaoSmtp requisicao) { servico.verificarSmtpPerfil(requisicao); return ResponseEntity.noContent().build(); }

    @GetMapping("/smtp/{escopo}")
    public Map<String,Object> obterSmtp(@PathVariable String escopo) { return servico.obterSmtp(escopo.toUpperCase()); }

    @PutMapping("/smtp/{escopo}")
    public Map<String,Object> salvarSmtp(@PathVariable String escopo, @RequestBody ConfiguracoesSegurasService.RequisicaoSmtp requisicao) { return servico.salvarSmtp(escopo.toUpperCase(), requisicao); }

    @PostMapping("/smtp/{escopo}/verificar")
    public ResponseEntity<Void> verificarSmtp(@PathVariable String escopo, @RequestBody(required=false) ConfiguracoesSegurasService.RequisicaoSmtp requisicao) { servico.verificarSmtp(escopo.toUpperCase(), requisicao); return ResponseEntity.noContent().build(); }

    @GetMapping("/certificados")
    public List<Map<String,Object>> listarCertificados() { return servico.listarCertificados(); }

    @GetMapping("/fiscal/nfse/senha-prefeitura")
    public Map<String,Object> obterSenhaPrefeituraNfse() { return servico.obterSenhaPrefeituraNfse(); }

    @PutMapping("/fiscal/nfse/senha-prefeitura")
    public Map<String,Object> salvarSenhaPrefeituraNfse(@RequestBody Map<String,String> requisicao) { return servico.salvarSenhaPrefeituraNfse(requisicao == null ? null : requisicao.get("senha")); }

    @GetMapping("/fiscal/nfce/credenciais")
    public Map<String,Object> obterCredenciaisNfce() { return servico.obterCredenciaisNfce(); }

    @PutMapping("/fiscal/nfce/credenciais")
    public Map<String,Object> salvarCredenciaisNfce(@RequestBody Map<String,String> requisicao) { return servico.salvarCredenciaisNfce(requisicao == null ? null : requisicao.get("csc"), requisicao == null ? null : requisicao.get("token")); }

    @PostMapping(value="/certificados", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> criarCertificado(@RequestParam("nomeArquivo") @NotBlank String nomeArquivo, @RequestParam(value="email",required=false) String email, @RequestParam("senha") @NotBlank String senha, @RequestParam("arquivo") MultipartFile arquivo) throws Exception {
        String nome=nomeArquivo.trim();
        if (arquivo.getOriginalFilename()!=null && !arquivo.getOriginalFilename().toLowerCase().matches(".*\\.(pfx|p12)$")) return ResponseEntity.unprocessableEntity().build();
        servico.criarCertificado(nome,email,senha,arquivo.getBytes()); return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
