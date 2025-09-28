package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.dtos.CreateProblemRequest;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioInput;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.errors.DuplicatedProblemException;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.coding.errors.LanguageNotFoundException;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.CreateProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.Mediator;
import static br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.Mediator.Message;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Setter
@RequestScope
@RequiredArgsConstructor
public class CreateProblemUseCase {
    private final Mediator mediator;
    private final CreateProblemsRepository problemsRepository;
    private final LanguagesRepository languagesRepository;

    private Authentication user;

    public Problem execute(CreateProblemRequest command) {
        Optional<Language> baseLanguage = languagesRepository.findById(command.baseLanguage());

        if (baseLanguage.isEmpty()) {
            throw new LanguageNotFoundException(command.baseLanguage());
        }

        Problem problem = command.toEntity(user.getName(), baseLanguage.get());

        if (problemsRepository.existsBySlug(problem.getSlug())) {
            log.error("Duplicated problme with slug :: {}", problem.getSlug());
            throw new DuplicatedProblemException(problem.getSlug());
        }

        Problem persistedProblem = problemsRepository.save(problem);
        log.info("Persisted problem {}", persistedProblem.getId());

        int size = command.testingScenarios().size();
        log.info("Including {} scenarios", size);

        for (int i = 0; i < size; i++) {
            ScenarioInput scenario = command.testingScenarios().get(i);
            persistedProblem.includeScenario(i + 1, scenario.toEntity());
        }

        problemsRepository.save(persistedProblem);
        log.info("Problem persisted :: {}", persistedProblem.getId());

        mediator.dispatch(new Message<>("new-problem", persistedProblem.getId()));

        return persistedProblem;
    }
}
