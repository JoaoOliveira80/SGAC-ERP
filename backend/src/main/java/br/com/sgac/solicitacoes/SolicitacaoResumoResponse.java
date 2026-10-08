package br.com.sgac.solicitacoes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SolicitacaoResumoResponse(
    Long id, SolicitacaoStatus status, String solicitante,
    Long centroCustoId, String centroCusto,
    Long fornecedorId, String fornecedor,
    BigDecimal valorTotal, LocalDateTime criadaEm
) {
    public static SolicitacaoResumoResponse from(Solicitacao s) {
        return new SolicitacaoResumoResponse(s.getId(), s.getStatus(), s.getSolicitante(),
            s.getCentroCusto().getId(), s.getCentroCusto().getNome(),
            s.getFornecedor().getId(), s.getFornecedor().getRazaoSocial(),
            s.getValorTotal(), s.getCriadaEm());
    }
}
