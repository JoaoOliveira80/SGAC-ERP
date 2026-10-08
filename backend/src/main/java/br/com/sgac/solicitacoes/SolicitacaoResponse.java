package br.com.sgac.solicitacoes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SolicitacaoResponse(
    Long id, SolicitacaoStatus status, String solicitante, String justificativa,
    Long centroCustoId, String centroCusto,
    Long fornecedorId, String fornecedor,
    BigDecimal valorTotal, LocalDateTime criadaEm,
    List<SolicitacaoItemResponse> itens
) {
    public static SolicitacaoResponse from(Solicitacao s) {
        return new SolicitacaoResponse(s.getId(), s.getStatus(), s.getSolicitante(),
            s.getJustificativa(), s.getCentroCusto().getId(), s.getCentroCusto().getNome(),
            s.getFornecedor().getId(), s.getFornecedor().getRazaoSocial(), s.getValorTotal(),
            s.getCriadaEm(), s.getItens().stream().map(SolicitacaoItemResponse::from).toList());
    }
}
