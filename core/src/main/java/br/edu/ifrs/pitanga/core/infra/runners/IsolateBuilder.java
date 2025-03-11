package br.edu.ifrs.pitanga.core.infra.runners;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.stream.Stream;

import static br.edu.ifrs.pitanga.core.infra.runners.vo.Config.CHARSET;

import reactor.core.publisher.Mono;

public class IsolateBuilder {
    private List<String> args;

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
