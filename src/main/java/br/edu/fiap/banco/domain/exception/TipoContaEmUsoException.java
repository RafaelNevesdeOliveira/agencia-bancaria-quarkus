package br.edu.fiap.banco.domain.exception;

/** O tipo ainda está associado a uma conta bancária. */
public class TipoContaEmUsoException extends ErroNegocioException {
    public TipoContaEmUsoException() {
        super("Tipo de conta está associado a uma conta bancária.");
    }
}
