package br.edu.ifrs.poa.pitanga_code.app.queue;

import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.CreateProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.ProblemStatus;
import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.EventHandler;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.BuildDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("new-problem")
@RequiredArgsConstructor
public class NewProblemEventHandler implements EventHandler<Long> {
    private final CreateProblemsRepository problemsRepository;
    private final SandboxProvider sandboxProvider;

    @Override
    @Transactional
    public void execute(Long problemId) {
        log.debug("Running execute code for problem :: {}", problemId);
        var maybeProblem = problemsRepository.findById(problemId);

        if (maybeProblem.isEmpty()) {
            throw new RuntimeException("Invalid problem with id " + problemId);
        }

        var problem = maybeProblem.get();

        BuildDTO buildDTO = new BuildDTO(problem.getReviewCode(), problem.getBaseLanguage());
        var identifier = sandboxProvider.setup(buildDTO);

        try {
            for (var scenario : problem.getTestingScenarios()) {
                log.debug("Testing scenario [problem={}, id={}]",
                        scenario.getId().getProblemId(),
                        scenario.getId().getId());
                var res = sandboxProvider.execute(identifier, buildDTO, scenario.getInput());

                scenario.setExpectedOutput(res.output());
            }
        } finally {
            sandboxProvider.cleanup(identifier);
        }

        problem.setStatus(ProblemStatus.ACTIVE);

        problemsRepository.save(problem);

        log.debug("Finished running {}", problemId);
    }
}
