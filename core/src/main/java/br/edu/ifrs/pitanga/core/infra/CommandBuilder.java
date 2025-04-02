package br.edu.ifrs.pitanga.core.infra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import reactor.core.publisher.Mono;

@Slf4j
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

    public Mono<List<String>> build() {
        try {
            var process = new ProcessBuilder().command(args).start().onExit();
            return Mono.fromFuture(process).flatMap(
                p -> {
                    List<String> result = new ArrayList<>();
                    p.errorReader().lines().forEachOrdered(result::add);
                    p.inputReader().lines().forEachOrdered(result::add);
                    log.debug("Result for command {} is {}", args, result);
                    return Mono.just(result);
                }
            );
        } catch(IOException e) {
            return Mono.error(e);
        }
    }
}
