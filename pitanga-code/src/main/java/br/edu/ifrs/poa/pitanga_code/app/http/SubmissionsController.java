package br.edu.ifrs.poa.pitanga_code.app.http;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.poa.pitanga_code.app.usecases.RunChallengeTestsUseCase;
import br.edu.ifrs.poa.pitanga_code.app.usecases.SubmitSolutionUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping({ "/submissions" })
public class SubmissionsController {
    private final RunChallengeTestsUseCase runChallengeTestsUseCase;
    private final SubmitSolutionUseCase submitSolutionUseCase;

    @PostMapping
    public void submit() {
        submitSolutionUseCase.execute();
    }

    @PostMapping(value = { "/{id}/tests" })
    public List<String> runTests() {
        return runChallengeTestsUseCase.execute();
    }
}
