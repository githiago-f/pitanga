package br.edu.ifrs.pitanga.core.domain.pbl.services.commands;

import java.util.UUID;

import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.vo.SolutionId;

public record SubmitSolutionCommand(
    String code,
    String language,
    String challengeId,
    String submitterId
) {
    public Solution toEntity() {
        SolutionId id = SolutionId.builder()
            .challengeId(UUID.fromString(challengeId()))
            .submitterId(UUID.fromString(submitterId()))
            .build();
        return Solution.builder()
            .language(language())
            .code(code())
            .id(id)
            .build();
    }
}
