package br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;

public record SandboxRunRequest(
        String code,
        Integer boxId,
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
