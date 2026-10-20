package br.edu.fiap.banco.application.usecase;

import br.edu.fiap.banco.presenter.dto.PessoaRequest;
import br.edu.fiap.banco.presenter.dto.PessoaResponse;

/** Caso de uso do titular. A implementação fica em {@link PessoaServiceImpl}. */
public interface PessoaService {

    PessoaResponse cadastrar(PessoaRequest request);

    PessoaResponse getPessoaById(Long id);

    PessoaResponse atualizar(Long id, PessoaRequest request);

    void excluir(Long id);
}
