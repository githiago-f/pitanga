package br.edu.ifrs.poa.pitanga_code.app.usecases;

import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.ifrs.poa.pitanga_code.app.dtos.CreateProblemCommand;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioInput;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Scenario;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.CreateProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.Difficulty;

public class CreateProblemUseCaseTests {
    private CreateProblemsRepository problemsRepository;
    private LanguagesRepository languagesRepository;
    private List<Language> languages = new ArrayList<>();
    private Authentication user = Mockito.mock(Authentication.class);
    private CreateProblemUseCase useCase;

    @BeforeEach
    void setup() {
        problemsRepository = Mockito.mock(CreateProblemsRepository.class);
        Mockito.when(problemsRepository.save(Mockito.any(Problem.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        var lang = Language.builder()
                .sourceFile("")
                .runCommand(new String[] { "" })
                .compileCMD(new String[] { "" });

        languages.add(lang.id(1l).name("golang").build());
        languages.add(lang.id(2l).name("java").build());

        languagesRepository = Mockito.mock(LanguagesRepository.class);
        Mockito.when(languagesRepository.findAllById(Mockito.anyList()))
                .thenAnswer(call -> {
                    List<Language> arr = new ArrayList<>();
                    for (Long id : (List<Long>) call.getArguments()[0]) {
                        Optional<Language> language = languages.stream()
                                .filter(l -> l.getId() == id)
                                .findAny();
                        if (language.isPresent())
                            arr.add(language.get());
                    }
                    return arr;
                });

        useCase = new CreateProblemUseCase(problemsRepository, languagesRepository);
        useCase.setUser(user);
    }

    @Test
    void shouldAllowEveryLanguageIfNoneIsListed() {
        CreateProblemCommand command = new CreateProblemCommand(
                "Hello World!",
                "Make your code to print hello world",
                Difficulty.EASY,
                List.of(),
                List.of());

        Problem problem = useCase.execute(command);
        assertThat("Problem allow golang", problem.checkAllow(languages.get(0)));
        assertThat("Problem allow java", problem.checkAllow(languages.get(1)));
    }

    @Test
    void shouldAllowOnlyTheListedLanguages() {
        CreateProblemCommand command = new CreateProblemCommand(
                "Hello World!",
                "Make your code to print hello world",
                Difficulty.EASY,
                List.of(1l),
                List.of());

        Problem problem = useCase.execute(command);
        assertThat("Problem allow golang", problem.checkAllow(languages.get(0)));
        assertThat("Problem do not allow java", !problem.checkAllow(languages.get(1)));
    }

    @Test
    void shouldPersistTestScenarios() {
        List<ScenarioInput> testingScenarios = List.of(
                new ScenarioInput("[1, 2, 3]", true));
        CreateProblemCommand command = new CreateProblemCommand(
                "Traverse tree",
                "...",
                Difficulty.EASY,
                List.of(),
                testingScenarios);

        Problem problem = useCase.execute(command);

        assertThat("Should include the test scenarios to the problem", !problem.getTestingScenarios().isEmpty());
        for (Scenario scenario : problem.getTestingScenarios()) {
            assertThat("Should exist scenario input", scenario.getInput().equals("[1, 2, 3]"));
            assertThat("Should be an example", scenario.getIsExample());
        }
    }
}
