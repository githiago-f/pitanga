package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.config.FilesConfiguration;
import br.edu.ifrs.poa.pitanga_code.app.config.LimitsConfiguration;
import br.edu.ifrs.poa.pitanga_code.infra.lib.IsolateBuilder;
import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.LoadBalanceAlgorithmProvider;
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
    private final LoadBalanceAlgorithmProvider hashProvider;

    private final String ISOLATE_PATH = "PATH=\"/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin\"";

    private void writeFile(Path file, String data) throws IOException {
        Files.writeString(file, data,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void makeFiles(int boxId, SandboxRunRequest request) throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("pitanga/" + boxId);
    }

    private String createBox(Integer boxId) throws InterruptedException, IOException {
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

    private List<String> build(SandboxRunRequest runRequest, int boxId)
            throws InterruptedException, IOException {
        IsolateBuilder isolate = getIsolateBuilder()
                .box(boxId)
                .in("/dev/null");

        for (String envVar : runRequest.getEnv()) {
            isolate.env(envVar);
        }

        return isolate.run(runRequest.getCompile()).build().out();
    }

    private List<String> run(SandboxRunRequest runRequest, int boxId)
            throws InterruptedException, IOException {
        IsolateBuilder isolate = getIsolateBuilder()
                .box(boxId)
                .in(Path.of("/" + files.getStdin()).toString());

        for (String envVar : runRequest.getEnv()) {
            isolate.env(envVar);
        }

        String[] command = runRequest.getRun();
        return isolate.run(command).build().out();
    }

    @Override
    public List<String> execute(SandboxRunRequest runRequest) {
        int boxId = hashProvider.getNumber();

        try {
            String workdir = createBox(boxId);
            Path rootDir = Path.of(workdir, "root");

            List<String> lines = new ArrayList<>();
            if (runRequest.language().getCompileCMD().length != 0) {
                build(runRequest, boxId).forEach(lines::add);
            }
            run(runRequest, boxId).forEach(lines::add);

            return lines;
        } catch (IOException | InterruptedException e) {
            log.error("Error on processing", e);
        } finally {
            cleanup(boxId);
        }

        return List.of("Failed");
    }

    public void cleanup(Integer boxId) {
        try {
            IsolateBuilder.builder()
                    .silent()
                    .cg()
                    .box(boxId)
                    .clean()
                    .build();
        } catch (IOException | InterruptedException e) {
            log.error("Error on processing", e);
        }
    }
}
