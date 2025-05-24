package br.edu.ifrs.poa.pitanga_code.app.commands;

import java.util.List;
import java.util.Optional;

import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.Difficulty;

public record CreateChallengeCommand(
        String title,
        String description,
        Difficulty initialDifficultyLevel,
        List<Long> allowedLanguages,
        Optional<String> baseCode) {
}
