package br.com.sgac.departamentos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DepartamentoRequest(
    @NotBlank(message = "O codigo e obrigatorio")
    @Size(min = 2, max = 20)
    @Pattern(regexp = "[A-Za-z0-9_-]+", message = "Use apenas letras, numeros, _ ou -")
    String codigo,

    @NotBlank(message = "O nome e obrigatorio")
    @Size(min = 3, max = 120)
    String nome
) {}
