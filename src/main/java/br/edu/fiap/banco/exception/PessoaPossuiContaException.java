package br.edu.fiap.banco.exception;

import jakarta.ws.rs.core.Response;

public class PessoaPossuiContaException extends ErroNegocioException {
    public PessoaPossuiContaException() {
        super(Response.Status.CONFLICT, "Pessoa possui conta bancária e não pode ser excluida");
    }
}
