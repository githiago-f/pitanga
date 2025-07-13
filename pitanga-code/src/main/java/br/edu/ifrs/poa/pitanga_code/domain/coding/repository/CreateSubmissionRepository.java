package br.edu.ifrs.poa.pitanga_code.domain.coding.repository;

import org.springframework.data.repository.CrudRepository;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Submission;

public interface CreateSubmissionRepository extends CrudRepository<Submission, Long> {
}
