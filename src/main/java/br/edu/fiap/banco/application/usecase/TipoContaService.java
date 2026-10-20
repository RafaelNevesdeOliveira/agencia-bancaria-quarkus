package br.edu.fiap.banco.application.usecase;

import br.edu.fiap.banco.presenter.dto.TipoContaRequest;
import br.edu.fiap.banco.presenter.dto.TipoContaResponse;

import java.util.List;

/**
 * Contrato do CRUD de tipos de conta.
 * O resource traduz HTTP; a implementação decide unicidade e existência do registro.
 */
public interface TipoContaService {

    TipoContaResponse cadastrar(TipoContaRequest request);

    List<TipoContaResponse> listar();

    TipoContaResponse buscar(Long id);

    TipoContaResponse atualizar(Long id, TipoContaRequest request);

    void excluir(Long id);
}
