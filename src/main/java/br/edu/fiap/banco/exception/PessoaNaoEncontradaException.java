package br.edu.fiap.banco.exception;

import jakarta.ws.rs.core.Response;

/** Pessoa ausente na consulta por id. O tratamento global responde HTTP 404. */
public class PessoaNaoEncontradaException extends ErroNegocioException {
    public PessoaNaoEncontradaException() {
        super(Response.Status.NOT_FOUND, "Pessoa não encontrada.");
    }
}
