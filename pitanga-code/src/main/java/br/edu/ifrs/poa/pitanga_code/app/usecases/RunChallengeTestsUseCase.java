package br.edu.ifrs.poa.pitanga_code.app.usecases;

import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.app.commands.Code;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RunChallengeTestsUseCase {
    private final SandboxProvider sandboxProvider;

    public String execute(Code code) {
        try {
            sandboxProvider.setBoxId(1);
            return sandboxProvider.execute(code.code());
        } finally {
            sandboxProvider.cleanup();
        }
    }
}
