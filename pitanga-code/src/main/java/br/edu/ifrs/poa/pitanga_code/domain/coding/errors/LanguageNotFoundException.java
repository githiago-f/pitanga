package br.edu.ifrs.poa.pitanga_code.domain.coding.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LanguageNotFoundException extends RuntimeException {
    public LanguageNotFoundException(Long languageId) {
        super(String.format("Language %d do not exist", languageId));
    }
}
