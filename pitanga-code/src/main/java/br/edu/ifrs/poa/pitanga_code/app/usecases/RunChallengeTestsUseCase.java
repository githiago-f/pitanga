package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.app.commands.RunTestsCommand;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RunChallengeTestsUseCase {
    private final LanguagesRepository languagesRepository;
    private final SandboxProvider sandboxProvider;

    public List<String> execute(RunTestsCommand code) {
        Optional<Language> language = languagesRepository.findById(code.languageId());

        if (language.isEmpty()) {
            return null;
        }

        Integer boxId = 1;

        try {
            SandboxRunRequest request = new SandboxRunRequest(
                    code.code(),
                    boxId,
                    language.get());

            return sandboxProvider.execute(request);
        } finally {
            sandboxProvider.cleanup(boxId);
        }
    }
}
