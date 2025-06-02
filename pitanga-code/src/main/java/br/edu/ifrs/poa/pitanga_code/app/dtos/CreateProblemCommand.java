package br.edu.ifrs.poa.pitanga_code.app.dtos;

import java.util.List;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.Difficulty;

public record CreateProblemCommand(
        String title,
        String description,
        Difficulty initialDifficultyLevel,
        List<Long> allowedLanguages) {
}
