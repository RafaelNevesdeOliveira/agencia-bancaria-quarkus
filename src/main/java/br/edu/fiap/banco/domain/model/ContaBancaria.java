package br.edu.fiap.banco.domain.model;

import java.math.BigDecimal;

public record ContaBancaria(
        Long id,
        String agencia,
        String numero,
        BigDecimal saldo,
        boolean ativa,
        Pessoa titular,
        TipoConta tipoConta
) {

    public ContaBancaria{
        if(agencia == null || !agencia.matches("\\d{4}")){
            throw new IllegalArgumentException("Agência deve ter quatro números");
        }

        if(numero == null || !numero.matches("\\d{6}-\\d")){
            throw new IllegalArgumentException("Agência deve seguir o formato 000000-0");
        }

        if(saldo == null || saldo.signum() < 0){
            throw new IllegalArgumentException("Saldo não pode ser negativo");
        }

        if(titular == null || tipoConta == null){
            throw new IllegalArgumentException("Titular e tipo de conta são obrigatórios");
        }
    }

    //POST
    public static ContaBancaria nova(String agencia, String numero, Pessoa titular, TipoConta tipoConta){
        return new ContaBancaria(
                null,
                agencia,
                numero,
                BigDecimal.ZERO,
                true,
                titular,
                tipoConta);
    }

    // GETBYID
    public ContaBancaria comId(Long novoId){
        return new ContaBancaria(novoId, agencia, numero, saldo, ativa, titular, tipoConta);
    }

    //PUT
    public ContaBancaria atualizar(String novaAgencia, String novoNumero, TipoConta novoTipo){
        return new ContaBancaria(id, novaAgencia, novoNumero, saldo, ativa, titular, novoTipo);
    }

    //PUT (soft delete)
    public ContaBancaria encerrar(){
        return new ContaBancaria(id, agencia, numero, saldo, false, titular, tipoConta);
    }
}
