package br.edu.ifrs.pitanga.core.domain.pbl.services.commands;

import org.springframework.security.core.Authentication;

import br.edu.ifrs.pitanga.core.app.http.dto.ChallengeRequest;
import br.edu.ifrs.pitanga.core.domain.pbl.Challenge;

public record SaveChallengeCommand(Authentication user, ChallengeRequest request) {
    public Challenge toEntity() {
        return request.toEntity(user.getName());
    }
}
