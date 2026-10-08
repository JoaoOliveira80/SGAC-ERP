package br.com.sgac.solicitacoes;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "solicitacao_itens", schema = "dbo")
public class SolicitacaoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private Solicitacao solicitacao;

    @Nationalized
    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(nullable = false)
    private int quantidade;

    @Column(name = "valor_unitario", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorUnitario;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal;

    protected SolicitacaoItem() { }

    public SolicitacaoItem(Solicitacao solicitacao, String descricao,
                          int quantidade, BigDecimal valorUnitario) {
        if (quantidade < 1 || valorUnitario == null || valorUnitario.signum() <= 0) {
            throw new IllegalArgumentException("Quantidade e preco devem ser positivos");
        }
        this.solicitacao = solicitacao;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario.setScale(2, RoundingMode.UNNECESSARY);
        this.subtotal = this.valorUnitario.multiply(BigDecimal.valueOf(quantidade))
            .setScale(2, RoundingMode.UNNECESSARY);
    }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
}
