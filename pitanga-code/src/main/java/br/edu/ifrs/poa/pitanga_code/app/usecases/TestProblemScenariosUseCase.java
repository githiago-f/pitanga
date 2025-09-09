package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.dtos.RunTestsRequest;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioOutput;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.errors.ProblemNotFoundException;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.errors.TooManyTestInputsException;
import br.edu.ifrs.poa.pitanga_code.domain.coding.errors.InvalidLanguageException;
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
        var maybeProblem = challengesRepository.findById(code.problemId());
        var language = languagesRepository.findById(code.languageId());

        if (code.inputs().size() > 10) {
            throw new TooManyTestInputsException();
        }

        if (maybeProblem.isEmpty()) {
            throw new ProblemNotFoundException(code.problemId());
        }

        if (language.isEmpty()) {
            throw new LanguageNotFoundException(code.languageId());
        }

        if (!maybeProblem.get().checkAllow(language.get())) {
            throw new InvalidLanguageException(maybeProblem.get().getAllowedLanguages());
        }

        var problem = maybeProblem.get();

        var request = new BuildDTO(problem.verificationCode(code.code()), language.get());
        var verificationRequest = new BuildDTO(problem.verificationCode(), problem.getBaseLanguage());

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
