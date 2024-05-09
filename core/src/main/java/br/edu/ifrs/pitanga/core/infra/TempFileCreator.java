package br.edu.ifrs.pitanga.core.infra;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.springframework.stereotype.Component;

import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TempFileCreator {
    private final String ROOT_PATH = "/tmp/pitanga";

    public File makePersonalDir(String prefix) throws IOException {
        File dir = new File(ROOT_PATH + "/" + prefix);
        if(!dir.exists()) {
            log.debug("Creating {} directory", dir.getPath());
            Files.createDirectories(dir.toPath());
            log.debug("{} created", dir.getPath());
        }
        return dir;
    }

    public void makeSolutionJavaFile(Solution solution, File dir) throws IOException {
        Path path = dir.toPath();
        Path filePath = path.resolve("Solution.java");
        if(!Files.exists(filePath)) {
            log.debug("Creating {} file", filePath.toString());
            Files.createFile(filePath);
            log.debug("{} created", filePath.toString());
        }
        byte[] bytes = solution.getCode().getBytes();
        log.debug("Writing code to solution file {}", filePath.toString());
        Files.write(filePath, bytes, StandardOpenOption.WRITE);
        log.debug("Writend {} bytes to {}", bytes.length, filePath.toString());
    }

    public File makeInputFile(Solution solution, Validation validation, File dir) throws IOException {
        Path path = dir.toPath();
        Path inputPath = path.resolve("input_" + validation.getId().getId());
        if(Files.exists(inputPath)) {
            log.info("File already exists");
            return inputPath.toFile();
        }
        log.debug("Creating {} file", inputPath.toString());
        Files.createFile(inputPath);
        byte[] data = validation.getTestInput().getBytes();
        Files.write(inputPath, data, StandardOpenOption.WRITE);
        log.debug("{} created", inputPath.toString());
        return inputPath.toFile();
    }
}
