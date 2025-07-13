package br.edu.ifrs.poa.pitanga_code.app.dtos;

import java.util.List;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioInput;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.Difficulty;

public record CreateProblemRequest(
        String title,
        String slug,
        String description,
        Difficulty initialDifficultyLevel,
        List<Long> allowedLanguages,
        List<ScenarioInput> testingScenarios) {
}
