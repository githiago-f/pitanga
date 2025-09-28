package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.dtos.RunTestsRequest;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioOutput;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.errors.ProblemNotFoundException;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.errors.TooManyTestInputsException;
import br.edu.ifrs.poa.pitanga_code.domain.coding.errors.LanguageNotFoundException;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.CreateProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider.Box;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.BuildDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequestScope
@RequiredArgsConstructor
public class TestProblemScenariosUseCase {
    private final LanguagesRepository languagesRepository;
    private final CreateProblemsRepository challengesRepository;
    private final SandboxProvider sandboxProvider;

    public List<ScenarioOutput> execute(RunTestsRequest code) {
        if (code.inputs().size() > 10) {
            throw new TooManyTestInputsException();
        }

        var problem = challengesRepository.findById(code.problemId())
                .orElseThrow(() -> new ProblemNotFoundException(code.problemId()));

        var language = languagesRepository.findById(code.languageId())
                .orElseThrow(() -> new LanguageNotFoundException(code.languageId()));

        var request = new BuildDTO(code.code(), language);
        var verificationRequest = new BuildDTO(problem.getReviewCode(), problem.getBaseLanguage());

        Box box = sandboxProvider.setup(request);
        Box verificationBox = sandboxProvider.setup(verificationRequest);

        try {
            return code.inputs().stream()
                    .map(input -> {
                        var result = sandboxProvider.execute(box, request, input);
                        var expectedResult = sandboxProvider.execute(verificationBox, verificationRequest, input);

                        return new ScenarioOutput(input, expectedResult.output(), result.output());
                    })
                    .toList();
        } finally {
            sandboxProvider.cleanup(box);
            sandboxProvider.cleanup(verificationBox);
        }
    }
}
