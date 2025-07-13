package br.edu.ifrs.poa.pitanga_code.domain.coding.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Submission;

public interface ReadSubmissionsRepository extends PagingAndSortingRepository<Submission, Long> {
}
