package br.edu.ifrs.pitanga.core.infra.runners;

import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import reactor.core.publisher.Mono;

public interface CommandRunner {
    Mono<String> execute(Solution solution, Validation input);
}
