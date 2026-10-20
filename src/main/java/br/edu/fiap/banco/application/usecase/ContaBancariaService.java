package br.edu.fiap.banco.application.usecase;

import br.edu.fiap.banco.presenter.dto.ContaBancariaRequest;
import br.edu.fiap.banco.presenter.dto.ContaBancariaResponse;

import java.util.List;

/** Caso de uso da conta bancária. A implementação fica em {@link ContaBancariaServiceImpl}. */
public interface ContaBancariaService {

    List<ContaBancariaResponse> listar();

    List<ContaBancariaResponse> listarPorTitular(Long titularId);

    ContaBancariaResponse criarConta(ContaBancariaRequest request);

    void encerrar(Long id);

    ContaBancariaResponse atualizar(Long id, ContaBancariaRequest request);
}
