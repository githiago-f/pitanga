package br.edu.ifrs.poa.pitanga_code.app.http;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import br.edu.ifrs.poa.pitanga_code.app.dtos.RunTestsRequest;
import br.edu.ifrs.poa.pitanga_code.app.dtos.SubmissionRequest;
import br.edu.ifrs.poa.pitanga_code.app.usecases.SubmitProblemSolutionUseCase;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.CodeSubmission;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.ReadSubmissionsRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.SubmissionId;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioOutput;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping({ "/submissions" })
public class SubmissionsController {
    private final SubmitProblemSolutionUseCase submitProblemSolutionUseCase;

    private final ReadSubmissionsRepository readSubmissionsRepository;

    @GetMapping("/{problemId}/{id}")
    public ResponseEntity<CodeSubmission> view(Authentication user, @PathVariable Long problemId,
            @PathVariable Long id) {

        var submissionId = new SubmissionId(id, problemId);
        Optional<CodeSubmission> submission = readSubmissionsRepository.findById(submissionId);

        return submission.filter(i -> i.getCreatorId().equals(user.getName()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{problemId}")
    public Page<CodeSubmission> list(Authentication user, @PathVariable Long problemId,
            Pageable pageable) {
        Specification<CodeSubmission> spec = (root, query, cb) -> cb.equal(root.get("creatorId"), user.getName());
        Specification<CodeSubmission> spec2 = (root, query, cb) -> cb.equal(root.get("id").get("problemId"), problemId);

        return readSubmissionsRepository.findAll(Specification.allOf(spec, spec2), pageable);
    }

    @PostMapping
    public CodeSubmission submit(Authentication user, @RequestBody SubmissionRequest request) {
        submitProblemSolutionUseCase.setUser(user);

        return submitProblemSolutionUseCase.execute(request);
    }

    @PostMapping(value = { "/test" })
    public ResponseEntity<List<ScenarioOutput>> runExamples(@RequestBody RunTestsRequest code) {
        // var results = runChallengeTestsUseCase.execute(code);
        return ResponseEntity.accepted().body(List.of());
    }
}
