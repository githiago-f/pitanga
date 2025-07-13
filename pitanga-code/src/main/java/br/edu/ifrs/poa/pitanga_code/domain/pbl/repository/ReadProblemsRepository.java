package br.edu.ifrs.poa.pitanga_code.domain.pbl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;

public interface ReadProblemsRepository extends PagingAndSortingRepository<Problem, Long> {
    @Query("FROM Problem p JOIN FETCH p.testingScenarios s WHERE p.slug = :slug AND s.isExample = true")
    Optional<Problem> findBySlugWithExamples(@Param("slug") String slug);

    @Query("FROM Problem p JOIN FETCH p.testingScenarios s WHERE p.id = :problem_id")
    Optional<Problem> findByIdWithExamples(@Param("problem_id") Long id);
}
