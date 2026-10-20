package br.edu.fiap.banco.domain.exception;

/** Nome de tipo de conta já existente. */
public class TipoContaJaCadastradoException extends ErroNegocioException {
    public TipoContaJaCadastradoException() {
        super("Já existe um tipo de conta com esse nome.");
    }
}
