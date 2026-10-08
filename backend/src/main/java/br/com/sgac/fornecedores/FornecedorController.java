package br.com.sgac.fornecedores;

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
@RequestMapping("/api/fornecedores")
public class FornecedorController {
    private final FornecedorService service;

    public FornecedorController(FornecedorService service) { this.service = service; }

    @GetMapping
    public List<FornecedorResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public FornecedorResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    public ResponseEntity<FornecedorResponse> criar(@Valid @RequestBody FornecedorRequest request) {
        FornecedorResponse criado = service.criar(request);
        return ResponseEntity.created(URI.create("/api/fornecedores/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public FornecedorResponse atualizar(@PathVariable Long id,
                                        @Valid @RequestBody FornecedorRequest request) {
        return service.atualizar(id, request);
    }

    @PatchMapping("/{id}/status")
    public FornecedorResponse alterarStatus(@PathVariable Long id,
                                             @Valid @RequestBody FornecedorStatusRequest request) {
        return service.alterarStatus(id, request);
    }
}
