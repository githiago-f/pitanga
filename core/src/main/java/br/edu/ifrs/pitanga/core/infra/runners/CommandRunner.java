package br.edu.ifrs.pitanga.core.infra.runners;

import br.edu.ifrs.pitanga.core.domain.pbl.Solution;

public interface CommandRunner {
    default String execute(Solution solution) {
        return this.execute(solution, "");
    }
    String execute(Solution solution, String input);
}
