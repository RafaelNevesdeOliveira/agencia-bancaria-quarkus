package br.edu.fiap.banco.presenter.exception;

import br.edu.fiap.banco.application.exception.ContaNaoEncontradaException;
import br.edu.fiap.banco.application.exception.CredenciaisInvalidasException;
import br.edu.fiap.banco.application.exception.PessoaNaoEncontradaException;
import br.edu.fiap.banco.application.exception.TipoContaNaoEncontradoException;
import br.edu.fiap.banco.domain.exception.ErroNegocioException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

/** Traduz erro de negócio para HTTP. No Spring, o equivalente é um @ControllerAdvice. */
@Provider
public class ErroGlobalMapper implements ExceptionMapper<ErroNegocioException> {
    @Override
    public Response toResponse(ErroNegocioException erro) {
        return Response.status(status(erro))
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("mensagem", erro.getMessage()))
                .build();
    }

    private Response.Status status(ErroNegocioException erro) {
        if (erro instanceof PessoaNaoEncontradaException
                || erro instanceof ContaNaoEncontradaException
                || erro instanceof TipoContaNaoEncontradoException) {
            return Response.Status.NOT_FOUND;
        }
        if (erro instanceof CredenciaisInvalidasException) {
            return Response.Status.UNAUTHORIZED;
        }
        return Response.Status.CONFLICT;
    }
}
