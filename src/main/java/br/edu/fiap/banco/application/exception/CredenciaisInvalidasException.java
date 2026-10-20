package br.edu.fiap.banco.application.exception;

import br.edu.fiap.banco.domain.exception.ErroNegocioException;

/** Login recusado porque o usuário não existe, está inativo ou a senha não confere. */
public class CredenciaisInvalidasException extends ErroNegocioException {
    public CredenciaisInvalidasException() {
        super("Credenciais inválidas.");
    }
}
