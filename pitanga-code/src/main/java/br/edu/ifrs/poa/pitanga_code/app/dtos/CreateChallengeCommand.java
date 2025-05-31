package br.edu.ifrs.poa.pitanga_code.app.dtos;

import java.util.List;

import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.Difficulty;

public record CreateChallengeCommand(
        String title,
        String description,
        Difficulty initialDifficultyLevel,
        List<Long> allowedLanguages) {
}
