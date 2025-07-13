package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.dtos.RunTestsRequest;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.errors.ProblemNotFoundException;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.coding.errors.InvalidLanguageException;
import br.edu.ifrs.poa.pitanga_code.domain.coding.errors.LanguageNotFoundException;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.CreateProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxResult;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequestScope
@RequiredArgsConstructor
public class TestProblemScenariosUseCase {
    private final LanguagesRepository languagesRepository;
    private final CreateProblemsRepository challengesRepository;
    private final SandboxProvider sandboxProvider;

    public List<SandboxResult> execute(RunTestsRequest code) {
        Optional<Problem> problem = challengesRepository.findById(code.problemId());
        Optional<Language> language = languagesRepository.findById(code.languageId());

        if (problem.isEmpty()) {
            log.warn("Problem(id={}) not found when submiting a solution", code.problemId());
            throw new ProblemNotFoundException(code.problemId());
        }

        if (language.isEmpty()) {
            log.warn("Language(id={}) not found when submiting a solution", code.languageId());
            throw new LanguageNotFoundException(code.languageId());
        }

        if (!problem.get().checkAllow(language.get())) {
            throw new InvalidLanguageException(problem.get().getAllowedLanguages());
        }

        SandboxRunRequest request = new SandboxRunRequest(code.code(), code.inputs(), language.get());

        return sandboxProvider.execute(request);
    }
}
