package br.com.sgac.solicitacoes;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record SolicitacaoItemRequest(
    @NotBlank @Size(max = 200) String descricao,
    @NotNull @Min(1) @Max(10000) Integer quantidade,
    @NotNull @DecimalMin("0.01") @Digits(integer = 12, fraction = 2) BigDecimal valorUnitario
) { }
