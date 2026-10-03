package br.edu.fiap.banco.entity;

/** Objeto de domínio sem anotações JPA no Dia 1. */
public record Pessoa(Long id, String nome, String cpf, String email) {
    public Pessoa comId(Long novoId) {
        return new Pessoa(novoId, nome, cpf, email);
    }
}
