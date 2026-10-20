package br.edu.fiap.banco.presenter.dto;

public record TokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        long expiresAt
) {
}
