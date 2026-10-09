package br.com.sgac.departamentos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartamentoServiceTest {
    @Mock DepartamentoRepository repository;
    @InjectMocks DepartamentoService service;

    @Test
    void impedeCodigoDuplicado() {
        when(repository.existsByCodigoIgnoreCase("TI")).thenReturn(true);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.criar(new DepartamentoRequest("ti", "Tecnologia da Informacao")));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(repository, never()).save(any());
    }

    @Test
    void normalizaCodigoNaCriacao() {
        when(repository.save(any(Departamento.class))).thenAnswer(inv -> inv.getArgument(0));
        DepartamentoResponse r = service.criar(
            new DepartamentoRequest(" ti ", " Tecnologia da Informacao "));
        assertEquals("TI", r.codigo());
        assertEquals("Tecnologia da Informacao", r.nome());
        assertTrue(r.ativo());
    }

    @Test
    void editarMesmoCodigoNaoEhDuplicidade() {
        Departamento dep = new Departamento("TI", "Tecnologia");
        when(repository.findById(7L)).thenReturn(Optional.of(dep));
        DepartamentoResponse r = service.atualizar(7L,
            new DepartamentoRequest("ti", "Tecnologia e Sistemas"));
        assertEquals("Tecnologia e Sistemas", r.nome());
        verify(repository).existsByCodigoIgnoreCaseAndIdNot("TI", 7L);
    }

    @Test
    void idInexistenteRetorna404() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.buscarPorId(9876L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }
}
