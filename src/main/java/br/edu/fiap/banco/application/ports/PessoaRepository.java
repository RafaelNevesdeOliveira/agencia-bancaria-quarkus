package br.edu.fiap.banco.application.ports;

import br.edu.fiap.banco.domain.model.Pessoa;

import java.util.Optional;

/** Fronteira que será implementada com JDBC  */
public interface PessoaRepository {
    boolean existePorCpf(String cpf);
    Pessoa salvar(Pessoa pessoa);

    Optional<Pessoa> getPessoaById(Long id);

    Pessoa atualizar(Pessoa pessoa);

    boolean excluir(Long id);
}
