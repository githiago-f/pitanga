package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.config.FilesConfiguration;
// import br.edu.ifrs.poa.pitanga_code.app.config.LimitsConfiguration;
import br.edu.ifrs.poa.pitanga_code.infra.lib.IsolateBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class IsolateSandboxProvider implements SandboxProvider {
    // private final LimitsConfiguration limits;
    private final FilesConfiguration files;

    private Integer boxId;

    public void setBoxId(Integer boxId) {
        this.boxId = boxId;
    }

    private String createBox() throws InterruptedException, IOException {
        var box = IsolateBuilder.builder()
                .box(boxId)
                .cg()
                .init()
                .build();

        if (box.error().size() != 0)
            throw new RuntimeException(box.errorAsString());

        return box.out().getFirst().trim();
    }

    private List<String> build() throws InterruptedException, IOException {
        var buildOutput = IsolateBuilder.builder()
                .silent()
                .cg()
                .box(boxId)
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

        return buildOutput.out();
    }

    private List<String> run() throws InterruptedException, IOException {
        var execution = IsolateBuilder.builder()
                .silent()
                .cg()
                .box(boxId)
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

        return execution.out();
    }

    @Override
    public String execute(String code) {
        try {
            String workdir = createBox();

            for (String file : files.getFiles()) {
                makeFile(workdir, file);
            }

            makeFile(workdir, "box/main.go");
            writeFile(workdir, "box/main.go", code);

            StringBuilder builder = new StringBuilder();
            build().forEach(builder::append);
            run().forEach(builder::append);

            return builder.toString();
        } catch (IOException | InterruptedException e) {
            log.error("Error on processing", e);
        }

        return "Failed";
    }

    @Override
    public void cleanup() {
        try {
            IsolateBuilder.builder()
                    .silent()
                    .box(boxId)
                    .clean()
                    .build();
        } catch (IOException | InterruptedException e) {
            log.error("Error on processing", e);
        }
    }
}
