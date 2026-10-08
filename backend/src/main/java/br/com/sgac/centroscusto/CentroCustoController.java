package br.com.sgac.centroscusto;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/centros-custo")
public class CentroCustoController {
    private final CentroCustoService service;

    public CentroCustoController(CentroCustoService service) { this.service = service; }

    @GetMapping
    public List<CentroCustoResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public CentroCustoResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    public ResponseEntity<CentroCustoResponse> criar(@Valid @RequestBody CentroCustoRequest request) {
        CentroCustoResponse criado = service.criar(request);
        return ResponseEntity.created(URI.create("/api/centros-custo/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public CentroCustoResponse atualizar(@PathVariable Long id,
                                          @Valid @RequestBody CentroCustoRequest request) {
        return service.atualizar(id, request);
    }

    @PatchMapping("/{id}/status")
    public CentroCustoResponse alterarStatus(@PathVariable Long id,
                                               @Valid @RequestBody CentroCustoStatusRequest request) {
        return service.alterarStatus(id, request);
    }
}
