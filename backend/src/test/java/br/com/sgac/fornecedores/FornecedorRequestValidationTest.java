package br.com.sgac.fornecedores;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FornecedorRequestValidationTest {
    @Test
    void rejeitaFormatoInvalidoDeCnpjEEmail() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            FornecedorRequest request = new FornecedorRequest("1234", "Fornecedor Teste", "email-invalido");
            var erros = validator.validate(request);
            assertTrue(erros.stream().anyMatch(e -> e.getPropertyPath().toString().equals("cnpj")));
            assertTrue(erros.stream().anyMatch(e -> e.getPropertyPath().toString().equals("email")));
        }
    }

    @Test
    void aceitaCamposBemFormatados() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var erros = validator.validate(new FornecedorRequest(
                "11222333000181", "Fornecedor Demonstrativo", "compras@exemplo.com"));
            assertTrue(erros.isEmpty());
        }
    }
}
