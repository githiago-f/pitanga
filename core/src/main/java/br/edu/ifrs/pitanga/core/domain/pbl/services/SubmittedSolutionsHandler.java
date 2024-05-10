package br.edu.ifrs.pitanga.core.domain.pbl.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.edu.ifrs.pitanga.core.app.http.dto.SolutionResponse;
import br.edu.ifrs.pitanga.core.app.http.dto.vo.ValidationResult;
import br.edu.ifrs.pitanga.core.app.http.errors.ChallengeNotFoundException;
import br.edu.ifrs.pitanga.core.domain.pbl.Challenge;
import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.infra.MD5HashCalculator;
import br.edu.ifrs.pitanga.core.infra.runners.CommandRunner;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import br.edu.ifrs.pitanga.core.domain.repositories.ChallengesRepository;
import br.edu.ifrs.pitanga.core.domain.repositories.SolutionsRepository;
import br.edu.ifrs.pitanga.core.domain.pbl.services.commands.SaveSolutionCommand;

@Service
@AllArgsConstructor
public class SubmittedSolutionsHandler {
    private final CommandRunner runner;
    private final SolutionsRepository solutionsRepository;
    private final ChallengesRepository challengesRepository;
    private final MD5HashCalculator hashCalculator;

    public Mono<SolutionResponse> viewLast(String userId, UUID challengeId) {
        Solution solution = solutionsRepository.findByLastVersion(userId, challengeId);
        if(solution == null) {
            return Mono.empty();
        }

        SolutionResponse.SolutionResponseBuilder builder = SolutionResponse.builder()
            .solutionId(solution.getId())
            .code(solution.getCode());

        return getResults(solution, builder);
    }

    private Mono<SolutionResponse> getResults(Solution solution, SolutionResponse.SolutionResponseBuilder builder) {
        Flux<ValidationResult> results = Flux.concat(
            solution.validations().map(input -> runner.execute(solution, input)
                    .map(out -> ValidationResult.fromString(input, out))
            ).toList()
        );

        return results.collectList().map(validations -> {
            SolutionResponse response = builder.validationResults(validations)
                .build();
            solution.setPassAllValidations(response.getPassValidations());
            solutionsRepository.save(solution);
            return response;
        });
    }

    public Mono<SolutionResponse> handle(SaveSolutionCommand submission) {
        UUID challengeId = submission.challengeId();
        Solution solution = solutionsRepository.findByLastVersion(
            submission.submitterId(),
            challengeId
        );

        Solution entity = submission.toEntity();
        entity.setHash(hashCalculator.calculate(entity.getCode()));
        entity.setVersion(solution);

        Challenge challenge = challengesRepository.findById(challengeId)
            .orElseThrow(() -> new ChallengeNotFoundException());

        entity.setChallenge(challenge);

        if(!entity.compareHash(solution)) {
            solution = solutionsRepository.save(entity);
        }

        SolutionResponse.SolutionResponseBuilder builder = SolutionResponse.builder()
            .solutionId(entity.getId())
            .code(entity.getCode());

        return getResults(entity, builder);
    }
}
