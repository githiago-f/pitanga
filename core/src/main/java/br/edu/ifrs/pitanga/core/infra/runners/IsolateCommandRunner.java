package br.edu.ifrs.pitanga.core.infra.runners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import br.edu.ifrs.pitanga.core.infra.CommandBuilder;
import br.edu.ifrs.pitanga.core.infra.runners.vo.SourceFiles;

@Component
@AllArgsConstructor
public class IsolateCommandRunner implements CommandRunner {
    private FilesConfiguration files;

    @Override
    public Mono<String> execute(Solution solution, Validation input) {
        int boxId = (int) (solution.getId().getVersion() % 1e7);
        setupWorkdir(boxId, solution, input);
        return null;
    }

    private Mono<SourceFiles> setupWorkdir(int boxId, Solution solution, Validation input) {
        return IsolateBuilder.builder().cg().box(boxId).init().build()
            .flatMap(out -> {
                String workdir = out.findFirst().orElse("/tmp/pitanga").trim();

                SourceFiles sf = new SourceFiles(workdir, solution.getLanguage().getSourceFile(), files);

                return makeFile(sf.getStderrFile()).and(makeFile(sf.getStdoutFile()))
                    .and(makeFile(sf.getStdinFile()))
                    .and(writeOrError(sf.getSourceFile(), solution.getCode()))
                    .and(writeOrError(sf.getStdinFile(), input.getTestInput()))
                    .thenReturn(sf);
            });
    }

    private Mono<Optional<String>> makeFile(String file) {
        return CommandBuilder.builder()
            .cmd("sudo", "touch", file, "&&", "sudo", "chown", "$(whoami):", file)
            .build().flatMap(s -> Mono.just(s.findFirst()));
    }

    private Mono<Object> writeOrError(String filePath, String data) {
        try {
            Files.write(Path.of(filePath), data.getBytes());
            return Mono.empty();
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    private Mono<?> compile(int boxId, SourceFiles sf) {
        return null;
    }
}
