package br.edu.ifrs.poa.pitanga_code.domain.pbl.dto;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Scenario;

public record ScenarioInput(String input, Boolean isExample) {
    public Scenario toEntity() {
        return new Scenario(input, isExample);
    }
}
