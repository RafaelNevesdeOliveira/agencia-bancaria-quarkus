package br.edu.fiap.banco.presenter.resource;

import br.edu.fiap.banco.presenter.dto.UsuarioRequest;
import br.edu.fiap.banco.presenter.dto.UsuarioResponse;
import br.edu.fiap.banco.application.usecase.UsuarioService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

/**
 * Resource da camada HTTP para cadastro de identidade de acesso.
 * A rota é pública porque o usuário ainda não possui um token.
 */
@Path("/api/usuarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {
    private final UsuarioService service;

    @Inject
    public UsuarioResource(UsuarioService service) {
        this.service = service;
    }


    // PermitAll: esta rota não exige papel. O usuário ainda não tem token.
    @POST
    @PermitAll
    public Response cadastrar(@NotNull @Valid UsuarioRequest request) {
        UsuarioResponse resposta = service.cadastrarUsuario(request);
        return Response.created(URI.create("/api/usuarios/" + resposta.id())).entity(resposta).build();
    }
}
