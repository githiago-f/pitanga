package br.edu.ifrs.poa.pitanga_code.domain.coding.errors;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidLanguageException extends RuntimeException {
    public InvalidLanguageException(Set<Language> validLanguages) {
        super("Invalid language for this problem, possible languages are: " +
                String.join(", ", validLanguages.stream().map(i -> i.getName()).toList()));
    }
}
