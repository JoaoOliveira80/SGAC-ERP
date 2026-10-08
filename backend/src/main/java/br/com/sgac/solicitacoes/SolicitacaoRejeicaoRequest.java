package br.com.sgac.solicitacoes;

import jakarta.validation.constraints.*;

public record SolicitacaoRejeicaoRequest(
    @NotBlank @Size(max = 120) String responsavel,
    @NotBlank @Size(min = 10, max = 500) String motivo
) { }
