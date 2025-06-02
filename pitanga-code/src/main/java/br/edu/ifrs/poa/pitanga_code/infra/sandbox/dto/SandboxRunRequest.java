package br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto;

import java.util.List;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;

public record SandboxRunRequest(
        String code,
        List<String> inputLines,
        Language language) {

    public String[] getEnv() {
        return language.getEnvironment();
    }

    public String[] getRun() {
        return language.getRunCommand();
    }

    public String[] getCompile() {
        return language.getCompileCMD();
    }
}
