package br.edu.fiap.banco.exception;

import jakarta.ws.rs.core.Response;

/** CPF repetido no cadastro. O tratamento global responde HTTP 409. */
public class CpfJaCadastradoException extends ErroNegocioException {
    public CpfJaCadastradoException() {
        super(Response.Status.CONFLICT, "CPF já cadastrado.");
    }
}
