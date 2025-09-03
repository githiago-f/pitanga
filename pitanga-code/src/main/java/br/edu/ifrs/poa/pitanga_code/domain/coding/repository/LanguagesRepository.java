package br.edu.ifrs.poa.pitanga_code.domain.coding.repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.poa.pitanga_code.domain.coding.dtos.Lang;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;

@Repository
public interface LanguagesRepository extends CrudRepository<Language, Long> {
    @Query("SELECT new br.edu.ifrs.poa.pitanga_code.domain.coding.dtos.Lang(l.id, l.name) FROM Language l")
    public List<Lang> findLanguageList();

    default Set<Language> findAllByIds(Collection<Long> langIDs) {
        Set<Language> languages = new HashSet<>();
        findAllById(langIDs).forEach(languages::add);
        return languages;
    }
}
