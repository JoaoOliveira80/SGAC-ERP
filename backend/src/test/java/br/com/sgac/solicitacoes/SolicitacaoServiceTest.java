package br.com.sgac.solicitacoes;

import br.com.sgac.centroscusto.CentroCustoRepository;
import br.com.sgac.fornecedores.FornecedorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitacaoServiceTest {
    @Mock SolicitacaoRepository solicitacoes;
    @Mock SolicitacaoHistoricoRepository historicos;
    @Mock CentroCustoRepository centros;
    @Mock FornecedorRepository fornecedores;
    @InjectMocks SolicitacaoService service;

    @Test
    void rejeitaCentroDeCustoInexistenteSemCriarSolicitacao() {
        SolicitacaoCriarRequest request = new SolicitacaoCriarRequest(
            100L, 10L, "Maria Oliveira", "Compra para o setor",
            List.of(new SolicitacaoItemRequest("Monitor", 1, new java.math.BigDecimal("650.00"))));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.criar(request));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(solicitacoes, never()).saveAndFlush(any());
    }

    @Test
    void naoPermiteAprovarUmRascunho() {
        Solicitacao pedido = new Solicitacao(null, null, "Maria", "Compra");
        when(solicitacoes.findById(4L)).thenReturn(Optional.of(pedido));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.aprovar(4L, new SolicitacaoDecisaoRequest("Carlos", "Ok")));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(historicos, never()).save(any());
        assertEquals(SolicitacaoStatus.RASCUNHO, pedido.getStatus());
    }

    @Test
    void naoPermiteRejeitarUmRascunho() {
        Solicitacao pedido = new Solicitacao(null, null, "Maria", "Compra");
        when(solicitacoes.findById(4L)).thenReturn(Optional.of(pedido));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.rejeitar(4L, new SolicitacaoRejeicaoRequest("Carlos", "Motivo fundamentado")));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(historicos, never()).save(any());
    }
}
