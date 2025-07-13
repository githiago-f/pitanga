package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxResult;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;

@Component
@Profile("develop")
public class LocalSandboxProvider implements SandboxProvider {

    @Override
    public List<SandboxResult> execute(SandboxRunRequest runRequest) {
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

}
