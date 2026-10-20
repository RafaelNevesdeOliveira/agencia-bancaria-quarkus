package br.edu.fiap.banco.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Credencial de acesso persistida na tabela usuarios. A senha guardada é o hash. */
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 60)
    private String senha;

    @Column(nullable = false)
    private boolean ativo;

    protected Usuario() {
    }

    public Usuario(String nome, String email, String senha, boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
    }

    public Usuario(Long id, String nome, String email, String senha, boolean ativo) {
        this(nome, email, senha, ativo);
        this.id = id;
    }

    /** Copia os dados para uma instância nova, pronta para o EntityManager persistir. */
    public static Usuario de(Usuario usuario) {
        Usuario entity = new Usuario();
        entity.nome = usuario.nome;
        entity.email = usuario.email;
        entity.senha = usuario.senha;
        entity.ativo = usuario.ativo;
        return entity;
    }

    /** Copia a linha persistida, inclusive o id gerado pelo banco. */
    public Usuario paraDominio() {
        return new Usuario(id, nome, email, senha, ativo);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public static Usuario novo(String nome, String email, String hash){
        return new Usuario(null, nome.trim(), email.trim().toLowerCase(), hash, true);
    }
}
