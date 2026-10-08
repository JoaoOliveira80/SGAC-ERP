package br.com.sgac.solicitacoes;

import br.com.sgac.centroscusto.CentroCusto;
import br.com.sgac.fornecedores.Fornecedor;
import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solicitacoes", schema = "dbo")
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "centro_custo_id", nullable = false)
    private CentroCusto centroCusto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Nationalized
    @Column(nullable = false, length = 120)
    private String solicitante;

    @Nationalized
    @Column(nullable = false, length = 500)
    private String justificativa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SolicitacaoStatus status = SolicitacaoStatus.RASCUNHO;

    @Column(name = "valor_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorTotal = new BigDecimal("0.00");

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm = LocalDateTime.now(ZoneOffset.UTC);

    @Version
    @Column(name = "versao", nullable = false)
    private Long versao;

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<SolicitacaoItem> itens = new ArrayList<>();

    protected Solicitacao() { }

    public Solicitacao(CentroCusto centroCusto, Fornecedor fornecedor,
                       String solicitante, String justificativa) {
        this.centroCusto = centroCusto;
        this.fornecedor = fornecedor;
        this.solicitante = solicitante;
        this.justificativa = justificativa;
    }

    public void adicionarItem(String descricao, int quantidade, BigDecimal valorUnitario) {
        if (status != SolicitacaoStatus.RASCUNHO) {
            throw new IllegalStateException("Itens so podem ser adicionados no rascunho");
        }
        SolicitacaoItem item = new SolicitacaoItem(this, descricao, quantidade, valorUnitario);
        itens.add(item);
        valorTotal = valorTotal.add(item.getSubtotal()).setScale(2, RoundingMode.UNNECESSARY);
    }

    public void enviar() {
        exigirStatus(SolicitacaoStatus.RASCUNHO);
        if (itens.isEmpty()) {
            throw new IllegalStateException("Solicitacao precisa de itens para ser enviada");
        }
        status = SolicitacaoStatus.PENDENTE;
    }

    public void aprovar() {
        exigirStatus(SolicitacaoStatus.PENDENTE);
        status = SolicitacaoStatus.APROVADA;
    }

    public void rejeitar() {
        exigirStatus(SolicitacaoStatus.PENDENTE);
        status = SolicitacaoStatus.REJEITADA;
    }

    private void exigirStatus(SolicitacaoStatus esperado) {
        if (status != esperado) {
            throw new IllegalStateException("Operacao nao permitida no status " + status);
        }
    }

    public Long getId() { return id; }
    public CentroCusto getCentroCusto() { return centroCusto; }
    public Fornecedor getFornecedor() { return fornecedor; }
    public String getSolicitante() { return solicitante; }
    public String getJustificativa() { return justificativa; }
    public SolicitacaoStatus getStatus() { return status; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
    public List<SolicitacaoItem> getItens() { return itens; }
}
