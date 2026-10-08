package br.com.sgac.departamentos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DepartamentoTest {
    @Test
    void criaAtualizaEAlteraStatus() {
        Departamento departamento = new Departamento("TI", "Tecnologia da Informacao");
        assertEquals("TI", departamento.getCodigo());
        assertTrue(departamento.isAtivo());

        departamento.atualizar("RH", "Recursos Humanos");
        departamento.alterarStatus(false);

        assertEquals("RH", departamento.getCodigo());
        assertEquals("Recursos Humanos", departamento.getNome());
        assertFalse(departamento.isAtivo());
    }
}
