package br.com.sgac.departamentos;

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
@RequestMapping("/api/departamentos")
public class DepartamentoController {
    private final DepartamentoService service;

    public DepartamentoController(DepartamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<DepartamentoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public DepartamentoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<DepartamentoResponse> criar(@Valid @RequestBody DepartamentoRequest request) {
        DepartamentoResponse resposta = service.criar(request);
        return ResponseEntity.created(URI.create("/api/departamentos/" + resposta.id())).body(resposta);
    }

    @PutMapping("/{id}")
    public DepartamentoResponse atualizar(@PathVariable Long id,
                                           @Valid @RequestBody DepartamentoRequest request) {
        return service.atualizar(id, request);
    }

    @PatchMapping("/{id}/status")
    public DepartamentoResponse alterarStatus(@PathVariable Long id,
                                               @Valid @RequestBody DepartamentoStatusRequest request) {
        return service.alterarStatus(id, request);
    }
}
