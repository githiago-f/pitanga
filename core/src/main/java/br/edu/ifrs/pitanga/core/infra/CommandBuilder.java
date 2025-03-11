package br.edu.ifrs.pitanga.core.infra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static br.edu.ifrs.pitanga.core.infra.runners.vo.Config.CHARSET;
import reactor.core.publisher.Mono;

public class CommandBuilder {
    private final List<String> args;

    private CommandBuilder() {
        this.args = new ArrayList<>();
    }

    public static CommandBuilder builder() {
        return new CommandBuilder();
    }

    public CommandBuilder cmd(String... parts) {
        for(String part : parts) {
            args.add(part);
        }
        return this;
    }

    public Mono<Stream<String>> build() {
        try {
            var process = new ProcessBuilder().command(args).start().onExit();
            return Mono.fromFuture(process).flatMap(
                p -> Mono.just(p.inputReader(CHARSET).lines())
            );
        } catch(IOException e) {
            return Mono.error(e);
        }
    }
}
