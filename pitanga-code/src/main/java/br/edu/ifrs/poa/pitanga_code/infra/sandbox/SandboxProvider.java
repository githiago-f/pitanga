package br.edu.ifrs.poa.pitanga_code.infra.sandbox;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public interface SandboxProvider {
    String execute(String code);

    void cleanup();

    void setBoxId(Integer boxId);

    default void writeFile(String workdir, String file, String data) throws IOException {
        Files.writeString(Path.of(workdir, file),
                data,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    default void makeFile(String workdir, String file)
            throws IOException, InterruptedException {
        String fileName = Path.of(workdir, file).toString();
        Process process = new ProcessBuilder("sh", "-c",
                "sudo touch " + fileName + " && sudo chown $(whoami): " + fileName).start();
        process.waitFor();
    }
}
