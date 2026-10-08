package br.com.sgac.centroscusto;

import jakarta.validation.constraints.NotNull;

public record CentroCustoStatusRequest(
    @NotNull(message = "Status e obrigatorio") Boolean ativo
) {}
