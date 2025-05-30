package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;

public interface SandboxProvider {
    List<String> execute(SandboxRunRequest runRequest);

    void cleanup(Integer boxId);

    default void writeFile(Path file, String data) throws IOException {
        Files.writeString(file, data,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    default void makeFile(Path file)
            throws IOException, InterruptedException {
        String fileName = file.toString();
        Process process = new ProcessBuilder("sh", "-c",
                "sudo touch " + fileName + " && sudo chown $(whoami): " + fileName).start();
        process.waitFor();
    }
}
