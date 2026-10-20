package br.edu.fiap.banco.presenter.resource;

import br.edu.fiap.banco.presenter.dto.TipoContaRequest;
import br.edu.fiap.banco.presenter.dto.TipoContaResponse;
import br.edu.fiap.banco.application.usecase.TipoContaService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Resource da camada HTTP para o CRUD de tipos de conta.
 * Ele converte protocolo em chamadas do service e não executa SQL.
 */
@Path("/api/tipos-conta")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TipoContaResource {
    private final TipoContaService service;

    /** Injeta a camada que contém as regras e a coordenação do CRUD. */
    @Inject
    public TipoContaResource(TipoContaService service) {
        this.service = service;
    }

    /** Cadastra e devolve HTTP 201 com a localização do novo recurso. */
    @POST
    public Response cadastrar(@NotNull @Valid TipoContaRequest request, @Context UriInfo uriInfo) {
        TipoContaResponse resposta = service.cadastrar(request);
        URI location = uriInfo.getAbsolutePathBuilder().path(resposta.id().toString()).build();
        return Response.created(location).entity(resposta).build();
    }

    /** Lista todos os tipos cadastrados. */
    @GET
    public List<TipoContaResponse> listar() {
        return service.listar();
    }

    /** Busca um tipo pelo ID informado no caminho da URL. */
    @GET
    @Path("/{id}")
    public TipoContaResponse buscar(@PathParam("id") Long id) {
        return service.buscar(id);
    }

    /** Substitui o nome do tipo existente após validar o corpo. */
    @PUT
    @Path("/{id}")
    public TipoContaResponse atualizar(@PathParam("id") Long id, @NotNull @Valid TipoContaRequest request) {
        return service.atualizar(id, request);
    }

    /** Exclui o tipo quando nenhuma conta possui essa chave estrangeira. */
    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        service.excluir(id);
        return Response.noContent().build();
    }
}
