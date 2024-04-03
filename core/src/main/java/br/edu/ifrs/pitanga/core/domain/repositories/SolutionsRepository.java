package br.edu.ifrs.pitanga.core.domain.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.pitanga.core.domain.pbl.Challenge;
import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.vo.SolutionId;
import br.edu.ifrs.pitanga.core.domain.school.User;
import java.util.List;

@Repository
public interface SolutionsRepository extends CrudRepository<Solution, SolutionId> {
    List<Solution> findBySubmitter(User submitter);
    List<Solution> findByChallenge(Challenge challenge);
}
