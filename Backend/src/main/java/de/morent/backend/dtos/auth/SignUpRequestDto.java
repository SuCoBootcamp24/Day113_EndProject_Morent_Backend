package de.morent.backend.dtos.store.user;

public record signUpRequestDto(
        String email,
        String firstName,
        String lastName,
        String password
) {
}
