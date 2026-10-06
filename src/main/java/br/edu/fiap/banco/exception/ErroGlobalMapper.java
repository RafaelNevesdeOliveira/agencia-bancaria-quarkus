package br.edu.fiap.banco.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

abstract class ErroNegocioException extends RuntimeException {
    private final Response.Status status;

    protected ErroNegocioException(Response.Status status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    Response.Status status() {
        return status;
    }
}

/** Um mapper para todo erro de negócio. No Spring, o equivalente é um @ControllerAdvice. */
@Provider
public class ErroGlobalMapper implements ExceptionMapper<ErroNegocioException> {
    @Override
    public Response toResponse(ErroNegocioException erro) {
        return Response.status(erro.status())
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("mensagem", erro.getMessage()))
                .build();
    }
}
