package br.edu.ifrs.pitanga.core.infra.runners;

import java.io.File;
import java.io.IOException;
import static java.util.concurrent.TimeUnit.SECONDS;

import static java.lang.ProcessBuilder.Redirect.PIPE;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import br.edu.ifrs.pitanga.core.app.http.errors.SolutionProcessingException;
import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.Validation;
import br.edu.ifrs.pitanga.core.infra.TempFileCreator;

@Slf4j
@Component
@AllArgsConstructor
public class LocalCommandRunner implements CommandRunner {
    private TempFileCreator fileCreator;
    private final Long TIMEOUT = 5l;

    private static class SolutionFiles {
        File input, directory;
    }

    @SuppressWarnings("static-access")
    private Mono<String> runProgram(SolutionFiles files) throws IOException {
        log.info("Executing solution on dir {}", files.directory.getPath());
        Process process = new ProcessBuilder()
            .command("bash", "-c", "javac Solution.java && java Solution")
            .directory(files.directory)
            .redirectInput(PIPE.from(files.input))
            .start();

        return Mono.fromFuture(process.onExit().orTimeout(TIMEOUT, SECONDS))
            .onErrorComplete()
            .map(p -> {
                StringBuffer stringBuffer = new StringBuffer();
                p.inputReader().lines().forEachOrdered(i -> stringBuffer.append(i));
                p.errorReader().lines().forEachOrdered(i -> stringBuffer.append(i));
                return stringBuffer.toString();
            })
            .doFinally((signal) -> process.destroyForcibly());
    }

    private SolutionFiles createFiles(Solution solution, Validation validation) throws IOException {
        SolutionFiles solutionFiles = new SolutionFiles();
        String prefix = solution.getId().toString();
        solutionFiles.directory = fileCreator.makePersonalDir(prefix);
        solutionFiles.input = fileCreator.makeInputFile(solution, validation, solutionFiles.directory);
        fileCreator.makeSolutionJavaFile(solution, solutionFiles.directory);
        return solutionFiles;
    }

    @Override
    public Mono<String> execute(Solution solution, Validation input) {
        try {
            String challengeId = solution.getId().getChallengeId().toString();
            log.info("Running the solution for challenge {}", challengeId);
            SolutionFiles files = createFiles(solution, input);
            return runProgram(files);
        } catch(IOException e) {
            e.printStackTrace();
            throw new SolutionProcessingException();
        }
    }
}
