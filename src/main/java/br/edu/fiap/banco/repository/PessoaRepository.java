package br.edu.fiap.banco.repository;

import br.edu.fiap.banco.entity.Pessoa;

/** Fronteira que será implementada com JDBC  */
public interface PessoaRepository {
    boolean existePorCpf(String cpf);
    Pessoa salvar(Pessoa pessoa);
}
