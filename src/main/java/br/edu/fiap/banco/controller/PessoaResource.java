package br.edu.fiap.banco.controller;

import br.edu.fiap.banco.dto.PessoaRequest;
import br.edu.fiap.banco.dto.PessoaResponse;
import br.edu.fiap.banco.service.PessoaService;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Camada HTTP equivalente ao PessoaController no Spring.
 * A anotação @Path define a URL base como @RequestMapping; @Consumes e @Produces
 * declaram JSON como consumes e produces do mapeamento Spring.
 */
@Path("/api/pessoas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Pessoas")
public class PessoaResource {
    private final PessoaService service;

    // CDI @Inject faz a injeção pelo construtor; no Spring, usa-se injeção
    // por construtor (com @Autowired opcional quando há um único construtor).
    @Inject
    public PessoaResource(PessoaService service) {
        this.service = service;
    }

    // @POST equivale a @PostMapping no Spring. @NotNull recusa corpo nulo e
    // @Valid aplica as restrições do DTO, como em @Valid @RequestBody.
    @POST
    @Operation(summary = "Cadastra um titular")
    public Response cadastrar(@NotNull @Valid PessoaRequest request) {
        PessoaResponse pessoa = service.cadastrar(request);
        return Response.created(URI.create("/api/pessoas/" + pessoa.id()))
                .entity(pessoa)
                .build();
    }
}
