package br.edu.ifrs.pitanga.core.infra.runners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import br.edu.ifrs.pitanga.core.infra.CommandBuilder;
import br.edu.ifrs.pitanga.core.infra.runners.vo.SourceFiles;

@Slf4j
@Component
@AllArgsConstructor
public class IsolateCommandRunner implements CommandRunner {
    private FilesConfiguration files;
    private LimitsConfiguration limits;

    @Override
    public Mono<String> execute(Solution solution, Validation input) {
        log.info("Executing #{}", solution.getId());
        final int boxId = (int) (solution.getId().getVersion() % 1e7);
        return setupWorkdir(boxId, solution, input)
            .flatMap(sf -> {
                return compile(boxId, solution, sf)
                    .flatMap(r -> Mono.just(r.getFirst()));
            });
    }

    private Mono<SourceFiles> setupWorkdir(int boxId, Solution solution, Validation input) {
        return IsolateBuilder.builder().box(boxId).init().build()
            .flatMap(out -> {
                if(out.isEmpty()) return Mono.empty();
                String workdir = out.getFirst().trim();

                SourceFiles sf = new SourceFiles(workdir, solution.getLanguage().getSourceFile(), files);

                return makeFile(sf.getStderrFile())
                    .and(makeFile(sf.getStdoutFile()))
                    .and(makeFile(sf.getStdinFile()))
                    .and(makeFile(sf.getSourceFile()))
                    .and(writeOrError(sf.getSourceFile(), solution.getCode()))
                    .and(writeOrError(sf.getStdinFile(), input.getTestInput()))
                    .thenReturn(sf);
            });
    }

    private Mono<String> makeFile(String file) {
        log.info("Creating {}", file);
        return CommandBuilder.builder()
            .cmd("sudo", "touch", file, "&&", "sudo", "chown", "$(whoami):", file).build()
            .flatMap(s -> s.isEmpty() ? Mono.empty() : Mono.just(s.getFirst()));
    }

    private Mono<Object> writeOrError(String filePath, String data) {
        try {
            Files.write(Path.of(filePath), data.getBytes());
            return Mono.empty();
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    private Mono<List<String>> compile(int boxId, Solution solution, SourceFiles sf) {
        Map<String, String> envs = new HashMap<>();
        envs.put("HOME", "/tmp");
        envs.put("PATH", "\"/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin\"");

        String compileScript = solution.getLanguage().getCompileCmd();
        String compileOutputFile = sf.getStdoutFile();

        return IsolateBuilder.builder()
            .silent()
            .box(boxId).errToOut()
            .in("/dev/null")
            .time(limits.getCpuTime())
            .xTime(0d)
            .wall(limits.getWallTime())
            .stack(limits.getStack())
            .mem(limits.getMemory())
            .fileSize(4096)
            .env(envs)
            .env("LANG", "LANGUAGE", "LC_ALL")
            .dir("/etc:noexec")
            .run("/bin/bash $(basename "+ compileScript +") > " + compileOutputFile)
            .build();
    }
}
