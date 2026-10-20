package br.edu.fiap.banco.presenter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO de cadastro recebido pela borda HTTP.
 *
 * <p>Este é o único contrato que transporta a senha em texto. O service deve
 * transformá-la imediatamente em hash, e logs nunca devem imprimir o objeto.</p>
 */
@Schema(description = "Dados para criar a identidade de acesso")
public record UsuarioRequest(
        @NotBlank @Size(max = 120) @Schema(example = "Ana Lima") String nome,
        @NotBlank @Email @Size(max = 160) @Schema(example = "ana@example.test") String email,
        @NotBlank @Size(min = 8, max = 72) @Schema(example = "Senha@123") String senha) {
}
