package br.edu.ifrs.pitanga.core.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import br.edu.ifrs.pitanga.core.domain.pbl.Language;

public interface LanguagesRepository extends CrudRepository<Language, Long> {
}
