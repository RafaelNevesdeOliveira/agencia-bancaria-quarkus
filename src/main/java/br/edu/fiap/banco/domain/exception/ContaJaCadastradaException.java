package br.edu.fiap.banco.domain.exception;

/** Agência e número já usados por outra conta. */
public class ContaJaCadastradaException extends ErroNegocioException {
    public ContaJaCadastradaException() {
        super("Agencia e número já cadastrados.");
    }
}
