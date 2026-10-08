package br.com.sgac.departamentos;

import jakarta.validation.constraints.NotNull;

public record DepartamentoStatusRequest(
    @NotNull(message = "O status e obrigatorio") Boolean ativo
) {}
