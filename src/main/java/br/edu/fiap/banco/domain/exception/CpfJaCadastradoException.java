package br.edu.fiap.banco.domain.exception;

/** CPF repetido no cadastro. */
public class CpfJaCadastradoException extends ErroNegocioException {
    public CpfJaCadastradoException() {
        super("CPF já cadastrado.");
    }
}
