package br.edu.ifrs.pitanga.core.app.http.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.edu.ifrs.pitanga.core.domain.pbl.Challenge;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import br.edu.ifrs.pitanga.core.domain.pbl.vo.ValidationId;

record ValidationDTO(String input, String output) {}

public record ChallengeRequest(
    Integer creatorId,
    String title,
    String description,
    String baseCode,
    List<ValidationDTO> validations
) {
    public List<Validation> transformValidations(UUID challengeId) {
        List<Validation> aValidations = new ArrayList<>();
        for (int i = 0; i < validations.size(); i++) {
            ValidationDTO validationDTO = validations.get(i);
            ValidationId id = ValidationId.builder()
                .id((long) i + 1)
                .challengeId(challengeId)
                .build();
            Validation validation = Validation.builder()
                    .id(id)
                    .testInput(validationDTO.input())
                    .expectedOutput(validationDTO.output())
                    .build();
            aValidations.add(validation);
        }
        return aValidations;
    }

    public Challenge toEntity() {
        return Challenge.builder()
            .title(title())
            .description(description())
            .creatorId(creatorId())
            .baseCode(baseCode())
            .build();
    }
}
