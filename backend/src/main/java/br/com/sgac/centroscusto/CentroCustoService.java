package br.com.sgac.centroscusto;

import br.com.sgac.departamentos.Departamento;
import br.com.sgac.departamentos.DepartamentoRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CentroCustoService {
    private final CentroCustoRepository repository;
    private final DepartamentoRepository departamentoRepository;

    public CentroCustoService(CentroCustoRepository repository,
                              DepartamentoRepository departamentoRepository) {
        this.repository = repository;
        this.departamentoRepository = departamentoRepository;
    }

    @Transactional(readOnly = true)
    public List<CentroCustoResponse> listar() {
        return repository.findAllByOrderByNomeAsc()
            .stream().map(CentroCustoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CentroCustoResponse buscar(Long id) { return CentroCustoResponse.from(encontrar(id)); }

    @Transactional
    public CentroCustoResponse criar(CentroCustoRequest request) {
        String codigo = normalizarCodigo(request.codigo());
        if (repository.existsByCodigoIgnoreCase(codigo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Centro de custo com codigo ja cadastrado");
        }
        CentroCusto centroCusto = new CentroCusto(
            codigo, request.nome().trim(), encontrarDepartamentoAtivo(request.departamentoId())
        );
        return CentroCustoResponse.from(repository.save(centroCusto));
    }

    @Transactional
    public CentroCustoResponse atualizar(Long id, CentroCustoRequest request) {
        CentroCusto centroCusto = encontrar(id);
        String codigo = normalizarCodigo(request.codigo());
        if (repository.existsByCodigoIgnoreCaseAndIdNot(codigo, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Codigo pertence a outro centro de custo");
        }
        centroCusto.atualizar(
            codigo, request.nome().trim(), encontrarDepartamentoAtivo(request.departamentoId())
        );
        return CentroCustoResponse.from(centroCusto);
    }

    @Transactional
    public CentroCustoResponse alterarStatus(Long id, CentroCustoStatusRequest request) {
        CentroCusto centroCusto = encontrar(id);
        if (request.ativo()) {
            // Nao reativar centro vinculado a departamento inativo.
            encontrarDepartamentoAtivo(centroCusto.getDepartamento().getId());
        }
        centroCusto.alterarStatus(request.ativo());
        return CentroCustoResponse.from(centroCusto);
    }

    private CentroCusto encontrar(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Centro de custo nao encontrado"));
    }

    private Departamento encontrarDepartamentoAtivo(Long id) {
        Departamento departamento = departamentoRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento nao encontrado"));
        if (!departamento.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Departamento esta inativo");
        }
        return departamento;
    }

    private String normalizarCodigo(String codigo) {
        return codigo.trim().toUpperCase(Locale.ROOT);
    }
}
