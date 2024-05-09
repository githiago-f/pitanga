package br.edu.ifrs.pitanga.core.domain.pbl.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import br.edu.ifrs.pitanga.core.domain.pbl.Challenge;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import br.edu.ifrs.pitanga.core.domain.pbl.services.commands.SaveChallengeCommand;
import br.edu.ifrs.pitanga.core.domain.pbl.vo.SolutionStatus;
import br.edu.ifrs.pitanga.core.app.http.dto.ChallengePageableFilter;
import br.edu.ifrs.pitanga.core.app.http.dto.ChallengeResponse;
import br.edu.ifrs.pitanga.core.domain.repositories.ChallengesRepository;
import br.edu.ifrs.pitanga.core.domain.repositories.SolutionsRepository;
import br.edu.ifrs.pitanga.core.domain.repositories.ValidationsRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChallengesService {
    private final ChallengesRepository challengesRepository;
    private final ValidationsRepository validationsRepository;
    private final SolutionsRepository solutionsRepository;

    public Page<ChallengeResponse> findAndFilter(String userId, ChallengePageableFilter filter) {
        Pageable pageable = filter.getPage();
        Page<Challenge> page = challengesRepository.findAll(filter.getSpec(), pageable);
        long total = page.getTotalElements();

        List<ChallengeResponse> content = page.getContent().stream().map(i -> {
            Integer solutions = solutionsRepository.countSolutionsForChallenge(userId, i.getId());
            Boolean check = solutionsRepository.solutionPassValidations(userId, i.getId());

            return ChallengeResponse.builder()
                .id(i.getId())
                .title(i.getTitle())
                .level(i.getLevel())
                .status(SolutionStatus.getStatus(solutions, check))
                .build();
        }).toList();

        return new PageImpl<ChallengeResponse>(content, pageable, total);
    }

    public Optional<Challenge> findById(UUID id) {
        return challengesRepository.findById(id);
    }

    public Challenge handle(SaveChallengeCommand command) {
        Challenge challenge = challengesRepository.save(command.toEntity());
        List<Validation> validations = command.request()
            .transformValidations(challenge.getId())
            .stream().map(validationsRepository::save)
            .collect(Collectors.toList());
        challenge.setValidations(validations);
        return challenge;
    }
}
