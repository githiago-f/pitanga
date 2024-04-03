package br.edu.ifrs.pitanga.core.app.http.dto;

import java.util.UUID;

import br.edu.ifrs.pitanga.core.domain.school.User;

public record UserResponse(
    UUID id,
    String name,
    String surname,
    String email,
    String avatar
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getSurname(),
            user.getEmail(),
            user.getAvatar()
        );
    }
}
