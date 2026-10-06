package br.edu.fiap.banco.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Mesmo contrato de entrada do Spring, agora recebido por Jakarta REST.
 * As anotações @NotBlank, @Pattern e @Email são Jakarta Bean Validation nos dois
 * frameworks: verificam preenchimento, formato do CPF e formato do e-mail.
 * O @Valid do resource aciona estas regras antes de chamar o service.
 */
public record PessoaRequest(
        @Schema(example = "Mariana Costa")
        @NotBlank(message = "Nome é obrigatório.") String nome,
        @Schema(example = "52998224725")
        @NotBlank(message = "CPF é obrigatório.")
        @Pattern(regexp = "\\d{11}", message = "CPF deve ter exatamente 11 números.") String cpf,
        @Schema(example = "mariana.costa@example.test")
        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail deve ter formato válido.") String email) {
}
