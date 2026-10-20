package br.edu.fiap.banco.presenter.resource;

import br.edu.fiap.banco.presenter.dto.LoginRequest;
import br.edu.fiap.banco.presenter.dto.TokenResponse;
import br.edu.fiap.banco.presenter.dto.UsuarioAutenticadoResponse;
import br.edu.fiap.banco.infrastructure.security.JwtService;
import br.edu.fiap.banco.application.usecase.AutenticacaoService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.JsonNumber;
import jakarta.json.JsonString;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AutenticacaoResource {

    private final AutenticacaoService service;

    @Inject
    JsonWebToken jwt;

    @Inject
    public AutenticacaoResource(AutenticacaoService service){
        this.service = service;
    }

    @POST
    @Path("/login")
    @PermitAll
    public TokenResponse login(@NotNull @Valid LoginRequest request){
        return service.login(request);
    }

    @GET
    @Path("/me")
    @RolesAllowed(JwtService.PAPEL_USUARIO)
    public UsuarioAutenticadoResponse me(){
        return new UsuarioAutenticadoResponse(
                jwt.getSubject(),
                lerTexto("nome"),
                lerId("usuarioId"),
                jwt.getGroups()
        );
    }

    public String lerTexto(String claim){
        Object valor = jwt.getClaim(claim);
        if(valor instanceof JsonString json){
            return json.getString();
        }
        return valor == null ? null :  valor.toString();
    }

    private Long lerId(String claim){
        Object valor = jwt.getClaim(claim);
        if(valor instanceof JsonNumber json){
            return json.longValue();
        }

        if(valor instanceof Number numero){
            return numero.longValue();
        }

        return valor == null ? null :  Long.valueOf(valor.toString());
    }

}
