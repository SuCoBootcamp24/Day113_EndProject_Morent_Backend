package de.morent.backend.dtos.auth;

public record SignUpRequestDto(
        String email,
        String firstName,
        String lastName,
        String password
) {
}
