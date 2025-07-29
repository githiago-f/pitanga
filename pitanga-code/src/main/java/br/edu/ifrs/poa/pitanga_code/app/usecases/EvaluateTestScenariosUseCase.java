package br.edu.ifrs.poa.pitanga_code.app.usecases;

import org.springframework.stereotype.Service;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Scenario;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider.Box;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.BuildDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluateTestScenariosUseCase {
    private final SandboxProvider sandboxProvider;

    public void execute(Problem problem) {
        String code = problem.getBaseCode().replace("%%PITANGA_USER_CODE%%", problem.getReviewCode());

        BuildDTO buildDTO = new BuildDTO(code, problem.getBaseLanguage());
        Box identifier = sandboxProvider.setup(buildDTO);

        for (Scenario scenario : problem.getTestingScenarios()) {
            var res = sandboxProvider.execute(identifier, buildDTO, scenario.getInput());

            scenario.setExpectedOutput(res.output());
        }

        sandboxProvider.cleanup(identifier);
    }
}
