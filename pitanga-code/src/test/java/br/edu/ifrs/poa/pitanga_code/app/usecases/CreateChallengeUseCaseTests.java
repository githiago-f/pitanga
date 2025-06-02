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
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.CreateProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.Difficulty;

public class CreateChallengeUseCaseTests {
    private CreateProblemsRepository challengesRepository;
    private LanguagesRepository languagesRepository;
    private List<Language> languages = new ArrayList<>();
    private Authentication user = Mockito.mock(Authentication.class);

    @BeforeEach
    public void setup() {
        challengesRepository = Mockito.mock(CreateProblemsRepository.class);
        Mockito.when(challengesRepository.save(Mockito.any(Problem.class)))
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
    }

    @Test
    public void shouldAllowEveryLanguageIfNoneIsListed() {
        CreateProblemUseCase useCase = new CreateProblemUseCase(challengesRepository, languagesRepository);
        useCase.setUser(user);

        CreateProblemCommand command = new CreateProblemCommand(
                "Hello World!",
                "Make your code to print hello world",
                Difficulty.EASY,
                List.of());

        Problem challenge = useCase.execute(command);
        assertThat("Challenge allow golang", challenge.checkAllow(languages.get(0)));
        assertThat("Challenge allow java", challenge.checkAllow(languages.get(1)));
    }

    @Test
    public void shouldAllowOnlyTheListedLanguages() {
        CreateProblemUseCase useCase = new CreateProblemUseCase(challengesRepository, languagesRepository);
        useCase.setUser(user);

        CreateProblemCommand command = new CreateProblemCommand(
                "Hello World!",
                "Make your code to print hello world",
                Difficulty.EASY,
                List.of(1l));

        Problem challenge = useCase.execute(command);
        assertThat("Challenge allow golang", challenge.checkAllow(languages.get(0)));
        assertThat("Challenge do not allow java", !challenge.checkAllow(languages.get(1)));
    }
}
