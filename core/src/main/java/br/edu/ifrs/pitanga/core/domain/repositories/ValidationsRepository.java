package br.edu.ifrs.pitanga.core.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import br.edu.ifrs.pitanga.core.domain.pbl.vo.ValidationId;

public interface ValidationsRepository extends CrudRepository<Validation, ValidationId> {
}
