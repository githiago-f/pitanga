package br.edu.ifrs.poa.pitanga_code.app.http;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import br.edu.ifrs.poa.pitanga_code.app.dtos.CreateProblemRequest;
import br.edu.ifrs.poa.pitanga_code.app.usecases.CreateProblemUseCase;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ProblemItem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.errors.DuplicatedProblemException;
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
    public ResponseEntity<ProblemItem> getBySlug(@PathVariable String slug) {
        return readProblemsRepository.findBySlugWithExamples(slug)
                .map(p -> new ProblemItem(p, false))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<Problem> getById(@PathVariable Long id) {
        return readProblemsRepository.findByIdWithExamples(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Problem> create(Authentication user, @RequestBody CreateProblemRequest data) {
        log.info("User {} is creating the challenge {}", user.getName(), data.title());
        createChallengeUseCase.setUser(user);

        try {
            Problem problem = createChallengeUseCase.execute(data);
            URI problemUri = URI.create("/problems/" + problem.getSlug());
            return ResponseEntity.created(problemUri).body(problem);
        } catch (DuplicatedProblemException duplication) {
            return ResponseEntity.status(409)
                    .header("Location", "/problems/" + data.slug())
                    .build();
        } catch (RuntimeException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
