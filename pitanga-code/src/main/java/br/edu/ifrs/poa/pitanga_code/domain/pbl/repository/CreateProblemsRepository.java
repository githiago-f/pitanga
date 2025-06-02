package br.edu.ifrs.poa.pitanga_code.domain.pbl.repository;

import org.springframework.data.repository.CrudRepository;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;

public interface CreateProblemsRepository extends CrudRepository<Problem, Long> {
    Boolean existsBySlug(String slug);
}
