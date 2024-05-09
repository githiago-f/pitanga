package br.edu.ifrs.pitanga.core.app.http.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Challenge does not exist", code = HttpStatus.BAD_REQUEST)
public class ChallengeDoesNotExist extends RuntimeException {
}
