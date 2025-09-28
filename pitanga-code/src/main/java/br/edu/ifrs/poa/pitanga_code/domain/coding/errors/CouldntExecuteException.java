package br.edu.ifrs.poa.pitanga_code.domain.coding.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CouldntExecuteException extends RuntimeException {
    public CouldntExecuteException() {
        super("Could not execute this code");
    }
}
