package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.app.config.EnvironmentConfiguration;
import br.edu.ifrs.poa.pitanga_code.app.config.FilesConfiguration;
import br.edu.ifrs.poa.pitanga_code.app.config.LimitsConfiguration;
import br.edu.ifrs.poa.pitanga_code.infra.lib.CmdHelper;
import br.edu.ifrs.poa.pitanga_code.infra.lib.IsolateBuilder;
import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.LoadBalanceAlgorithmProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.BuildDTO;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("production")
@RequiredArgsConstructor
public class IsolateSandboxProvider implements SandboxProvider {
    private final FilesConfiguration files;
    private final LimitsConfiguration limits;
    private final EnvironmentConfiguration environment;
    private final LoadBalanceAlgorithmProvider hashProvider;

    private void setupFiles(BuildDTO buildDto, Path path) throws IOException {
        String sourceFile = buildDto.language().getSourceFile();
        Path boxDir = path.resolve("box");
        var sourcePath = boxDir.resolve(sourceFile).toString();

        var temp = Files.createTempFile(path.toString().replace("/", "-"), sourceFile);
        Files.write(temp, buildDto.code().getBytes());

        CmdHelper.Output res = CmdHelper.builder()
                .run("sudo", "touch", sourcePath)
                .and()
                .run("cat " + temp.toString())
                .pipe()
                .run("sudo", "tee", sourcePath)
                .build();

        String out = res.exit() != 0 ? res.err() : res.out();
        log.debug("Cmd exited with {} :: {}", res.exit(), out);

        Files.deleteIfExists(temp);
    }

    private void writeStdin(String line, Path path) throws IOException {
        var temp = Files.createTempFile(path.toString().replace("/", "-"), files.getStdin());
        Files.write(temp, (line + "\n").getBytes());

        String stdInFile = path.resolve("box")
                .resolve(files.getStdin())
                .toString();

        CmdHelper.Output res = CmdHelper.builder()
                .run("cat " + temp.toString())
                .pipe()
                .run("sudo", "tee", stdInFile)
                .build();

        String out = res.exit() != 0 ? res.err() : res.out();
        log.debug("Cmd exited with {} :: {}", res.exit(), out);

        Files.deleteIfExists(temp);
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

    private Optional<SandboxResult> build(BuildDTO runRequest, int boxId)
            throws InterruptedException, IOException {
        IsolateBuilder isolate = getIsolateBuilder()
                .box(boxId)
                .filesOpen(0)
                .in("/dev/null");

        for (String envVar : runRequest.getEnv()) {
            isolate.env(envVar);
        }

        var box = isolate.run(runRequest.getCompile()).build();

        if (box.error().size() == 0)
            return Optional.empty();

        return Optional.of(new SandboxResult(box.errorAsString()));
    }

    private SandboxResult run(BuildDTO runRequest, int boxId) {
        IsolateBuilder isolate = getIsolateBuilder()
                .box(boxId)
                .filesOpen(limits.getOpenFiles())
                .in(files.getStdin());

        for (String envVar : runRequest.getEnv()) {
            isolate.env(envVar);
        }

        String[] command = runRequest.getRun();

        try {
            var boxOutput = isolate.run(command).build();

            if (boxOutput.error().size() != 0)
                return new SandboxResult(boxOutput.errorAsString());

            return new SandboxResult(String.join("\n", boxOutput.out()));
        } catch (InterruptedException | IOException e) {
            log.error("System exception", e);
            throw new IllegalStateException("Could not run solution");
        }
    }

    @Override
    public Box setup(BuildDTO buildDTO) {
        int boxId = hashProvider.getNumber();

        try {
            Path boxDir = Path.of(createBox(boxId));

            log.debug("Box created at {}", boxDir);

            setupFiles(buildDTO, boxDir);

            if (buildDTO.hasCompileCommand()) {
                Optional<SandboxResult> result = build(buildDTO, boxId);
                if (result.isPresent()) {
                    throw new IllegalStateException(result.get().output());
                }
            }

            return new Box(boxId, boxDir);
        } catch (InterruptedException | IOException e) {
            log.error("Failed to build code due to {}", e);
            throw new IllegalStateException("Failed to build code");
        }
    }

    @Override
    public SandboxResult execute(Box box, BuildDTO buildDTO, String inputLine) {
        try {
            writeStdin(inputLine, box.path());
            return run(buildDTO, box.id());
        } catch (IOException e) {
            log.error("IO expection on execute");
            return null;
        }
    }

    @Override
    public void cleanup(Box box) {
        try {
            IsolateBuilder isolateBuilder = IsolateBuilder.builder()
                    .silent()
                    .box(box.id())
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
