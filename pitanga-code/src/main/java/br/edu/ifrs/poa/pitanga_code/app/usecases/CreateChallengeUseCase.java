package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.commands.CreateChallengeCommand;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Challenge;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.ChallengesRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class CreateChallengeUseCase {
    private final ChallengesRepository challengesRepository;
    private final LanguagesRepository languagesRepository;

    private Authentication user;

    public void setUser(Authentication user) {
        this.user = user;
    }

    public Challenge execute(CreateChallengeCommand command) {
        Set<Language> languages = new HashSet<>();
        languagesRepository.findAllById(command.allowedLanguages()).forEach(lang -> {
            log.info("Allow language {} for challenge", lang.getName());
            languages.add(lang);
        });

        Challenge challenge = Challenge.builder()
                .allowedLanguages(languages)
                .title(command.title())
                .description(command.description())
                .customBaseCode(command.baseCode().orElse(null))
                .creator(user.getName())
                .build();

        return challengesRepository.save(challenge);
    }
}
