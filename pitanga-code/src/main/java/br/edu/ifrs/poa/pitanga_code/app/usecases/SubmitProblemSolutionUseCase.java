package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.ifrs.poa.pitanga_code.app.dtos.SubmissionRequest;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Submission;
import br.edu.ifrs.poa.pitanga_code.domain.coding.errors.LanguageNotFoundException;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.CreateSubmissionRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.SubmissionId;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
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

    public Submission execute(SubmissionRequest runCommand) {
        Optional<Language> language = languagesRepository.findById(runCommand.languageId());
        Optional<Problem> problem = problemsRepository.findByIdWithExamples(runCommand.problemId());

        if (problem.isEmpty()) {
            log.error("Problem(id={}) not found when submiting a solution", runCommand.problemId());
            throw new ProblemNotFoundException(runCommand.problemId());
        }

        if (language.isEmpty()) {
            log.error("Language(id={}) not found when submiting a solution", runCommand.languageId());
            throw new LanguageNotFoundException(runCommand.languageId());
        }

        List<Scenario> scenarios = problem.get().getTestingScenarios();
        BuildDTO request = new BuildDTO(runCommand.code(), language.get());

        Box box = sandboxProvider.setup(request);
        try {
            int i = 0;
            for (; i < scenarios.size(); i++) {
                sandboxProvider.execute(box, request, scenarios.get(i).getInput());
            }

            Submission submission = new Submission(
                    new SubmissionId(1l, problem.get().getId()),
                    runCommand.code(),
                    i + 1 == scenarios.size() ? "PASS" : "ERROR",
                    i + 1,
                    problem.get(),
                    language.get());

            return submissionsRepository.save(submission);
        } finally {
            sandboxProvider.cleanup(box);
        }
    }
}
