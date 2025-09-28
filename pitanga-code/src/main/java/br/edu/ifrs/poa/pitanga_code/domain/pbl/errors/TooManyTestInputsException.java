package br.edu.ifrs.poa.pitanga_code.domain.pbl.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TooManyTestInputsException extends RuntimeException {
    public TooManyTestInputsException() {
        super("Too many test inputs, the maximum is 10");
    }
}
