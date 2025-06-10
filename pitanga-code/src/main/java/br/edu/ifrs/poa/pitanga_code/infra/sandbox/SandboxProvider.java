package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import java.util.List;

import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;

public interface SandboxProvider {
    List<String> execute(SandboxRunRequest runRequest);
}
