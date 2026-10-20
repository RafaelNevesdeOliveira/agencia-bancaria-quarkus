package br.edu.fiap.banco.presenter.dto;

import br.edu.fiap.banco.domain.model.ContaBancaria;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * DTO de saída da camada HTTP.
 *
 * <p>A API devolve os IDs e os nomes relacionados para que o consumidor não
 * precise conhecer a entidade interna nem executar chamadas adicionais apenas
 * para apresentar titular e modalidade da conta.</p>
 */
@Schema(name = "ContaBancariaResponse", description = "Conta com titular e tipo relacionados")
public record ContaBancariaResponse(
        Long id,
        String agencia,
        String numero,
        BigDecimal saldo,
        boolean ativa,
        Long titularId,
        String titularNome,
        Long tipoContaId,
        String tipoContaNome) {
    /**
     * Converte a entidade de domínio para o contrato JSON público da API.
     */
    public static ContaBancariaResponse de(ContaBancaria conta) {
        return new ContaBancariaResponse(conta.id(), conta.agencia(), conta.numero(), conta.saldo(), conta.ativa(),
                conta.titular().id(), conta.titular().nome(), conta.tipoConta().id(), conta.tipoConta().nome());
    }
}
