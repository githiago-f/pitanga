package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.app.config.EnvironmentConfiguration;
import br.edu.ifrs.poa.pitanga_code.app.config.FilesConfiguration;
import br.edu.ifrs.poa.pitanga_code.app.config.LimitsConfiguration;
import br.edu.ifrs.poa.pitanga_code.infra.lib.CmdHelper;
import br.edu.ifrs.poa.pitanga_code.infra.lib.IsolateBuilder;
import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.LoadBalanceAlgorithmProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class IsolateSandboxProvider implements SandboxProvider {
    private final FilesConfiguration files;
    private final LimitsConfiguration limits;
    private final EnvironmentConfiguration environment;
    private final LoadBalanceAlgorithmProvider hashProvider;

    private void setupFiles(SandboxRunRequest request, Path path) throws IOException {
        String sourceFile = request.language().getSourceFile();
        Path boxDir = path.resolve("box");
        Path sourcePath = boxDir.resolve(sourceFile);

        CmdHelper.Output res = CmdHelper.builder()
                .run("sudo", "touch", sourcePath.toString())
                .and()
                .run("echo '" + request.code() + "'")
                .pipe()
                .run("sudo", "tee", sourcePath.toString())
                .and()
                .run("echo '" + String.join("\n", request.inputLines()) + "'")
                .pipe()
                .run("sudo", "tee", boxDir.resolve(files.getStdin()).toString())
                .build();

        String out = res.exit() != 0 ? res.err() : res.out();
        log.debug("Cmd exited with {} :: {}", res.exit(), out);
    }

    private String createBox(Integer boxId) throws InterruptedException, IOException {
        var builder = IsolateBuilder.builder().box(boxId).init();
        if (environment.getUseCgroups()) {
            builder.cg();
        }

        var box = builder.build();

        if (box.error().size() != 0)
            throw new RuntimeException(box.errorAsString());

        return box.out().getFirst().trim();
    }

    private IsolateBuilder getIsolateBuilder() {
        IsolateBuilder builder = IsolateBuilder.builder()
                .silent()
                .errToOut()
                .time(limits.getCpuTime())
                .xTime(limits.getExtraCpuTime())
                .fileSize(limits.getFileSize())
                .processesOrThreads(limits.getProcessesAndThreads())
                .stack(limits.getStack())
                .env("HOME=/tmp")
                .env(environment.getDefaultPath());

        if (environment.getUseCgroups()) {
            builder.cg();
        }

        return builder;
    }

    private List<String> build(SandboxRunRequest runRequest, int boxId)
            throws InterruptedException, IOException {
        IsolateBuilder isolate = getIsolateBuilder()
                .box(boxId)
                .in("/dev/null");

        for (String envVar : runRequest.getEnv()) {
            isolate.env(envVar);
        }

        var box = isolate.run(runRequest.getCompile()).build();

        if (box.error().size() != 0)
            throw new RuntimeException(box.errorAsString());

        return box.out();
    }

    private List<String> run(SandboxRunRequest runRequest, int boxId)
            throws InterruptedException, IOException {
        IsolateBuilder isolate = getIsolateBuilder()
                .box(boxId)
                .in(files.getStdin());

        for (String envVar : runRequest.getEnv()) {
            isolate.env(envVar);
        }

        String[] command = runRequest.getRun();

        var box = isolate.run(command).build();

        if (box.error().size() != 0)
            throw new RuntimeException(box.errorAsString());

        return box.out();
    }

    @Override
    public List<String> execute(SandboxRunRequest runRequest) {
        int boxId = hashProvider.getNumber();

        try {
            Path boxDir = Path.of(createBox(boxId));
            log.info("Box created at {}", boxDir);

            setupFiles(runRequest, boxDir);

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
            IsolateBuilder isolateBuilder = IsolateBuilder.builder()
                    .silent()
                    .box(boxId)
                    .clean();

            if (environment.getUseCgroups()) {
                isolateBuilder.cg();
            }

            isolateBuilder.build();
        } catch (IOException | InterruptedException e) {
            log.error("Error on processing", e);
        }
    }
}
