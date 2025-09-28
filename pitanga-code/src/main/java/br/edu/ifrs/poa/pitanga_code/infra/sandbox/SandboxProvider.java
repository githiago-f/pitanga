package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxResult;

import java.nio.file.Path;

import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.BuildDTO;

public interface SandboxProvider {
    public record Box(Integer id, Path path) {
    }

    Box setup(BuildDTO buildDTO);

    void cleanup(Box box);

    SandboxResult execute(Box box, BuildDTO buildDTO, String inputLine);
}
