package br.edu.fiap.banco.application.ports;

import br.edu.fiap.banco.domain.model.ContaBancaria;

import java.util.List;
import java.util.Optional;

public interface ContaBancariaRepository {

    boolean existePorAgenciaENumero(String agencia, String numero);

    ContaBancaria salvar(ContaBancaria conta);

    Optional<ContaBancaria> buscarPorId(Long id);

    List<ContaBancaria> listar();

    List<ContaBancaria> listarPorTitular(Long titularId);

    ContaBancaria atualizar(ContaBancaria conta);

    boolean encerrar(Long id);
}
