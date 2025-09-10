package br.edu.ifrs.poa.pitanga_code.app.http;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifrs.poa.pitanga_code.app.dtos.RunTestsRequest;
import br.edu.ifrs.poa.pitanga_code.app.dtos.SubmissionRequest;
import br.edu.ifrs.poa.pitanga_code.app.usecases.SubmitProblemSolutionUseCase;
import br.edu.ifrs.poa.pitanga_code.app.usecases.TestProblemScenariosUseCase;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.CodeSubmission;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioOutput;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping({ "/submissions" })
public class SubmissionsController {
    private final SubmitProblemSolutionUseCase submitProblemSolutionUseCase;
    private final TestProblemScenariosUseCase runChallengeTestsUseCase;

    @PostMapping
    public CodeSubmission submit(@RequestBody SubmissionRequest request) {
        return submitProblemSolutionUseCase.execute(request);
    }

    @PostMapping(value = { "/test" })
    public ResponseEntity<List<ScenarioOutput>> runExamples(@RequestBody RunTestsRequest code) {
        var results = runChallengeTestsUseCase.execute(code);
        return ResponseEntity.accepted().body(results);
    }
}
