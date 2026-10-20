package br.edu.fiap.banco.application.usecase;

import br.edu.fiap.banco.presenter.dto.TipoContaRequest;
import br.edu.fiap.banco.presenter.dto.TipoContaResponse;
import br.edu.fiap.banco.domain.model.TipoConta;
import br.edu.fiap.banco.domain.exception.TipoContaJaCadastradoException;
import br.edu.fiap.banco.application.exception.TipoContaNaoEncontradoException;
import br.edu.fiap.banco.application.ports.TipoContaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class TipoContaServiceImpl implements TipoContaService {
    private final TipoContaRepository repository;

    /** Injeção por construtor facilita substituir a persistência em testes. */
    @Inject
    public TipoContaServiceImpl(TipoContaRepository repository) {
        this.repository = repository;
    }

    /** Impede nome repetido antes de construir e persistir a entidade. */
    @Override
    public TipoContaResponse cadastrar(TipoContaRequest request) {
        if (repository.existePorNome(request.nome())) {
            throw new TipoContaJaCadastradoException();
        }
        return TipoContaResponse.de(repository.salvar(TipoConta.novo(request.nome())));
    }

    /** Converte a coleção de entidades para o contrato de saída da API. */
    @Override
    public List<TipoContaResponse> listar() {
        return repository.listar().stream().map(TipoContaResponse::de).toList();
    }

    /** Transforma Optional vazio em exceção de negócio 404. */
    @Override
    public TipoContaResponse buscar(Long id) {
        return TipoContaResponse.de(repository.buscarPorId(id).orElseThrow(TipoContaNaoEncontradoException::new));
    }

    /** Atualiza o nome sem permitir que ele colida com outro registro. */
    @Override
    public TipoContaResponse atualizar(Long id, TipoContaRequest request) {
        TipoConta atual = repository.buscarPorId(id).orElseThrow(TipoContaNaoEncontradoException::new);
        if (!atual.nome().equalsIgnoreCase(request.nome()) && repository.existePorNome(request.nome())) {
            throw new TipoContaJaCadastradoException();
        }
        return TipoContaResponse.de(repository.atualizar(atual.atualizarNome(request.nome())));
    }

    /** Exclui e diferencia ID ausente de sucesso. A FK pode produzir conflito. */
    @Override
    public void excluir(Long id) {
        if (!repository.excluir(id)) {
            throw new TipoContaNaoEncontradoException();
        }
    }
}
