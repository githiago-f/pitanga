package br.edu.ifrs.pitanga.core.domain.pbl.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifrs.pitanga.core.domain.pbl.Challenge;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import br.edu.ifrs.pitanga.core.app.http.dto.ChallengePageable;
import br.edu.ifrs.pitanga.core.app.http.dto.ChallengeRequest;
import br.edu.ifrs.pitanga.core.domain.repositories.ChallengesRepository;
import br.edu.ifrs.pitanga.core.domain.repositories.ValidationsRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChallengesService {
    private final ChallengesRepository challengesRepository;
    private final ValidationsRepository validationsRepository;

    public Page<Challenge> handle(ChallengePageable pageable) {
        return challengesRepository.findAll(pageable.toPageable());
    }

    public Optional<Challenge> handle(UUID id) {
        return challengesRepository.findById(id);
    }

    public Challenge handle(ChallengeRequest command) {
        Challenge challenge = challengesRepository.save(command.toEntity());
        List<Validation> validations = command.transformValidations(challenge.getId())
            .stream().map(validationsRepository::save)
            .collect(Collectors.toList());
        challenge.setValidations(validations);
        return challenge;
    }
}
