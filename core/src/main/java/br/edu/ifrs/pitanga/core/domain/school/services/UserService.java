package br.edu.ifrs.pitanga.core.domain.school.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifrs.pitanga.core.app.http.dto.CreateUserRequest;
import br.edu.ifrs.pitanga.core.domain.repositories.UsersRepository;
import br.edu.ifrs.pitanga.core.domain.school.User;

@Service
public class UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder encoder;
    public UserService(
        UsersRepository usersRepository, PasswordEncoder encoder) {
        this.usersRepository = usersRepository;
        this.encoder = encoder;
    }

    public User handle(CreateUserRequest userRequest) {
        User userEntity = userRequest.toEntity(encoder);
        return usersRepository.save(userEntity);
    }
}
