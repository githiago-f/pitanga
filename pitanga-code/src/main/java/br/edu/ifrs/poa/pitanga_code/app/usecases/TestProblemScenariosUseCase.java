package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.dtos.RunTestsCommand;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.CreateProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class TestProblemScenariosUseCase {
    private final LanguagesRepository languagesRepository;
    private final CreateProblemsRepository challengesRepository;
    private final SandboxProvider sandboxProvider;

    public List<String> execute(RunTestsCommand code) {
        Optional<Problem> challenge = challengesRepository.findById(code.challengeId());
        Optional<Language> language = languagesRepository.findById(code.languageId());

        if (challenge.isEmpty() || language.isEmpty()) {
            return List.of("Challenge or language do not exist.");
        }

        if (!challenge.get().checkAllow(language.get())) {
            return List.of("Cannot use this language");
        }

        SandboxRunRequest request = new SandboxRunRequest(code.code(), code.inputs(), language.get());

        return sandboxProvider.execute(request);
    }
}
