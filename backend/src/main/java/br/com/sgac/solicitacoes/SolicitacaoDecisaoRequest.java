package br.com.sgac.solicitacoes;

import jakarta.validation.constraints.*;

public record SolicitacaoDecisaoRequest(
    @NotBlank @Size(max = 120) String responsavel,
    @Size(max = 500) String observacao
) { }
