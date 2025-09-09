package br.edu.ifrs.poa.pitanga_code.domain.pbl.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScenarioOutput {
    private String input, expectedOutput, actualOutput;

    public boolean getPass() {
        return expectedOutput.equals(actualOutput);
    }
}
