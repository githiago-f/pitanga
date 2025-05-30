package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.config.FilesConfiguration;
// import br.edu.ifrs.poa.pitanga_code.app.config.LimitsConfiguration;
import br.edu.ifrs.poa.pitanga_code.infra.lib.IsolateBuilder;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class IsolateSandboxProvider implements SandboxProvider {
    // private final LimitsConfiguration limits;
    private final FilesConfiguration files;

    private final String ISOLATE_PATH = "PATH=\"/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin\"";

    private String createBox(Integer boxId)
            throws InterruptedException, IOException {
        var box = IsolateBuilder.builder()
                .box(boxId)
                .cg()
                .init()
                .build();

        if (box.error().size() != 0)
            throw new RuntimeException(box.errorAsString());

        return box.out().getFirst().trim();
    }

    private List<String> build(SandboxRunRequest runRequest)
            throws InterruptedException, IOException {
        IsolateBuilder isolate = IsolateBuilder.builder()
                .silent()
                .cg()
                .box(runRequest.boxId())
                .errToOut()
                .in("/dev/null")
                .time(100d)
                .xTime(0d)
                .fileSize(1024 * 1024)
                .processesOrThreads(120)
                .stack(10000)
                .env("HOME=/tmp")
                .env(ISOLATE_PATH);

        for (String envVar : runRequest.getEnv()) {
            isolate.env(envVar);
        }

        return isolate.run(runRequest.getCompile()).build().out();
    }

    private List<String> run(SandboxRunRequest runRequest, String wordkir)
            throws InterruptedException, IOException {
        IsolateBuilder isolate = IsolateBuilder.builder()
                .silent()
                .cg()
                .box(runRequest.boxId())
                .errToOut()
                .time(100d)
                .xTime(0d)
                .fileSize(1024 * 1024)
                .processesOrThreads(120)
                .stack(10000)
                .env("HOME=/tmp")
                .env(ISOLATE_PATH);

        for (String envVar : runRequest.getEnv()) {
            isolate.env(envVar);
        }

        String[] command = runRequest.getRun();
        return isolate.run(command).build().out();
    }

    @Override
    public List<String> execute(SandboxRunRequest runRequest) {
        try {
            String workdir = createBox(runRequest.boxId());

            for (String file : files.getFiles()) {
                makeFile(Path.of(workdir, file));
            }

            Path sourceFile = Path.of(workdir, "box", runRequest.language().getSourceFile());
            makeFile(sourceFile);
            writeFile(sourceFile, runRequest.code());

            List<String> lines = new ArrayList<>();
            if (runRequest.language().getCompileCMD().length == 0) {
                build(runRequest).forEach(lines::add);
            }
            run(runRequest, workdir).forEach(lines::add);

            return lines;
        } catch (IOException | InterruptedException e) {
            log.error("Error on processing", e);
        }

        return List.of("Failed");
    }

    @Override
    public void cleanup(Integer boxId) {
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
