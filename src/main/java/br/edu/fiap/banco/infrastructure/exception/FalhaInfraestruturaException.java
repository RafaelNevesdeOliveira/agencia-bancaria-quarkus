package br.edu.fiap.banco.infrastructure.exception;

/** Falha técnica de persistência. A causa original fica disponível para diagnóstico. */
public class FalhaInfraestruturaException extends RuntimeException {
    public FalhaInfraestruturaException(String mensagem) {
        super(mensagem);
    }

    public FalhaInfraestruturaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
