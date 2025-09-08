package br.edu.ifrs.poa.pitanga_code.domain.coding.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.CodeSubmission;

public interface ReadSubmissionsRepository extends PagingAndSortingRepository<CodeSubmission, Long> {
}
