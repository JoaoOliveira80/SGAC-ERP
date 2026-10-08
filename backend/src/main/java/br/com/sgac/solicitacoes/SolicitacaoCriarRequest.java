package br.com.sgac.solicitacoes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record SolicitacaoCriarRequest(
    @NotNull @Positive Long centroCustoId,
    @NotNull @Positive Long fornecedorId,
    @NotBlank @Size(max = 120) String solicitante,
    @NotBlank @Size(max = 500) String justificativa,
    @NotEmpty @Size(max = 30) List<@NotNull @Valid SolicitacaoItemRequest> itens
) { }
