package br.com.sgac.solicitacoes;

import java.math.BigDecimal;

public record SolicitacaoItemResponse(
    Long id, String descricao, int quantidade,
    BigDecimal valorUnitario, BigDecimal subtotal
) {
    public static SolicitacaoItemResponse from(SolicitacaoItem item) {
        return new SolicitacaoItemResponse(item.getId(), item.getDescricao(),
            item.getQuantidade(), item.getValorUnitario(), item.getSubtotal());
    }
}
