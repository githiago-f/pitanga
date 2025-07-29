package br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;

public record BuildDTO(String code, Language language) {
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
