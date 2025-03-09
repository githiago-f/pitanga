package br.edu.ifrs.pitanga.core.infra.runners;

import org.springframework.stereotype.Component;

import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import reactor.core.publisher.Mono;

@Component
public class IsolateCommandRunner implements CommandRunner {

    @Override
    public Mono<String> execute(Solution solution, Validation input) {
        return null;
    }
}
