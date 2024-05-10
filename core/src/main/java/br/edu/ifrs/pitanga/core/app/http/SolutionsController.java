package br.edu.ifrs.pitanga.core.app.http;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import br.edu.ifrs.pitanga.core.app.http.dto.SolutionRequest;
import br.edu.ifrs.pitanga.core.app.http.dto.SolutionResponse;
import br.edu.ifrs.pitanga.core.domain.pbl.services.SubmittedSolutionsHandler;
import br.edu.ifrs.pitanga.core.domain.pbl.services.commands.SaveSolutionCommand;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@AllArgsConstructor
@RequestMapping("/challenges/{challengeId}/solutions")
public class SolutionsController {
    private final SubmittedSolutionsHandler solutionsService;

    @GetMapping
    public Mono<SolutionResponse> viewLastChallengeSolution(
        Authentication user, @PathVariable UUID challengeId) {
        return solutionsService.viewLast(user.getName(), challengeId);
    }

    @PutMapping
    public Mono<SolutionResponse> handleSolutionChangeStream(Authentication user,
        @PathVariable UUID challengeId, @RequestBody SolutionRequest solution) {
        SaveSolutionCommand command = new SaveSolutionCommand(
            solution.code(),
            solution.language(),
            challengeId,
            user.getName()
        );
        return solutionsService.handle(command);
    }
}
