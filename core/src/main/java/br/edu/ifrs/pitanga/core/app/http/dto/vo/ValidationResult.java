package br.edu.ifrs.pitanga.core.app.http.dto.vo;

import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import br.edu.ifrs.pitanga.core.domain.pbl.vo.ValidationId;

public record ValidationResult(
    ValidationId validation,
    String input,
    String output,
    String expectedOutput,
    ValidationResultStatus status
) {
    public static ValidationResult fromString(Validation validation, String outputData) {
        return new ValidationResult(
            validation.getId(),
            validation.getTestInput(),
            outputData,
            validation.getExpectedOutput(),
            validation.getExpectedOutput().equals(outputData) ?
                ValidationResultStatus.PASS : ValidationResultStatus.FAIL
        );
    }
}
