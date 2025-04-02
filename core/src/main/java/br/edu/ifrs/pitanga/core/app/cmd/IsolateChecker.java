package br.edu.ifrs.pitanga.core.app.cmd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.edu.ifrs.pitanga.core.infra.runners.CommandRunner;
import br.edu.ifrs.pitanga.core.infra.runners.vo.Submission;
import br.edu.ifrs.pitanga.core.infra.runners.vo.SubmissionLanguage;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
final class SubmissionDTO extends Submission {}

@SuperBuilder
final class LanguageDTO extends SubmissionLanguage {}

@Slf4j
@Component
@AllArgsConstructor
public class IsolateChecker implements CommandLineRunner {
    private CommandRunner runner;

    private final LanguageDTO javaLang = LanguageDTO.builder()
        .name("go")
        .sourceFile("program.go")
        .compileCmd("/usr/local/go build program.go")
        .runCommand("./program")
        .build();

    @Override
    public void run(String... args) throws Exception {
        SubmissionDTO submissionDTO = SubmissionDTO.builder()
            .id(5l)
            .code("package main\n\nimport \"fmt\"\n\nfunc main() {\n\s\sfmt.Println(\"Hello, World!\")\n}")
            .language(javaLang)
            .build();

        runner.execute(submissionDTO)
            .subscribe(result -> log.info(result));
    }
}
