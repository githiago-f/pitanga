package br.edu.ifrs.pitanga.core.domain.school.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import br.edu.ifrs.pitanga.core.domain.repositories.UsersRepository;
import br.edu.ifrs.pitanga.core.domain.school.User;
import br.edu.ifrs.pitanga.core.domain.school.services.dto.UserDetailsImpl;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Optional<User> user = usersRepository.findByEmailAndDeletedAtNull(username);
        if(user.isEmpty()) return Mono.empty();
        return Mono.just(new UserDetailsImpl(user.get()));
    }
}
