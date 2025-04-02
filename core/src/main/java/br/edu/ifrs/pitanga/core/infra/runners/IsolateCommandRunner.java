package br.edu.ifrs.pitanga.core.infra.runners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import br.edu.ifrs.pitanga.core.infra.CommandBuilder;
import br.edu.ifrs.pitanga.core.infra.IsolateBuilder;
import br.edu.ifrs.pitanga.core.infra.runners.config.FilesConfiguration;
import br.edu.ifrs.pitanga.core.infra.runners.config.LimitsConfiguration;
import br.edu.ifrs.pitanga.core.infra.runners.vo.*;

@Slf4j
@Component
@AllArgsConstructor
public class IsolateCommandRunner implements CommandRunner {
    private FilesConfiguration files;
    private LimitsConfiguration limits;

    @Override
    public Mono<String> execute(Submission submissionDTO) {
        log.info("Executing #{}", submissionDTO.getId());
        final Retry retry = Retry.fixedDelay(
            limits.getRetry(),
            limits.getRetryDuration()
        );

        return setupWorkdir(submissionDTO)
            .flatMap(sf -> {
                return compile(submissionDTO, sf)
                    .flatMap(r -> {
                        if(r == CompileStatus.FAILURE) {
                            return cleanUp(sf).then(Mono.just(""));
                        }
                        return Mono.just("");
                    });
            })
            .retryWhen(retry);
    }

    private Mono<SourceFiles> setupWorkdir(Submission submissionDTO) {
        IsolateBuilder builder = IsolateBuilder.builder()
            .cg()
            .box(submissionDTO.getBoxId())
            .init();
        String langSourceFile = submissionDTO.getLanguage().getSourceFile();

        return builder.build()
            .flatMap(out -> {
                if(out.exitValue() != 0) {
                    RuntimeException e = new RuntimeException(out.errorAsString());
                    return Mono.error(e);
                }
                String workdir = out.out().getFirst().trim();

                SourceFiles sf = new SourceFiles(workdir, langSourceFile, files);

                return makeFile(sf.getStderrFile())
                    .then(makeFile(sf.getStdoutFile()))
                    .then(makeFile(sf.getStdinFile()))
                    .then(makeFile(sf.getSourceFile()))
                    .then(makeFile(sf.getCompileOut()))
                    .then(writeOrError(sf.getSourceFile(), submissionDTO.getCode()))
                    .thenReturn(sf);
            });
    }

    private Mono<String> makeFile(String file) {
        log.info("Creating {}", file);
        return CommandBuilder.builder()
            .cmd("sudo", "touch", file, "&&", "sudo", "chown", "$(whoami):", file).build()
            .flatMap(s -> s.isEmpty() ? Mono.empty() : Mono.just(s.getFirst()));
    }

    private Mono<Void> writeOrError(String filePath, String data) {
        try {
            Files.write(Path.of(filePath), data.getBytes());
            return Mono.empty();
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    private Mono<CompileStatus> compile(Submission submissionDTO, SourceFiles sf) {
        Map<String, String> envs = new HashMap<>();
        envs.put("HOME", "/tmp");
        envs.put("PATH", "\"/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin\"");

        String compileScript = submissionDTO.getLanguage().getCompileCmd();

        var buildCommand = IsolateBuilder.builder()
            .cg()
            .silent()
            .errToOut()
            .box(submissionDTO.getBoxId())
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
            .run(new String[] {
                "/bin/bash",
                "$(basename "+ compileScript + ")",
                " > ",
                sf.getCompileOut()
            })
            .build();

        return buildCommand.flatMap(out -> {
            if(out.exitValue() != 0)
                return Mono.just(CompileStatus.FAILURE);

            Path path = Path.of(sf.getCompileOut());

            try {
                String compileOut = Files.readAllBytes(path).toString();
                log.info("Compile out :: {}", compileOut);
            } catch(IOException e) {
                return Mono.error(e);
            }

            return removeFile(sf.getCompileOut())
                .then(Mono.just(CompileStatus.SUCCESS));
        });
    }

    private Mono<List<String>> fixPermissions(SourceFiles sf) {
        return CommandBuilder.builder()
            .cmd("sudo", "chown", "-R", "$(whoami):", sf.getBoxdir())
            .build();
    }

    private Mono<List<String>> removeFile(String fileName) {
        return CommandBuilder.builder()
            .cmd("sudo", "rm", "-rf", fileName)
            .build();
    }

    private Mono<Void> cleanUp(SourceFiles sf) {
        var removeFromBox = CommandBuilder.builder()
            .cmd("sudo", "rm", "-rf", sf.getBoxdir() + "/*")
            .build();

        return fixPermissions(sf)
            .then(removeFromBox)
            .thenEmpty(null);
    }
}
