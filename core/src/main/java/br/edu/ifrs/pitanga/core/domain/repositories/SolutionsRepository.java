package br.edu.ifrs.pitanga.core.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.vo.SolutionId;

@Repository
public interface SolutionsRepository extends JpaRepository<Solution, SolutionId> {
    @Query("FROM solutions s WHERE s.id.submitterId = :submitter AND s.id.challengeId = :challengeId ORDER BY s.id.version DESC LIMIT 1")
    Solution findByLastVersion(Integer submitter, UUID challengeId);
}
