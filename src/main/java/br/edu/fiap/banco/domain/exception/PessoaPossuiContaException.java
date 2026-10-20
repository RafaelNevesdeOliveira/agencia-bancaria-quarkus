package br.edu.fiap.banco.domain.exception;

/** A pessoa ainda é titular de uma conta e não pode ser excluída. */
public class PessoaPossuiContaException extends ErroNegocioException {
    public PessoaPossuiContaException() {
        super("Pessoa possui conta bancária e não pode ser excluida");
    }
}
