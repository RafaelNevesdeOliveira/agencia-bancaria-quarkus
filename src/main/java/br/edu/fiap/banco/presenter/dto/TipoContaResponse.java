package br.edu.fiap.banco.presenter.dto;

import br.edu.fiap.banco.domain.model.TipoConta;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/** Contrato de saída que impede o resource de devolver diretamente a entidade. */
@Schema(name = "TipoContaResponse", description = "Tipo de conta cadastrado")
public record TipoContaResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "CORRENTE") String nome) {

    public static TipoContaResponse de(TipoConta tipo) {
        return new TipoContaResponse(tipo.id(), tipo.nome());
    }
}
