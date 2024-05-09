package br.edu.ifrs.pitanga.core.app.http.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR,
    reason = "Error during solution processing")
public class SolutionProcessingException extends RuntimeException {}
