package br.edu.ifrs.poa.pitanga_code.domain.coding.repository;

import org.springframework.data.repository.CrudRepository;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.CodeSubmission;

public interface CreateSubmissionRepository extends CrudRepository<CodeSubmission, Long> {
}
