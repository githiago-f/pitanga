package br.edu.ifrs.poa.pitanga_code.app.queue;

import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.domain.coding.errors.InvalidSubmissionException;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.CreateSubmissionRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.SubmissionId;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioOutput;
import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.EventHandler;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.BuildDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("new-submission")
public class NewSubmissionCodeExecutionHandler implements EventHandler<SubmissionId> {
    private final CreateSubmissionRepository createSubmissionRepository;
    private final SandboxProvider sandboxProvider;

    @Override
    @Transactional
    public void execute(SubmissionId data) {
        var submission = createSubmissionRepository.findById(data)
                .orElseThrow(() -> new InvalidSubmissionException());

        var buildDto = new BuildDTO(submission.getCode(), submission.getLanguage());
        var box = sandboxProvider.setup(buildDto);

        var problem = submission.getProblem();

        try {
            int scenarioIndex = 1;

            for (var scenario : problem.getTestingScenarios()) {
                var response = sandboxProvider.execute(box, buildDto, scenario.getInput());

                var output = new ScenarioOutput(
                        scenario.getInput(),
                        scenario.getExpectedOutput(),
                        response.output());

                if (!output.getPass()) {
                    submission.setFailingScenario(output);
                    break;
                }

                scenarioIndex++;
            }

            submission.setLastScenarioIndex(scenarioIndex);
            createSubmissionRepository.save(submission);
        } finally {
            sandboxProvider.cleanup(box);
        }
    }
}
