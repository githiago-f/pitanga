package br.edu.ifrs.pitanga.core.domain.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.pitanga.core.domain.school.User;
import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<User, UUID> {
    Optional<User> findByEmailAndDeletedAtNull(String email);
}
