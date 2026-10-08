package br.com.sgac.departamentos;

import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DepartamentoService {
    private final DepartamentoRepository repository;

    public DepartamentoService(DepartamentoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public DepartamentoResponse criar(DepartamentoRequest request) {
        String codigo = normalizarCodigo(request.codigo());
        String nome = request.nome().trim();
        if (repository.existsByCodigoIgnoreCase(codigo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Ja existe um departamento com esse codigo");
        }
        return DepartamentoResponse.from(repository.save(new Departamento(codigo, nome)));
    }

    @Transactional(readOnly = true)
    public List<DepartamentoResponse> listar() {
        return repository.findAllByOrderByNomeAsc().stream()
            .map(DepartamentoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public DepartamentoResponse buscarPorId(Long id) {
        return DepartamentoResponse.from(encontrarDepartamento(id));
    }

    @Transactional
    public DepartamentoResponse atualizar(Long id, DepartamentoRequest request) {
        Departamento departamento = encontrarDepartamento(id);
        String codigo = normalizarCodigo(request.codigo());
        String nome = request.nome().trim();
        if (repository.existsByCodigoIgnoreCaseAndIdNot(codigo, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Ja existe outro departamento com esse codigo");
        }
        departamento.atualizar(codigo, nome);
        return DepartamentoResponse.from(departamento);
    }

    @Transactional
    public DepartamentoResponse alterarStatus(Long id, DepartamentoStatusRequest request) {
        Departamento departamento = encontrarDepartamento(id);
        departamento.alterarStatus(request.ativo());
        return DepartamentoResponse.from(departamento);
    }

    private Departamento encontrarDepartamento(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento nao encontrado"));
    }

    private String normalizarCodigo(String codigo) {
        return codigo.trim().toUpperCase(Locale.ROOT);
    }
}
