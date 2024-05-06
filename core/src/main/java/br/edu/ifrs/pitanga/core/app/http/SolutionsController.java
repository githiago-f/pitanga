package br.edu.ifrs.pitanga.core.app.http;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import br.edu.ifrs.pitanga.core.app.http.dto.SolutionRequest;
import br.edu.ifrs.pitanga.core.app.http.dto.SolutionResponse;
import br.edu.ifrs.pitanga.core.domain.pbl.services.SubmittedSolutionsHandler;
import br.edu.ifrs.pitanga.core.domain.pbl.services.commands.SearchSolutionCommand;
import br.edu.ifrs.pitanga.core.domain.pbl.services.commands.SaveSolutionCommand;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@AllArgsConstructor
@RequestMapping("/challenges/{challengeId}/solutions")
public class SolutionsController {
    private final SubmittedSolutionsHandler solutionsService;

    @GetMapping()
    public Mono<ResponseEntity<SolutionResponse>> viewSolution(Authentication user, @PathVariable UUID challengeId) {
        SearchSolutionCommand command = new SearchSolutionCommand(user.getName(), challengeId);
        return solutionsService.viewByVersion(command)
            .map(ResponseEntity.ok()::body)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping
    public SolutionResponse trySolution(
        Authentication user,
        @PathVariable UUID challengeId,
        @RequestBody SolutionRequest solution
    ) {
        SaveSolutionCommand command = new SaveSolutionCommand(
            solution.code(),
            solution.language(),
            challengeId,
            user.getName()
        );
        return solutionsService.handle(command);
    }
}
