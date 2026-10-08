package br.com.sgac.solicitacoes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findAllByOrderByCriadaEmDescIdDesc();
}
