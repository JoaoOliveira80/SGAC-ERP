package br.com.sgac.centroscusto;

import br.com.sgac.departamentos.Departamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "centros_custo", schema = "dbo")
public class CentroCusto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Nationalized
    @Column(nullable = false, length = 120)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @Column(nullable = false)
    private boolean ativo = true;

    protected CentroCusto() {}

    public CentroCusto(String codigo, String nome, Departamento departamento) {
        this.codigo = codigo;
        this.nome = nome;
        this.departamento = departamento;
    }

    public void atualizar(String codigo, String nome, Departamento departamento) {
        this.codigo = codigo;
        this.nome = nome;
        this.departamento = departamento;
    }

    public void alterarStatus(boolean ativo) { this.ativo = ativo; }
    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public Departamento getDepartamento() { return departamento; }
    public boolean isAtivo() { return ativo; }
}
