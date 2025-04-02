package br.edu.ifrs.pitanga.core.infra;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import reactor.core.publisher.Mono;

@Slf4j
public class IsolateBuilder {
    private final List<String> args;

    private IsolateBuilder(List<String> args) {
        this.args = args;
    }

    public static IsolateBuilder builder() {
        List<String> isolateCommand = new ArrayList<>();
        isolateCommand.add("isolate");
        return new IsolateBuilder(isolateCommand);
    }

    public IsolateBuilder cg() {
        args.add("--cg");
        return this;
    }

    public IsolateBuilder init() {
        args.add("--init");
        return this;
    }

    public IsolateBuilder box(int boxId) {
        args.add("-b");
        args.add(String.valueOf(boxId));
        return this;
    }

    public IsolateBuilder silent() {
        args.add("-s");
        return this;
    }

    public IsolateBuilder meta(String metadataFile) {
        args.add("-M");
        args.add(metadataFile);
        return this;
    }

    public IsolateBuilder errToOut() {
        args.add("--stderr-to-stdout");
        return this;
    }

    public IsolateBuilder in(String fileDir) {
        args.add("-i");
        args.add(fileDir);
        return this;
    }

    public IsolateBuilder time(Double time) {
        args.add("-t");
        args.add(time.toString());
        return this;
    }

    public IsolateBuilder xTime(Double time) {
        args.add("-x");
        args.add(String.valueOf(time));
        return this;
    }

    public IsolateBuilder wall(Double time) {
        args.add("-w");
        args.add(String.valueOf(time));
        return this;
    }

    public IsolateBuilder out(String fileDir) {
        args.add("-o");
        args.add(fileDir);
        return this;
    }

    public IsolateBuilder mem(Integer size) {
        args.add("-m");
        args.add(String.valueOf(size));
        return this;
    }

    public IsolateBuilder stack(Integer size) {
        args.add("-k");
        args.add(String.valueOf(size));
        return this;
    }

    public IsolateBuilder fileSize(Integer size) {
        args.add("-f");
        args.add(String.valueOf(size));
        return this;
    }

    public IsolateBuilder dir(String dir) {
        args.add("-d");
        args.add(dir);
        return this;
    }

    public IsolateBuilder env(String... values) {
        for(String val : values) {
            args.add("-E");
            args.add(val);
        }
        return this;
    }

    public IsolateBuilder env(Map<String, String> values) {
        for(Map.Entry<String, String> val : values.entrySet()) {
            args.add(String.format("-E %s=%s", val.getKey(), val.getValue()));
        }
        return this;
    }

    public IsolateBuilder run(String... command) {
        args.add("--run");
        args.add("--");
        args.add(String.join(" ", command));
        return this;
    }

    public record Output(List<String> error, List<String> out, int exitValue) {
        public String errorAsString() {
            StringBuilder sb = new StringBuilder();
            error.forEach(st -> {
                sb.append(st);
                sb.append("\n");
            });
            return sb.toString();
        }
    }

    public Mono<Output> build() {
        try {
            log.info("Command -> {}", args);
            var process = new ProcessBuilder().command(args).start();
            return Mono.fromFuture(process.onExit())
                .flatMap(
                    p -> {
                        Output out = new Output(
                            p.errorReader().lines().toList(),
                            p.inputReader().lines().toList(),
                            p.exitValue()
                        );

                        log.info("Result for command {} is {}", args, out);

                        return Mono.just(out);
                    }
                );
        } catch(IOException e) {
            return Mono.error(e);
        }
    }
}
