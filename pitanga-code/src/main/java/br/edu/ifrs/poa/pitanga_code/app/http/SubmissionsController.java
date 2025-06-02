package br.edu.ifrs.poa.pitanga_code.app.http;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import br.edu.ifrs.poa.pitanga_code.app.dtos.RunTestsCommand;
import br.edu.ifrs.poa.pitanga_code.app.usecases.TestProblemScenariosUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping({ "/submissions" })
public class SubmissionsController {
    private final TestProblemScenariosUseCase runChallengeTestsUseCase;

    @PostMapping
    public void submit() {
        // TODO implement submissions handler
    }

    @PostMapping(value = { "/test" })
    public List<String> runExamples(@RequestBody RunTestsCommand code) {
        return runChallengeTestsUseCase.execute(code);
    }
}
