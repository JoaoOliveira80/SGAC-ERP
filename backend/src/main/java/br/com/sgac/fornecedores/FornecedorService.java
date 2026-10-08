package br.com.sgac.fornecedores;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FornecedorService {
    private final FornecedorRepository repository;

    public FornecedorService(FornecedorRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<FornecedorResponse> listar() {
        return repository.findAllByOrderByRazaoSocialAsc()
            .stream().map(FornecedorResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public FornecedorResponse buscar(Long id) { return FornecedorResponse.from(encontrar(id)); }

    @Transactional
    public FornecedorResponse criar(FornecedorRequest request) {
        String cnpj = request.cnpj().trim();
        if (repository.existsByCnpj(cnpj)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CNPJ ja cadastrado");
        }
        return FornecedorResponse.from(repository.save(new Fornecedor(
            cnpj, request.razaoSocial().trim(), normalizarEmail(request.email())
        )));
    }

    @Transactional
    public FornecedorResponse atualizar(Long id, FornecedorRequest request) {
        Fornecedor fornecedor = encontrar(id);
        String cnpj = request.cnpj().trim();
        if (repository.existsByCnpjAndIdNot(cnpj, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CNPJ pertence a outro fornecedor");
        }
        fornecedor.atualizar(cnpj, request.razaoSocial().trim(), normalizarEmail(request.email()));
        return FornecedorResponse.from(fornecedor);
    }

    @Transactional
    public FornecedorResponse alterarStatus(Long id, FornecedorStatusRequest request) {
        Fornecedor fornecedor = encontrar(id);
        fornecedor.alterarStatus(request.ativo());
        return FornecedorResponse.from(fornecedor);
    }

    private Fornecedor encontrar(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor nao encontrado"));
    }

    private String normalizarEmail(String email) {
        return email == null || email.isBlank() ? null : email.trim();
    }
}
