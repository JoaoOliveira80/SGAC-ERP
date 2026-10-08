package br.com.sgac.departamentos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "departamentos", schema = "dbo")
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Nationalized
    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false)
    private boolean ativo = true;

    protected Departamento() {}

    public Departamento(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
        this.ativo = true;
    }

    public void atualizar(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public void alterarStatus(boolean ativo) {
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public boolean isAtivo() { return ativo; }
}
