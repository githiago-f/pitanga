package br.edu.ifrs.pitanga.core.infra.runners;

import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import br.edu.ifrs.pitanga.core.domain.pbl.vo.ValidationId;

public interface CommandRunner {
    default String execute(Solution solution) {
        Validation validation = Validation.builder()
            .testInput("")
            .id(ValidationId.builder().id(1l).build())
            .build();
        return this.execute(solution, validation);
    }
    String execute(Solution solution, Validation input);
}
