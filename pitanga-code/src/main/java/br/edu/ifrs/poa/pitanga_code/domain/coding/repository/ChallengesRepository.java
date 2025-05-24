package br.edu.ifrs.poa.pitanga_code.domain.coding.repository;

import org.springframework.data.repository.CrudRepository;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Challenge;

public interface ChallengesRepository extends CrudRepository<Challenge, Long> {
}
