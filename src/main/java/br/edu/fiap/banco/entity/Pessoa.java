package br.edu.fiap.banco.entity;

/** Objeto de domínio sem anotações JPA no Dia 1. */
public record Pessoa(Long id, String nome, String cpf, String email) {

    public Pessoa{
        if(nome == null || nome.isBlank()){
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if(cpf == null || !cpf.matches("\\d{11}")){
            throw new IllegalArgumentException("CPF deve ter exatamente 11 caracteres");
        }

        if(email == null || !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")){
            throw new IllegalArgumentException("E-mail deve ter formato válido");
        }

        nome = nome.trim();
        email = email.trim();
    }

    public Pessoa atualizar(String novoNome, String novoCpf, String novoEmail){
        return new Pessoa(id, novoNome, novoCpf, novoEmail);
    }

    // O service soh contulta o banco quando o CPF deixou de ser o atual
    public boolean cpfAlterado(String novoCpf){
        return !cpf.equals((novoCpf));
    }

    // Devolve a mesma pessoa ja com a chave gerada (id)
    public Pessoa comId(Long novoId) {
        return new Pessoa(novoId, nome, cpf, email);
    }
}
