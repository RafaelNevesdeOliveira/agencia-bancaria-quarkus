package br.edu.fiap.banco.application.exception;

import br.edu.fiap.banco.domain.exception.ErroNegocioException;

/** O caso de uso não encontrou a pessoa informada. */
public class PessoaNaoEncontradaException extends ErroNegocioException {
    public PessoaNaoEncontradaException() {
        super("Pessoa não encontrada.");
    }
}
