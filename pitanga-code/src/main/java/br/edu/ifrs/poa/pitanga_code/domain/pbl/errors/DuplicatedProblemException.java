package br.edu.ifrs.poa.pitanga_code.domain.pbl.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicatedProblemException extends RuntimeException {
    public DuplicatedProblemException(String slug) {
        super("Duplicated problem, check for /problems/" + slug);
    }
}
