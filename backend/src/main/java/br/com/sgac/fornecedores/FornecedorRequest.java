package br.com.sgac.fornecedores;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FornecedorRequest(
    @NotBlank(message = "CNPJ e obrigatorio")
    @Pattern(regexp = "\\d{14}", message = "CNPJ deve ter exatamente 14 digitos, sem pontuacao")
    String cnpj,

    @NotBlank(message = "Razao social e obrigatoria")
    @Size(min = 3, max = 160)
    String razaoSocial,

    @Email(message = "Email invalido")
    @Size(max = 160)
    String email
) {}
