package br.com.sgac.fornecedores;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    boolean existsByCnpj(String cnpj);
    boolean existsByCnpjAndIdNot(String cnpj, Long id);
    List<Fornecedor> findAllByOrderByRazaoSocialAsc();
}
