package br.com.sgac.fornecedores;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "fornecedores", schema = "dbo")
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    @Nationalized
    @Column(name = "razao_social", nullable = false, length = 160)
    private String razaoSocial;

    @Column(length = 160)
    private String email;

    @Column(nullable = false)
    private boolean ativo = true;

    protected Fornecedor() {}

    public Fornecedor(String cnpj, String razaoSocial, String email) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.email = email;
    }

    public void atualizar(String cnpj, String razaoSocial, String email) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.email = email;
    }

    public void alterarStatus(boolean ativo) { this.ativo = ativo; }
    public Long getId() { return id; }
    public String getCnpj() { return cnpj; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getEmail() { return email; }
    public boolean isAtivo() { return ativo; }
}
