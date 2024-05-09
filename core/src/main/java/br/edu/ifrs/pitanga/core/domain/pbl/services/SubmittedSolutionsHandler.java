package br.edu.ifrs.pitanga.core.domain.pbl.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.edu.ifrs.pitanga.core.app.http.dto.SolutionResponse;
import br.edu.ifrs.pitanga.core.app.http.errors.ChallengeDoesNotExist;
import br.edu.ifrs.pitanga.core.domain.pbl.Challenge;
import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import br.edu.ifrs.pitanga.core.infra.MD5HashCalculator;
import br.edu.ifrs.pitanga.core.infra.runners.CommandRunner;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import br.edu.ifrs.pitanga.core.domain.repositories.ChallengesRepository;
import br.edu.ifrs.pitanga.core.domain.repositories.SolutionsRepository;
import br.edu.ifrs.pitanga.core.domain.pbl.services.commands.SearchSolutionCommand;
import br.edu.ifrs.pitanga.core.domain.pbl.services.commands.SaveSolutionCommand;

@Slf4j
@Service
@AllArgsConstructor
public class SubmittedSolutionsHandler {
    private final CommandRunner runner;
    private final SolutionsRepository solutionsRepository;
    private final ChallengesRepository challengesRepository;
    private final MD5HashCalculator hashCalculator;

    public Mono<SolutionResponse> viewByVersion(SearchSolutionCommand search) {
        Solution solution = solutionsRepository.findByLastVersion(
            search.submitterId(),
            search.challengeId()
        );
        if(solution == null) {
            return Mono.empty();
        }

        SolutionResponse response = SolutionResponse.builder()
            .solutionId(solution.getId())
            .code(solution.getCode())
            .build();

        for (Validation validation : solution.getChallenge().getValidations()) {
            log.info("Executing for validation {}", validation.getId());
            String output = runner.execute(solution, validation);
            log.info("Solution result: {}", output);
            response.addValidationResult(validation, output);
        }

        solution.setPassAllValidations(response.getPassValidations());
        solutionsRepository.save(solution);

        return Mono.just(response);
    }

    @Transactional
    public SolutionResponse handle(SaveSolutionCommand submission) {
        UUID challengeId = submission.challengeId();
        Solution solution = solutionsRepository.findByLastVersion(
            submission.submitterId(),
            challengeId
        );

        Solution entity = submission.toEntity();
        entity.setHash(hashCalculator.calculate(entity.getCode()));
        entity.setVersion(solution);

        Challenge challenge = challengesRepository.findById(challengeId)
            .orElseThrow(() -> new ChallengeDoesNotExist());

        entity.setChallenge(challenge);

        if(!entity.compareHash(solution)) {
            solution = solutionsRepository.save(entity);
        }

        SolutionResponse response = SolutionResponse.builder()
            .solutionId(solution.getId())
            .code(solution.getCode())
            .build();

        return response;
    }
}
