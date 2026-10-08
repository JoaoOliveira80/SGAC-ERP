package br.com.sgac.solicitacoes;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class SolicitacaoTest {
    @Test
    void somaItensSemUsarPontoFlutuante() {
        Solicitacao s = new Solicitacao(null, null, "Ana", "Compra de equipamentos");
        s.adicionarItem("Mouse", 3, new BigDecimal("29.90"));
        s.adicionarItem("Teclado", 2, new BigDecimal("120.50"));
        assertEquals(new BigDecimal("330.70"), s.getValorTotal());
        assertEquals(2, s.getItens().size());
        assertEquals(new BigDecimal("89.70"), s.getItens().get(0).getSubtotal());
    }

    @Test
    void exigeFluxoDeAprovacao() {
        Solicitacao s = new Solicitacao(null, null, "Ana", "Compra de equipamentos");
        s.adicionarItem("Monitor", 1, new BigDecimal("950.00"));
        assertEquals(SolicitacaoStatus.RASCUNHO, s.getStatus());
        assertThrows(IllegalStateException.class, s::aprovar);
        s.enviar();
        assertEquals(SolicitacaoStatus.PENDENTE, s.getStatus());
        assertThrows(IllegalStateException.class, s::enviar);
        s.aprovar();
        assertEquals(SolicitacaoStatus.APROVADA, s.getStatus());
        assertThrows(IllegalStateException.class, s::rejeitar);
    }

    @Test
    void rejeicaoNaoPodeSerRevertidaSemNovoFluxo() {
        Solicitacao s = new Solicitacao(null, null, "Ana", "Compra de equipamentos");
        s.adicionarItem("Monitor", 1, new BigDecimal("950.00"));
        s.enviar();
        s.rejeitar();
        assertEquals(SolicitacaoStatus.REJEITADA, s.getStatus());
        assertThrows(IllegalStateException.class, s::aprovar);
    }
}
