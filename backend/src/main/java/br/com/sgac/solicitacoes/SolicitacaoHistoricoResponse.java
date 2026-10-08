package br.com.sgac.solicitacoes;

import java.time.LocalDateTime;

public record SolicitacaoHistoricoResponse(
    Long id, SolicitacaoStatus statusAnterior, SolicitacaoStatus statusNovo,
    String responsavel, String observacao, LocalDateTime registradoEm
) {
    public static SolicitacaoHistoricoResponse from(SolicitacaoHistorico h) {
        return new SolicitacaoHistoricoResponse(h.getId(), h.getStatusAnterior(),
            h.getStatusNovo(), h.getResponsavel(), h.getObservacao(), h.getRegistradoEm());
    }
}
