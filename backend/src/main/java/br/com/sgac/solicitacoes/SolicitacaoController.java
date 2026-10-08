package br.com.sgac.solicitacoes;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {
    private final SolicitacaoService service;

    public SolicitacaoController(SolicitacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<SolicitacaoResumoResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public SolicitacaoResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @GetMapping("/{id}/historico")
    public List<SolicitacaoHistoricoResponse> historico(@PathVariable Long id) {
        return service.historico(id);
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponse> criar(@Valid @RequestBody SolicitacaoCriarRequest request) {
        SolicitacaoResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/api/solicitacoes/" + response.id())).body(response);
    }

    @PostMapping("/{id}/enviar")
    public SolicitacaoResponse enviar(@PathVariable Long id) { return service.enviar(id); }

    @PostMapping("/{id}/aprovar")
    public SolicitacaoResponse aprovar(@PathVariable Long id,
                                      @Valid @RequestBody SolicitacaoDecisaoRequest request) {
        return service.aprovar(id, request);
    }

    @PostMapping("/{id}/rejeitar")
    public SolicitacaoResponse rejeitar(@PathVariable Long id,
                                       @Valid @RequestBody SolicitacaoRejeicaoRequest request) {
        return service.rejeitar(id, request);
    }
}
