package br.edu.ifrs.pitanga.core.infra.runners;

import br.edu.ifrs.pitanga.core.infra.runners.vo.Submission;
import reactor.core.publisher.Mono;

public interface CommandRunner {
    Mono<String> execute(Submission submissionDTO);
}
