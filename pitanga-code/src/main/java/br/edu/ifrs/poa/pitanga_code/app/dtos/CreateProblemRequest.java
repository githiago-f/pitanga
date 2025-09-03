package br.edu.ifrs.poa.pitanga_code.app.dtos;

import java.util.List;
import java.util.Set;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioInput;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.Difficulty;

public record CreateProblemRequest(
        String title,
        String slug,
        String description,
        Difficulty initialDifficultyLevel,
        List<Long> allowedLanguages,
        List<ScenarioInput> testingScenarios,
        String reviewCode,
        String baseCode,
        String baseInputCode,
        Long baseLanguage) {

    public Problem toEntity(String userName, Language baseLanguage, Set<Language> languages) {
        return Problem.builder()
                .title(title)
                .slug(slug)
                .description(description)
                .reviewCode(reviewCode)
                .baseCode(baseCode)
                .difficultyLevel(initialDifficultyLevel)
                .allowedLanguages(languages)
                .baseInputCode(baseInputCode)
                .baseLanguage(baseLanguage)
                .creator(userName)
                .build();
    }
}
