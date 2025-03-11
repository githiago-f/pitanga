package br.edu.ifrs.pitanga.core.app.http.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Language not found", code = HttpStatus.BAD_REQUEST)
public class LanguageNotFoundException extends RuntimeException {}
