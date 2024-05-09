package br.edu.ifrs.pitanga.core.app.http.dto;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifrs.pitanga.core.app.http.dto.vo.ValidationResult;
import br.edu.ifrs.pitanga.core.app.http.dto.vo.ValidationResultStatus;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import br.edu.ifrs.pitanga.core.domain.pbl.vo.SolutionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder @Data
@AllArgsConstructor
public class SolutionResponse {
    private SolutionId solutionId;
    private String code;
    @Builder.Default
    private List<ValidationResult> validationResults = new ArrayList<>();

    public void addValidationResult(Validation validation, String output) {
        ValidationResult result = new ValidationResult(
            validation.getId(),
            validation.getTestInput(),
            output,
            validation.getExpectedOutput(),
            validation.getExpectedOutput().equals(output) ?
                ValidationResultStatus.PASS : ValidationResultStatus.FAIL
        );
        validationResults.add(result);
    }

    public Boolean getPassValidations() {
        Boolean pass = validationResults.size() != 0;
        for (ValidationResult validationResult : validationResults) {
            if(validationResult.status() == ValidationResultStatus.FAIL) {
                pass = false;
            }
        }
        return pass;
    }
}
