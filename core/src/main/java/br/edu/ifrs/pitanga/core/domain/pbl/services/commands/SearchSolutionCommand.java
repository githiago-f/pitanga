package br.edu.ifrs.pitanga.core.domain.pbl.services.commands;

import java.util.UUID;

public record SearchSolutionCommand(String submitterId, UUID challengeId) {
}
