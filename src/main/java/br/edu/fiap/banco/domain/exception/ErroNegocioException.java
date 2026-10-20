package br.edu.fiap.banco.domain.exception;

/** Regra de negócio violada. O status HTTP fica no mapper da camada de apresentação. */
public abstract class ErroNegocioException extends RuntimeException {
    protected ErroNegocioException(String mensagem) {
        super(mensagem);
    }
}
