package br.edu.ifrs.poa.pitanga_code.domain.pbl.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProblemNotFoundException extends RuntimeException {
    public ProblemNotFoundException(Long problemId) {
        super(String.format("Problem %d was not found", problemId));
    }
}
