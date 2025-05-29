package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.infra.lib.IsolateBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RunChallengeTestsUseCase {
    public List<String> execute() {
        try {
            int box = 1;
            var createBox = IsolateBuilder.builder()
                    .box(box)
                    .cg()
                    .init()
                    .build();

            String workdir = createBox.out().getFirst().trim();

            String sourceFile = workdir + "/box/main.go";
            Process process = new ProcessBuilder("sh", "-c",
                    "sudo touch " + sourceFile + " && sudo chown $(whoami): " + sourceFile).start();
            process.waitFor();

            Files.writeString(Path.of(sourceFile),
                    "package main\nimport \"fmt\"\n\nfunc main() {\n\tfmt.Println(\"Hello, World!\")\n}\n",
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            IsolateBuilder.builder()
                    .silent()
                    .cg()
                    .box(box)
                    .errToOut()
                    .in("/dev/null")
                    .time(100d)
                    .xTime(0d)
                    .fileSize(1024 * 1024)
                    .processesOrThreads(120)
                    .stack(10000)
                    .env("HOME=/tmp")
                    .env("GOCACHE=/tmp/.cache/go-build")
                    .env("GO111MODULE=off")
                    .run("/usr/local/go-1.24.2/bin/go", "build", "/box/main.go")
                    .build();

            var execution = IsolateBuilder.builder()
                    .silent()
                    .cg()
                    .box(box)
                    .errToOut()
                    .in("/dev/null")
                    .time(100d)
                    .xTime(0d)
                    .fileSize(1024 * 1024)
                    .processesOrThreads(120)
                    .stack(10000)
                    .env("HOME=/tmp")
                    .env("GOCACHE=/tmp/.cache/go-build")
                    .env("GO111MODULE=off")
                    .run("./main")
                    .build();

            IsolateBuilder.builder()
                    .silent()
                    .box(box)
                    .clean()
                    .build();

            return execution.out();
        } catch (InterruptedException | IOException e) {
            log.error("Error happened: ", e);
        }

        return List.of();
    }
}
