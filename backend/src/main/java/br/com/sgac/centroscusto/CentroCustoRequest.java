package br.com.sgac.centroscusto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CentroCustoRequest(
    @NotBlank(message = "Codigo e obrigatorio")
    @Size(min = 2, max = 20)
    @Pattern(regexp = "[A-Za-z0-9_-]+", message = "Codigo deve conter apenas letras, numeros, _ ou -")
    String codigo,

    @NotBlank(message = "Nome e obrigatorio")
    @Size(min = 3, max = 120)
    String nome,

    @NotNull(message = "Departamento e obrigatorio")
    @Positive(message = "Departamento deve ser positivo")
    Long departamentoId
) {}
