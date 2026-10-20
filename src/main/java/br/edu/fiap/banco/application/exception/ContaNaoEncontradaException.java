package br.edu.fiap.banco.application.exception;

import br.edu.fiap.banco.domain.exception.ErroNegocioException;

/** O caso de uso não encontrou a conta informada. */
public class ContaNaoEncontradaException extends ErroNegocioException {
    public ContaNaoEncontradaException() {
        super("Conta bancária não encontrada");
    }
}
