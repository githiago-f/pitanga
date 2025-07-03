package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;

@Component
@Profile("local")
public class LocalSandboxProvider implements SandboxProvider {

    @Override
    public List<String> execute(SandboxRunRequest runRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

}
