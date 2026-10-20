package br.edu.fiap.banco.application.ports;

import br.edu.fiap.banco.domain.model.TipoConta;
import java.util.List;
import java.util.Optional;

/** Porta usada pelo service para persistir tipos sem depender da API JDBC. */
public interface TipoContaRepository {
    boolean existePorNome(String nome);
    TipoConta salvar(TipoConta tipo);
    List<TipoConta> listar();
    Optional<TipoConta> buscarPorId(Long id);
    TipoConta atualizar(TipoConta tipo);
    boolean excluir(Long id);
}
