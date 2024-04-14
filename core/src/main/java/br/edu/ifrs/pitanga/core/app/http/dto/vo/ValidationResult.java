package br.edu.ifrs.pitanga.core.app.http.dto.vo;

import br.edu.ifrs.pitanga.core.domain.pbl.vo.ValidationId;

public record ValidationResult(
    ValidationId validation,
    String input,
    String output,
    String expectedOutput,
    ValidationResultStatus status
) {}
