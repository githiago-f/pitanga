package br.edu.ifrs.pitanga.core.infra.runners;

import java.io.File;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import static java.lang.ProcessBuilder.Redirect.PIPE;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import br.edu.ifrs.pitanga.core.domain.pbl.Solution;

@Slf4j
@Component
public class LocalCommandRunner implements CommandRunner {
    private final String ROOT_PATH = "/tmp/pitanga";

    private String runProgram(File dir) throws IOException {
        log.info("Executing solution on dir {}", dir.getPath());
        File inputFile = new File(dir.getPath() + "/input");
        @SuppressWarnings("static-access")
        Process process = new ProcessBuilder()
            .command("bash", "-c", "javac Solution.java && java Solution")
            .directory(dir)
            .redirectInput(PIPE.from(inputFile))
            .start();

        StringBuffer buffer = new StringBuffer();
        try(var reader = process.inputReader(); var error = process.errorReader()) {
            String line;
            while((line = error.readLine()) != null) {
                buffer.append(line);
                Thread.yield();
            }
            while((line = reader.readLine()) != null) {
                buffer.append(line);
                Thread.yield();
            }
        }
        return buffer.toString();
    }

    private File createTempFile(Solution solution, String input)
        throws IOException {
        String prefix = solution.getId().toString();
        File dir = new File(ROOT_PATH + "/" + prefix);
        log.debug("Creating {} directory", dir.getPath());
        Path path = Files.createDirectories(dir.toPath());
        log.debug("{} created", dir.getPath());
        Path filePath = path.resolve("Solution.java");
        Path inputPath = path.resolve("input");

        if(Files.exists(inputPath)) {
            Files.delete(inputPath);
        }

        log.debug("Creating {} file", inputPath.toString());
        Files.createFile(inputPath);
        log.debug("{} created", inputPath.toString());
        Files.write(inputPath, input.getBytes(), StandardOpenOption.WRITE);
        log.info("Persisted data {} to input file {}", input, inputPath);

        if(!Files.exists(filePath)) {
            log.debug("Creating {} file", filePath.toString());
            Files.createFile(filePath);
            log.debug("{} created", filePath.toString());
        }

        byte[] bytes = solution.getCode().getBytes();
        log.debug("Writing code to solution file {}", filePath.toString());
        Files.write(filePath, bytes, StandardOpenOption.WRITE);
        log.debug("Writend {} bytes to {}", bytes.length, filePath.toString());
        return dir;
    }

    @Override
    public String execute(Solution solution, String input) {
        try {
            String challengeId = solution.getId().getChallengeId().toString();
            log.info("Running the solution for challenge {}", challengeId);
            return runProgram(createTempFile(solution, input));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
