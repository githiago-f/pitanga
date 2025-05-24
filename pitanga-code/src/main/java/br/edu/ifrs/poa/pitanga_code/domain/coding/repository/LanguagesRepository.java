package br.edu.ifrs.poa.pitanga_code.domain.coding.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;

@Repository
public interface LanguagesRepository extends CrudRepository<Language, Long> {
}
