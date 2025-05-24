package br.edu.ifrs.poa.pitanga_code.app.http;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.poa.pitanga_code.app.commands.CreateChallengeCommand;
import br.edu.ifrs.poa.pitanga_code.app.usecases.CreateChallengeUseCase;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Challenge;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping({ "/challenges" })
public class ChallengesController {
    private final CreateChallengeUseCase createChallengeUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Challenge> create(Authentication user, @RequestBody CreateChallengeCommand data) {
        log.info("User is creating a new challenge {} {}", user, data.title());
        createChallengeUseCase.setUser(user);

        try {
            Challenge challenge = createChallengeUseCase.execute(data);
            return ResponseEntity.ok().body(challenge);
        } catch (RuntimeException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
