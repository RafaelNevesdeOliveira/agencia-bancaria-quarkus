package br.edu.fiap.banco.domain.exception;

/** E-mail repetido no cadastro de acesso. */
public class EmailJaCadastradoException extends ErroNegocioException {
    public EmailJaCadastradoException() {
        super("E-mail já cadastrad");
    }
}
