package br.edu.ifrs.pitanga.core.app.http;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.pitanga.core.app.http.dto.ChallengePageable;
import br.edu.ifrs.pitanga.core.app.http.dto.ChallengeRequest;
import br.edu.ifrs.pitanga.core.domain.pbl.Challenge;
import br.edu.ifrs.pitanga.core.domain.pbl.services.ChallengesService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@AllArgsConstructor
@RequestMapping("/challenges")
public class ChallengesController {
    private final ChallengesService challengesService;

    @GetMapping()
    public Page<Challenge> list(ChallengePageable pageable) {
        return challengesService.handle(pageable);
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<Challenge> getById(@PathVariable UUID challengeId) {
        return challengesService.findById(challengeId)
            .map(ResponseEntity.ok()::body)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{challengeId}")
    public ResponseEntity<Challenge> updateById(
        @PathVariable UUID challengeId, @RequestBody ChallengeRequest request) {
        return ResponseEntity.accepted().build();
    }

    @PostMapping()
    public Challenge createChallenge(@RequestBody ChallengeRequest request) {
        return challengesService.handle(request);
    }
}
