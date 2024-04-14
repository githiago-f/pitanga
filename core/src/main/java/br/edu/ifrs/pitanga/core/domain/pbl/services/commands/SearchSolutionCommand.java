package br.edu.ifrs.pitanga.core.domain.pbl.services.commands;

import java.util.UUID;

public record SearchSolutionCommand(Integer submitterId, UUID challengeId) {
}
