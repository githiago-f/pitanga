package br.edu.ifrs.poa.pitanga_code.domain.pbl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;

public interface ReadProblemsRepository extends PagingAndSortingRepository<Problem, Long> {
    @Query("SELECT DISTINCT p FROM Problem p " +
            "JOIN FETCH p.testingScenarios s " +
            "WHERE p.slug = :problem_slug")
    Optional<Problem> findBySlug(@Param("problem_slug") String slug);
}
