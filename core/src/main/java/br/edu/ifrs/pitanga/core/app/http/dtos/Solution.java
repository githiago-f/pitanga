package br.edu.ifrs.pitanga.core.app.http.dtos;

import java.util.Optional;

public record Solution(
    Optional<String> id,
    String code,
    String language,
    Long challengeId
) {}
