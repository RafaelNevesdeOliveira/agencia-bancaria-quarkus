package br.edu.fiap.banco.application.exception;

import br.edu.fiap.banco.domain.exception.ErroNegocioException;

/** O caso de uso não encontrou o tipo de conta informado. */
public class TipoContaNaoEncontradoException extends ErroNegocioException {
    public TipoContaNaoEncontradoException() {
        super("Tipo de conta não encontrado.");
    }
}
