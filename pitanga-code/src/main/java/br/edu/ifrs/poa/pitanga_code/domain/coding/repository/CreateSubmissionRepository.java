package br.edu.ifrs.poa.pitanga_code.domain.coding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.CodeSubmission;
import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.SubmissionId;

public interface CreateSubmissionRepository extends CrudRepository<CodeSubmission, SubmissionId> {

    @Query(value = "FROM CodeSubmission cs WHERE cs.id.problemId = :problemId AND cs.creatorId = :creator ORDER BY cs.id.id DESC")
    List<CodeSubmission> findByProblemIdOderedDesc(Long problemId, String creator);
}
