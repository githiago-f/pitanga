package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.config.FilesConfiguration;
import br.edu.ifrs.poa.pitanga_code.app.config.LimitsConfiguration;
import br.edu.ifrs.poa.pitanga_code.infra.lib.IsolateBuilder;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class IsolateSandboxProvider implements SandboxProvider {
    private final LimitsConfiguration limits;
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

    private IsolateBuilder getIsolateBuilder() {
        return IsolateBuilder.builder()
                .silent()
                .cg()
                .errToOut()
                .time(limits.getCpuTime())
                .xTime(limits.getExtraCpuTime())
                .fileSize(limits.getFileSize())
                .processesOrThreads(limits.getProcessesAndThreads())
                .stack(limits.getStack())
                .env("HOME=/tmp")
                .env(ISOLATE_PATH);
    }

    private List<String> build(SandboxRunRequest runRequest)
            throws InterruptedException, IOException {
        IsolateBuilder isolate = getIsolateBuilder()
                .box(runRequest.boxId())
                .in("/dev/null");

        for (String envVar : runRequest.getEnv()) {
            isolate.env(envVar);
        }

        return isolate.run(runRequest.getCompile()).build().out();
    }

    private List<String> run(SandboxRunRequest runRequest)
            throws InterruptedException, IOException {
        IsolateBuilder isolate = getIsolateBuilder()
                .box(runRequest.boxId())
                .in(Path.of("/box", files.getStdin()).toString());

        for (String envVar : runRequest.getEnv()) {
            isolate.env(envVar);
        }

        String[] command = runRequest.getRun();
        return isolate.run(command).build().out();
    }

    private void persistInputFile(SandboxRunRequest runRequest, String workdir)
            throws IOException {
        StringBuilder builder = new StringBuilder();
        runRequest.inputLines().forEach(line -> builder.append(line).append('\n'));
        Path inputFile = Path.of(workdir, "box", files.getStdin());
        writeFile(inputFile, builder.toString());
    }

    @Override
    public List<String> execute(SandboxRunRequest runRequest) {
        try {
            String workdir = createBox(runRequest.boxId());

            for (String file : files.getFiles()) {
                makeFile(Path.of(workdir, "box", file));
            }

            Path sourceFile = Path.of(workdir, "box", runRequest.language().getSourceFile());
            makeFile(sourceFile);
            writeFile(sourceFile, runRequest.code());

            persistInputFile(runRequest, workdir);

            List<String> lines = new ArrayList<>();
            if (runRequest.language().getCompileCMD().length != 0) {
                build(runRequest).forEach(lines::add);
            }
            run(runRequest).forEach(lines::add);

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
