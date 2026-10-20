package br.edu.fiap.banco.presenter.resource;

import br.edu.fiap.banco.presenter.dto.ContaBancariaRequest;
import br.edu.fiap.banco.presenter.dto.ContaBancariaResponse;
import br.edu.fiap.banco.application.usecase.ContaBancariaService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;


@Path("/api/contas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContaBancariaResource {
    private final ContaBancariaService service;

    /** Injeção por construtor mantém a dependência explícita e testável. */
    @Inject
    public ContaBancariaResource(ContaBancariaService service) {
        this.service = service;
    }

    /** Atende {@code GET /api/contas} e devolve a coleção completa. */
    @GET
    public List<ContaBancariaResponse> listar() {
        return service.listar();
    }

    /** Atende {@code GET /api/contas/pessoa/{pessoaId}} e demonstra o relacionamento 1:N. */
    @GET
    @Path("/pessoa/{pessoaId}")
    public List<ContaBancariaResponse> listarPorPessoa(@PathParam("pessoaId") Long id) {
        return service.listarPorTitular(id);
    }

    @POST
    public Response abrir(@NotNull @Valid ContaBancariaRequest request, @Context UriInfo uriInfo) {
        ContaBancariaResponse resposta = service.criarConta(request);
        URI location = uriInfo.getAbsolutePathBuilder().path(resposta.id().toString()).build();
        return Response.created(location).entity(resposta).build();
    }

    /** Atende {@code PUT /api/contas/{id}} e substitui os campos editáveis. */
    @PUT
    @Path("/{id}")
    public ContaBancariaResponse atualizar(
            @PathParam("id") Long id,
            @NotNull @Valid ContaBancariaRequest request) {
        return service.atualizar(id, request);
    }

    /** Atende {@code DELETE /api/contas/{id}} com encerramento lógico e HTTP 204. */
    @DELETE
    @Path("/{id}")
    public Response encerrar(@PathParam("id") Long id) {
        service.encerrar(id);
        return Response.noContent().build();
    }
}
