package br.edu.ifrs.poa.pitanga_code.app.usecases;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.infra.lib.IsolateBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RunChallengeTestsUseCase {
    private void make(String file) throws IOException, InterruptedException {
        log.info("Creating source files");
        Process process = new ProcessBuilder().command(
                "sudo", "touch", file, "&&",
                "sudo", "chown", "$(whoami):", file).start();

        process.waitFor();
    }

    private void writeOrError(String filePath, String data) throws IOException {
        log.info("Inserting data into file");
        Files.write(Path.of(filePath), data.getBytes());
    }

    public void execute() {
        try {
            var out = IsolateBuilder.builder()
                    .box(1)
                    .cg()
                    .init()
                    .build();

            String workdir = out.out().getFirst().trim();

            make(workdir + "/box/main.go");
            writeOrError(workdir + "/box/main.go",
                    "package main\n\nimport \"fmt\"\n\nfunc main() {\n\tfmt.Println(\"Hello, World!\")\n}\n");

            IsolateBuilder.builder()
                    .silent()
                    .cg()
                    .box(1)
                    .errToOut()
                    .in("/dev/null")
                    .time(100d)
                    .xTime(0d)
                    .stack(10000)
                    .env("HOME=/tmp")
                    .env("GOCACHE=/tmp/.cache/go-build")
                    .run("/usr/local/go-1.24.2/bin/go", "build", "main.go")
                    .build();
        } catch (InterruptedException | IOException e) {
            log.error("Error happened: ", e);
        }
    }
}
