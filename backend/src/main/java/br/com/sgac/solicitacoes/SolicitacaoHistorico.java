package br.com.sgac.solicitacoes;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "solicitacao_historico", schema = "dbo")
public class SolicitacaoHistorico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private Solicitacao solicitacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", length = 20)
    private SolicitacaoStatus statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false, length = 20)
    private SolicitacaoStatus statusNovo;

    @Nationalized
    @Column(nullable = false, length = 120)
    private String responsavel;

    @Nationalized
    @Column(length = 500)
    private String observacao;

    @Column(name = "registrado_em", nullable = false)
    private LocalDateTime registradoEm = LocalDateTime.now(ZoneOffset.UTC);

    protected SolicitacaoHistorico() { }

    public SolicitacaoHistorico(Solicitacao solicitacao, SolicitacaoStatus statusAnterior,
                               SolicitacaoStatus statusNovo, String responsavel,
                               String observacao) {
        this.solicitacao = solicitacao;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.responsavel = responsavel;
        this.observacao = observacao;
    }

    public Long getId() { return id; }
    public SolicitacaoStatus getStatusAnterior() { return statusAnterior; }
    public SolicitacaoStatus getStatusNovo() { return statusNovo; }
    public String getResponsavel() { return responsavel; }
    public String getObservacao() { return observacao; }
    public LocalDateTime getRegistradoEm() { return registradoEm; }
}
