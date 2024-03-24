package br.edu.ifrs.pitanga.core.domain.pbl;

public record Solution(
    Long id,
    Long version,
    String code,
    String language
) {}
