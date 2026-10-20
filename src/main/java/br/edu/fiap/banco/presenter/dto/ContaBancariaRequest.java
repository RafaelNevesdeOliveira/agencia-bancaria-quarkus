package br.edu.fiap.banco.presenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO de entrada da camada HTTP para abertura e atualização de contas.
 *
 * <p>O cliente envia IDs porque Pessoa e TipoConta já existem no banco. O
 * service resolverá esses IDs antes de construir a entidade. As anotações de
 * Bean Validation rejeitam formato inválido antes da regra de negócio.</p>
 */
@Schema(name = "ContaBancariaRequest", description = "Dados para abrir ou atualizar uma conta")
public record ContaBancariaRequest(
        @NotBlank @Pattern(regexp = "\\d{4}", message = "Agência deve ter quatro números.")
        @Schema(example = "0001") String agencia,
        @NotBlank @Pattern(regexp = "\\d{6}-\\d", message = "Número deve seguir 000000-0.")
        @Schema(example = "100001-1") String numero,
        @NotNull @Schema(example = "1") Long titularId,
        @NotNull @Schema(example = "1") Long tipoContaId) {
}
