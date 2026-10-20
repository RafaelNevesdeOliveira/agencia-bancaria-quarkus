package br.edu.fiap.banco.domain.model;

/**
 * Classificação de uma conta bancária, como CORRENTE ou POUPANCA.
 * O domínio permanece um record simples: JDBC cuida da tabela e o resource cuida do HTTP.
 */
public record TipoConta(Long id, String nome) {
    /** Valida e normaliza o nome em toda construção da entidade. */
    public TipoConta {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do tipo de conta é obrigatório.");
        }
        nome = nome.trim().toUpperCase();
    }

    /** Cria um tipo ainda sem a chave que será gerada pelo banco. */
    public static TipoConta novo(String nome) {
        return new TipoConta(null, nome);
    }

    /** Devolve outra instância com o ID retornado pelo INSERT. */
    public TipoConta comId(Long novoId) {
        return new TipoConta(novoId, nome);
    }

    /** Preserva o ID e passa novamente pela normalização do construtor. */
    public TipoConta atualizarNome(String novoNome) {
        return new TipoConta(id, novoNome);
    }
}
