package br.edu.fiap.banco.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

// @Provider registra o ExceptionMapper no Jakarta REST do Quarkus.
// Em Spring MVC, @ControllerAdvice com @ExceptionHandler faria o mapeamento.
@Provider
public class CpfJaCadastradoMapper implements ExceptionMapper<CpfJaCadastradoException> {
    @Override
    public Response toResponse(CpfJaCadastradoException erro) {
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("mensagem", erro.getMessage()))
                .build();
    }
}
