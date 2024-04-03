package br.edu.ifrs.pitanga.core.app.http.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.ifrs.pitanga.core.domain.school.User;

public record CreateUserRequest(
    String email,
    String password,
    String name,
    String surname
) {
    public User toEntity(PasswordEncoder encoder) {
        String encodedPass = encoder.encode(password());
        return User.builder()
            .email(email())
            .password(encodedPass)
            .name(name())
            .surname(surname())
            .build();
    }
}
