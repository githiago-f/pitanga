package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.dtos.RunTestsCommand;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Challenge;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.ChallengesRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.LoadBalanceAlgorithmProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class RunChallengeTestsUseCase {
    private final LanguagesRepository languagesRepository;
    private final ChallengesRepository challengesRepository;
    private final SandboxProvider sandboxProvider;
    private final LoadBalanceAlgorithmProvider hashProvider;

    public List<String> execute(RunTestsCommand code) {
        Optional<Challenge> challenge = challengesRepository.findById(code.challengeId());
        Optional<Language> language = languagesRepository.findById(code.languageId());

        if (challenge.isEmpty() || language.isEmpty()) {
            return List.of("Challenge or language do not exist.");
        }

        if (!challenge.get().checkAllow(language.get())) {
            return List.of("Cannot use this language");
        }

        int boxId = hashProvider.getNumber();

        try {
            SandboxRunRequest request = new SandboxRunRequest(
                    code.code(),
                    boxId,
                    code.inputs(),
                    language.get());

            return sandboxProvider.execute(request);
        } finally {
            sandboxProvider.cleanup(boxId);
        }
    }
}
