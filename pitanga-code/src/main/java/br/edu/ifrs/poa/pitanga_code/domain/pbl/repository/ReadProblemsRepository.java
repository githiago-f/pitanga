package br.edu.ifrs.poa.pitanga_code.domain.pbl.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;

public interface ReadProblemsRepository extends PagingAndSortingRepository<Problem, Long> {
    Optional<Problem> findBySlug(String slug);
}
