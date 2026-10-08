package br.com.sgac.solicitacoes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitacaoHistoricoRepository extends JpaRepository<SolicitacaoHistorico, Long> {
    List<SolicitacaoHistorico> findBySolicitacaoIdOrderByRegistradoEmAscIdAsc(Long solicitacaoId);
}
