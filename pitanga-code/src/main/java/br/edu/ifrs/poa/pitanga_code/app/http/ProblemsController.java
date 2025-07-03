package br.edu.ifrs.poa.pitanga_code.app.http;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.poa.pitanga_code.app.dtos.CreateProblemCommand;
import br.edu.ifrs.poa.pitanga_code.app.usecases.CreateProblemUseCase;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ProblemItem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.ReadProblemsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping({ "/problems" })
public class ProblemsController {
    private final CreateProblemUseCase createChallengeUseCase;
    private final ReadProblemsRepository readProblemsRepository;

    @GetMapping
    public ResponseEntity<Page<ProblemItem>> listProblems(Pageable pageable) {
        Page<Problem> page = readProblemsRepository.findAll(pageable);
        return ResponseEntity.ok(page.map(ProblemItem::new));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Problem> getById(@PathVariable String slug) {
        return readProblemsRepository.findBySlug(slug)
                .map(problem -> ResponseEntity.ok(problem))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Problem> create(Authentication user, @RequestBody CreateProblemCommand data) {
        log.info("User {} is creating the challenge {}", user.getName(), data.title());
        createChallengeUseCase.setUser(user);

        try {
            Problem problem = createChallengeUseCase.execute(data);
            URI problemUri = URI.create("/problems/" + problem.getSlug());
            return ResponseEntity.created(problemUri).body(problem);
        } catch (RuntimeException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
