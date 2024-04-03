package br.edu.ifrs.pitanga.core.app.http;

import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.pitanga.core.app.http.dto.SolutionDTO;
import br.edu.ifrs.pitanga.core.domain.pbl.services.SolutionsExecutingService;
import br.edu.ifrs.pitanga.core.domain.pbl.services.commands.SubmitSolutionCommand;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/challenges/{challengeId}/solutions")
public class SolutionsController {
    private SolutionsExecutingService solutionsService;

    public SolutionsController(SolutionsExecutingService solutionsService) {
        this.solutionsService = solutionsService;
    }

    @PatchMapping()
    public String trySolution(
        @PathVariable String challengeId,
        @RequestBody SolutionDTO solution
    ) {
        SubmitSolutionCommand command = new SubmitSolutionCommand(
            solution.code(),
            solution.language(),
            challengeId,
            ""
        );
        solutionsService.handle(command);
        return "";
    }
}
