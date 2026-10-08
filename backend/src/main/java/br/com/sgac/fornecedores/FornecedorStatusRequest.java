package br.com.sgac.fornecedores;

import jakarta.validation.constraints.NotNull;

public record FornecedorStatusRequest(
    @NotNull(message = "Status e obrigatorio") Boolean ativo
) {}
