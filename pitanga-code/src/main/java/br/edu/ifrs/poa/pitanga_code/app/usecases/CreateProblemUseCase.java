package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.dtos.CreateProblemCommand;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioInput;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.CreateProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class CreateProblemUseCase {
    private final CreateProblemsRepository problemsRepository;
    private final LanguagesRepository languagesRepository;

    private Authentication user;

    public void setUser(Authentication user) {
        this.user = user;
    }

    public Problem execute(CreateProblemCommand command) {
        Set<Language> languages = new HashSet<>();
        log.info("Setting allowed languages");
        languagesRepository.findAllById(command.allowedLanguages()).forEach(lang -> {
            log.info("Allow language {} for challenge", lang.getName());
            languages.add(lang);
        });

        Problem problem = new Problem(
                command.title(),
                command.description(),
                user.getName(),
                command.initialDifficultyLevel(),
                languages);

        if (problemsRepository.existsBySlug(problem.getSlug())) {
            log.error("Duplicated problme with slug :: {}", problem.getSlug());
            throw new RuntimeException("Duplicated problem, check for /problems/" + problem.getSlug());
        }

        int size = command.testingScenarios().size();
        log.info("Including {} scenarios", size);
        for (int i = 0; i < size; i++) {
            ScenarioInput scenario = command.testingScenarios().get(i);
            problem.includeScenario(i, scenario.toEntity());
        }

        Problem persistedProblem = problemsRepository.save(problem);
        log.info("Persisted problem {}", persistedProblem.getId());

        return persistedProblem;
    }
}
