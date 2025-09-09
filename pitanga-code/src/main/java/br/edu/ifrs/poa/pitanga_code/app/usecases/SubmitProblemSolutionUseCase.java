package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import br.edu.ifrs.poa.pitanga_code.app.dtos.SubmissionRequest;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.CodeSubmission;
import br.edu.ifrs.poa.pitanga_code.domain.coding.errors.LanguageNotFoundException;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.CreateSubmissionRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioOutput;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Scenario;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.errors.ProblemNotFoundException;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.ReadProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider.Box;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.BuildDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmitProblemSolutionUseCase {
    private final CreateSubmissionRepository submissionsRepository;
    private final ReadProblemsRepository problemsRepository;
    private final LanguagesRepository languagesRepository;
    private final SandboxProvider sandboxProvider;

    public CodeSubmission execute(SubmissionRequest runCommand) {
        var language = languagesRepository.findById(runCommand.languageId());
        var problem = problemsRepository.findByIdWithExamples(runCommand.problemId());

        if (problem.isEmpty()) {
            log.error("Problem(id={}) not found when submiting a solution", runCommand.problemId());
            throw new ProblemNotFoundException(runCommand.problemId());
        }

        if (language.isEmpty()) {
            log.error("Language(id={}) not found when submiting a solution", runCommand.languageId());
            throw new LanguageNotFoundException(runCommand.languageId());
        }

        var scenarios = problem.get().getTestingScenarios();
        var request = new BuildDTO(runCommand.code(), language.get());

        Box box = sandboxProvider.setup(request);
        try {
            int passingCount = 0;
            Scenario failingScenario = null;

            for (Scenario scenario : scenarios) {
                String input = scenario.getInput();
                var result = sandboxProvider.execute(box, request, input);

                var output = new ScenarioOutput(input, scenario.getExpectedOutput(), result.output());
                if (!output.getPass()) {
                    failingScenario = scenario;
                    break;
                }

                passingCount++;
            }

            CodeSubmission submission = CodeSubmission.builder()
                    .code(runCommand.code())
                    .language(language.get())
                    .problem(problem.get())
                    .failingScenario(failingScenario)
                    .passingScenarios(passingCount)
                    .build();

            return submissionsRepository.save(submission);
        } finally {
            sandboxProvider.cleanup(box);
        }
    }
}
